package com.sweetlab.sweetride;

import android.opengl.GLSurfaceView;

import com.sweetlab.sweetride.context.BackendContext;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * The engine GL surface renderer.
 */
public class EngineRenderer implements GLSurfaceView.Renderer {
    /**
     * The render listener. Will be called on GL thread.
     */
    private final Listener mListener;

    /**
     * Render listener, is called on GL thread.
     */
    public interface Listener {
        /**
         * Called when the surface has been created.
         *
         * @param context Backend context.
         */
        void onSurfaceCreated(BackendContext context);

        /**
         * Called when the surface has changed.
         *
         * @param context The backend context.
         * @param width   The surface width.
         * @param height  The surface height.
         */
        void onSurfaceChanged(BackendContext context, int width, int height);

        /**
         * Called when draw frame has finished.
         *
         * @param context Backend context.
         */
        void onDrawFrame(BackendContext context);
    }

    /**
     * The backend context.
     */
    private BackendContext mBackendContext;

    /**
     * Constructor. The listener will be called on GL thread.
     *
     * @param listener Listener that will be called on GL thread.
     */
    public EngineRenderer(Listener listener) {
        mListener = listener;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        if (mBackendContext == null) {
            mBackendContext = new BackendContext();
        }
        mListener.onSurfaceCreated(mBackendContext);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mListener.onSurfaceChanged(mBackendContext, width, height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        /**
         * Start of new frame. Reset clear order.
         */
        mBackendContext.onNewFrame();

        /**
         * Drive on draw frame.
         */
        mListener.onDrawFrame(mBackendContext);
    }
}
