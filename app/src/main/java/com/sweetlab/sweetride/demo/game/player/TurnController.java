package com.sweetlab.sweetride.demo.game.player;

import com.sweetlab.sweetride.geometry.OnTouchListener;
import com.sweetlab.sweetride.intersect.Ray;

/**
 * Keeps track of amount of turning.
 */
public class TurnController implements OnTouchListener {
    /**
     * The x coordinate that down was registered on.
     */
    private float mDownX;

    /**
     * The y coordinate that down was registered on.
     */
    private float mDownY;

    /**
     * The amount to turn around x axis.
     */
    private float mTurnAroundX;

    /**
     * The amount to turn around y axis.
     */
    private float mTurnAroundY;

    /**
     * Holds of turning or not.
     */
    private boolean mIsTurning;

    @Override
    public boolean onDown(Ray ray, int x, int y) {
        mDownX = x;
        mDownY = y;
        mIsTurning = true;
        return true;
    }

    @Override
    public boolean onUp(Ray ray, int x, int y) {
        mTurnAroundX = 0;
        mTurnAroundY = 0;
        mIsTurning = false;
        return true;
    }

    @Override
    public void onCancel() {
        mTurnAroundX = 0;
        mTurnAroundY = 0;
        mIsTurning = false;
    }

    @Override
    public boolean onMove(Ray ray, boolean isHit, int x, int y) {
        float sizeX = x - mDownX;
        float sizeY = y - mDownY;
        mTurnAroundY = -sizeX * 0.005f;
        mTurnAroundX = -sizeY * 0.005f;
        return false;
    }

    /**
     * Check if player is turning.
     *
     * @return True if turning.
     */
    public boolean isTurning() {
        return mIsTurning;
    }

    /**
     * Get the amount of turning around x axis.
     *
     * @return The amount.
     */
    public float getTurnAroundX() {
        return mTurnAroundX;
    }

    /**
     * Get the amount of turning around y axis.
     *
     * @return The amount.
     */
    public float getTurnAroundY() {
        return mTurnAroundY;
    }
}