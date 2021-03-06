package com.sweetlab.sweetride.testframework;

import android.test.ActivityInstrumentationTestCase2;

import com.sweetlab.sweetride.context.BackendContext;

/**
 * OpenGL instrumentation test case.
 */
public class OpenGLTestCase extends ActivityInstrumentationTestCase2<TestActivity> {
    /**
     * Activity as driver.
     */
    private TestActivity mActivity;

    /**
     * Constructor.
     */
    public OpenGLTestCase() {
        super(TestActivity.class);
    }

    /**
     * Get the surface height.
     *
     * @return The height.
     */
    protected int getSurfaceWidth() {
        assertActivityNotNull();
        return mActivity.getWidth();
    }

    /**
     * Get the surface width.
     *
     * @return The width.
     */
    protected int getSurfaceHeight() {
        assertActivityNotNull();
        return mActivity.getHeight();
    }

    /**
     * Get the backend context.
     *
     * @return The backend context.
     */
    protected BackendContext getBackendContext() {
        assertActivityNotNull();
        return mActivity.getBackendContext();
    }

    /**
     * Set the test information.
     *
     * @param testInfo The test information.
     */
    protected void setTestInfo(final String testInfo) {
        assertActivityNotNull();
        try {
            runTestOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mActivity.setTestInfo(testInfo);
                }
            });
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
    }

    /**
     * Run a task on gl thread.
     *
     * @param task Task to run.
     * @return Result.
     */
    protected Object runOnGLThread(ResultRunnable task) {
        assertActivityNotNull();
        return mActivity.runOnGLThread(task);
    }

    /**
     * Run a task in onDrawFrame.
     *
     * @param task Task to run.
     * @return Result.
     */
    protected Object runOnDrawFrame(ResultRunnable task) {
        assertActivityNotNull();
        return mActivity.runOnDrawFrame(task);
    }

    /**
     * Make the onDrawFrame sleep for a duration.
     *
     * @param duration Duration of sleep.
     */
    protected void sleepOnDrawFrame(final long duration) {
        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                try {
                    Thread.sleep(duration);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
    }

    /**
     * Check that we have activity. Assert if not.
     */
    private void assertActivityNotNull() {
        if (mActivity == null) {
            mActivity = getActivity();
            assertNotNull(mActivity);
        }
    }
}
