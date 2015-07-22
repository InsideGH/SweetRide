package com.sweetlab.sweetride;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.engine.frame.Frame;
import com.sweetlab.sweetride.node.Node;

/**
 * The engine surface view.
 */
public abstract class EngineView extends GLSurfaceView implements EngineRenderer.Listener {
    /**
     * GL version.
     */
    private static final int VERSION = 2;

    /**
     * Render mode.
     */
    private static final int RENDER_MODE = GLSurfaceView.RENDERMODE_WHEN_DIRTY;

    /**
     * Main handler to get of the GL thread.
     */
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    /**
     * The engine root node.
     */
    private final Node mEngineRoot = new Node();

    /**
     * The frame update task.
     */
    private final FrameUpdateTask mFrameUpdateTask = new FrameUpdateTask();

    /**
     * A render request that can be posted.
     */
    private final RenderRequest mRenderRequest = new RenderRequest();

    /**
     * Thread guard separating application updates on main thread from GL rendering.
     */
    private final Object mGuard = new Object();

    /**
     * The users application.
     */
    private UserApplication mApplication;

    /**
     * The surface width.
     */
    private int mSurfaceWidth;

    /**
     * The surface height.
     */
    private int mSurfaceHeight;

    /**
     * A frame.
     */
    private Frame mFrame = new Frame();

    /**
     * Constructor.
     *
     * @param context Android context.
     */
    public EngineView(Context context) {
        super(context);
        setup();
    }

    /**
     * Constructor.
     *
     * @param context Android context.
     * @param attrs   Attribute set.
     */
    public EngineView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setup();
    }

    @Override
    public void onSurfaceCreated(BackendContext context) {
    }

    @Override
    public void onSurfaceChanged(BackendContext context, int width, int height) {
        mSurfaceWidth = width;
        mSurfaceHeight = height;

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mApplication == null) {
                    mApplication = createUserApplication();
                    mApplication.onInitialized(mEngineRoot, mSurfaceWidth, mSurfaceHeight);
                } else {
                    mApplication.onSurfaceChanged(mSurfaceWidth, mSurfaceHeight);
                }
            }
        });
    }

    @Override
    public void onDrawFrame(BackendContext context) {
        synchronized (mGuard) {
            /**
             * Render frame.
             */
            mFrame.render(context);

            /**
             * Post new application update on the main thread.
             */
            mHandler.post(mFrameUpdateTask);

            /**
             * Enqueue a render request after application update.
             */
            mHandler.post(mRenderRequest);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mEngineRoot.onTouch(event);
    }

    /**
     * Called by the engine to create the user application.
     *
     * @return The user application.
     */
    protected abstract UserApplication createUserApplication();

    /**
     * Setup the android surface.
     */
    private void setup() {
        EngineRenderer renderer = new EngineRenderer(this);
        setEGLContextClientVersion(VERSION);
        setRenderer(renderer);
        setRenderMode(RENDER_MODE);
    }

    /**
     * Frame update runnable. Posted on main thread.
     */
    private class FrameUpdateTask implements Runnable {
        @Override
        public void run() {
            synchronized (mGuard) {
                mFrame.update(mApplication, mEngineRoot);
            }
        }
    }

    /**
     * A render request that can be posted.
     */
    private class RenderRequest implements Runnable {
        @Override
        public void run() {
            requestRender();
        }
    }
}
