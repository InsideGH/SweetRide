package com.sweetlab.sweetride.testframework;

import com.sweetlab.sweetride.util.Util;

import junit.framework.Assert;

import java.util.concurrent.CountDownLatch;

/**
 * Latched runnable where calling thread can wait and retrieve the result.
 */
public class LatchedRunnable implements Runnable {
    /**
     * Count down latch.
     */
    private final CountDownLatch mLatch;

    /**
     * Runnable with return statement.
     */
    private final ResultRunnable mRunnable;

    /**
     * Holding the result from the runnable.
     */
    private Object mResult;

    /**
     * Constructor.
     *
     * @param runnable Runnable to run.
     * @param latch    Latch to count down when finished with runnable.
     */
    public LatchedRunnable(ResultRunnable runnable, CountDownLatch latch) {
        mLatch = latch;
        mRunnable = runnable;
        mResult = null;
    }

    @Override
    public void run() {
        mResult = mRunnable.run();
        Assert.assertFalse(Util.hasGlError());
        mLatch.countDown();
    }

    /**
     * Get the result from the runnable.
     *
     * @return The result.
     */
    public Object getResult() {
        return mResult;
    }
}
