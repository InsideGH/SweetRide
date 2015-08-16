package com.sweetlab.sweetride.demo.game.player;

import com.sweetlab.sweetride.geometry.OnTouchListener;
import com.sweetlab.sweetride.intersect.Ray;

/**
 * Keeps track of amount of movement.
 */
public class MoveController implements OnTouchListener {
    /**
     * The x coordinate that down was registered on.
     */
    private float mDownX;

    /**
     * The y coordinate that down was registered on.
     */
    private float mDownY;

    /**
     * The amount to move along x axis.
     */
    private float mStrafeX;

    /**
     * The amount to move along z axis.
     */
    private float mStrafeZ;

    /**
     * Hold if moving or not.
     */
    private boolean mIsMoving;

    @Override
    public boolean onDown(Ray ray, int x, int y) {
        mDownX = x;
        mDownY = y;
        mIsMoving = true;
        return true;
    }

    @Override
    public boolean onUp(Ray ray, int x, int y) {
        mStrafeX = 0;
        mStrafeZ = 0;
        mIsMoving = false;
        return true;
    }

    @Override
    public void onCancel() {
        mStrafeX = 0;
        mStrafeZ = 0;
        mIsMoving = false;
    }

    @Override
    public boolean onMove(Ray ray, boolean isHit, int x, int y) {
        float sizeX = x - mDownX;
        float sizeY = y - mDownY;
        mStrafeX = sizeX * 0.0005f;
        mStrafeZ = -sizeY * 0.0005f;
        return false;
    }

    /**
     * Check if player is moving.
     *
     * @return True if moving.
     */
    public boolean isMoving() {
        return mIsMoving;
    }

    /**
     * Get the amount of movement along x axis.
     *
     * @return The amount.
     */
    public float getMoveX() {
        return mStrafeX;
    }

    /**
     * Get the amount of movement along z axis.
     *
     * @return The amount.
     */
    public float getMoveZ() {
        return mStrafeZ;
    }
}