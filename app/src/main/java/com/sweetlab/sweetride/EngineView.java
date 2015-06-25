package com.sweetlab.sweetride;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Looper;
import android.util.AttributeSet;
import android.util.Log;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.engine.FrameDraw;
import com.sweetlab.sweetride.engine.FrameUpdate;
import com.sweetlab.sweetride.engine.rendernode.RenderNodeTask;
import com.sweetlab.sweetride.math.Vec4;
import com.sweetlab.sweetride.node.Node;

import java.util.ArrayList;
import java.util.List;

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
     * The frame draw.
     */
    private final FrameDraw mFrameDraw = new FrameDraw();

    /**
     * A render request that can be posted.
     */
    private final RenderRequest mRenderRequest = new RenderRequest();

    /**
     * The render queue.
     */
    private final List<RenderNodeTask> mRenderQueue = new ArrayList<>();

    /**
     * Thread guard separating application updates on main thread from GL rendering.
     */
    private final Object mGuard = new Object();

    /**
     * The users application.
     */
    private UserApplication mApplication;

    /**
     * The background color.
     */
    private Vec4 mBackgroundColor = new Vec4();

    /**
     * The surface width.
     */
    private int mSurfaceWidth;

    /**
     * The surface height.
     */
    private int mSurfaceHeight;

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
        Log.d("Peter100", "Max number of texture units = " + context.getCapabilities().getMaxNumberTextureUnits());
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
                    mBackgroundColor.set(getBackgroundColor());
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
             * Some gl stuff...to be replaced.
             */
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            GLES20.glEnable(GLES20.GL_BLEND);
            GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
            GLES20.glViewport(0, 0, mSurfaceWidth, mSurfaceHeight);
            GLES20.glClearColor(mBackgroundColor.x, mBackgroundColor.y, mBackgroundColor.z, mBackgroundColor.w);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

            /**
             * Draw the frame.
             */
            mFrameDraw.draw(context, mRenderQueue);

            /**
             * Clear the render queue.
             */
            mRenderQueue.clear();

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

    /**
     * Called by the engine to create the user application.
     *
     * @return The user application.
     */
    protected abstract UserApplication createUserApplication();

    /**
     * Called by the engine to get the background color.
     *
     * @return The background color.
     */
    protected abstract Vec4 getBackgroundColor();

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
        /**
         * The frame update.
         */
        private final FrameUpdate mFrameUpdate = new FrameUpdate();

        @Override
        public void run() {
            synchronized (mGuard) {
                mRenderQueue.addAll(mFrameUpdate.update(mApplication, mEngineRoot));
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
