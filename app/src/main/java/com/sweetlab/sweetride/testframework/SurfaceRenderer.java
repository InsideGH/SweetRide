package com.sweetlab.sweetride.testframework;

import android.opengl.GLSurfaceView;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.util.Util;

import junit.framework.Assert;

import java.util.ArrayDeque;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * A surface renderer used for testing.
 */
public class SurfaceRenderer implements GLSurfaceView.Renderer {
    /**
     * A queue of tasks.
     */
    private final ArrayDeque<LatchedRunnable> mQueue = new ArrayDeque<>();

    /**
     * Surface width.
     */
    private int mWidth;

    /**
     * Surface height.
     */
    private int mHeight;

    /**
     * Backend context.
     */
    private BackendContext mBackendContext;

    /**
     * Constructor.
     */
    public SurfaceRenderer() {
    }

    @Override
    public synchronized void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mBackendContext = new BackendContext();
    }

    @Override
    public synchronized void onSurfaceChanged(GL10 gl, int width, int height) {
        mWidth = width;
        mHeight = height;
    }

    @Override
    public synchronized void onDrawFrame(GL10 gl) {
        mBackendContext.onNewFrame();

        if (!mQueue.isEmpty()) {
            LatchedRunnable latchedRunnable = mQueue.removeFirst();
            latchedRunnable.run();
            Assert.assertFalse(Util.hasGlError());
        }
    }

    /**
     * Get the backend context.
     *
     * @return The context.
     */
    public synchronized BackendContext getBackendContext() {
        return mBackendContext;
    }

    /**
     * Enqueue tasks to run at onDrawFrame.
     *
     * @param frameTask Task.
     */
    public synchronized void enqueue(LatchedRunnable frameTask) {
        mQueue.addLast(frameTask);
    }

    /**
     * Get the surface width.
     *
     * @return The width.
     */
    public synchronized int getWidth() {
        return mWidth;
    }

    /**
     * Get the surface height.
     *
     * @return The height.
     */
    public synchronized int getHeight() {
        return mHeight;
    }
}
