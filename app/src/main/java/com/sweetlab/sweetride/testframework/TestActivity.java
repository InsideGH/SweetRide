package com.sweetlab.sweetride.testframework;

import android.app.Activity;
import android.opengl.GLSurfaceView;
import android.os.Bundle;

import com.sweetlab.sweetride.context.BackendContext;

import java.util.concurrent.CountDownLatch;

/**
 * A test activity for GL required testing.
 */
public class TestActivity extends Activity {
    private static final int VERSION = 2;
    private static final int RENDER_MODE = GLSurfaceView.RENDERMODE_WHEN_DIRTY;
    /**
     * Android GLSurfaceView.
     */
    private GLSurfaceView mGLSurfaceView;

    /**
     * Android SurfaceRenderer.
     */
    private SurfaceRenderer mTestRenderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTestRenderer = new SurfaceRenderer();

        mGLSurfaceView = new GLSurfaceView(getApplicationContext());
        mGLSurfaceView.setEGLContextClientVersion(VERSION);
        mGLSurfaceView.setRenderer(mTestRenderer);
        mGLSurfaceView.setRenderMode(RENDER_MODE);

        setContentView(mGLSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLSurfaceView.onResume();
    }

    /**
     * Get surface width.
     *
     * @return The width.
     */
    public int getWidth() {
        return mTestRenderer.getWidth();
    }

    /**
     * Get surface height.
     *
     * @return The height.
     */
    public int getHeight() {
        return mTestRenderer.getHeight();
    }

    /**
     * Get the backend context.
     *
     * @return The backend context.
     */
    public BackendContext getBackendContext() {
        return mTestRenderer.getBackendContext();
    }

    /**
     * Run task on gl thread. Waits for task to finish.
     *
     * @param runnable Task to run.
     * @return Result.
     */
    public Object runOnGLThread(final ResultRunnable runnable) {
        final CountDownLatch latch = new CountDownLatch(1);
        LatchedRunnable latchedRunnable = new LatchedRunnable(runnable, latch);

        mGLSurfaceView.queueEvent(latchedRunnable);
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return latchedRunnable.getResult();
    }

    /**
     * Run task on gl thread in onDrawFrame. Waits for task to finish.
     *
     * @param runnable Task to run.
     * @return Result.
     */
    public Object runOnDrawFrame(ResultRunnable runnable) {
        final CountDownLatch latch = new CountDownLatch(1);
        LatchedRunnable latchedRunnable = new LatchedRunnable(runnable, latch);

        mTestRenderer.enqueue(latchedRunnable);
        mGLSurfaceView.requestRender();
        try {
            latch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return latchedRunnable.getResult();
    }
}
