package com.sweetlab.sweetride.node;

import com.sweetlab.sweetride.math.Transform;

/**
 * A transform that can be marked.
 */
public class MarkTransform extends Transform {

    /**
     * The mark.
     */
    private boolean mMark;

    /**
     * Mark.
     */
    public void mark() {
        mMark = true;
    }

    /**
     * Clear mark.
     */
    public void clearMark() {
        mMark = false;
    }

    /**
     * Is marked?
     *
     * @return True if marked.
     */
    public boolean isMarked() {
        return mMark;
    }
}
