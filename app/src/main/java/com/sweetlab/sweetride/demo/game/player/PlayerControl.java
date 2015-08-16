package com.sweetlab.sweetride.demo.game.player;

import android.content.Context;

import com.sweetlab.sweetride.camera.FirstPersonCamera;
import com.sweetlab.sweetride.demo.game.headup.HeadUpDisplay;
import com.sweetlab.sweetride.node.Node;

/**
 * The player control ui.
 */
public class PlayerControl extends Node {
    /**
     * The player head up display (has a orthogonal projection camera).
     */
    private final HeadUpDisplay mHeadUpDisplay;

    /**
     * The player camera (a perspective projection camera).
     */
    private final FirstPersonCamera mCamera;

    /**
     * The move controller.
     */
    private final MoveController mMoveController;

    /**
     * The turn controller.
     */
    private final TurnController mTurnController;

    /**
     * The player control listener.
     */
    private PlayerControlListener mPlayerControlListener;

    /**
     * Constructor.
     *
     * @param camera        The camera to use.
     * @param context       The android context.
     * @param surfaceWidth  The surface width.
     * @param surfaceHeight The surface height.
     */
    public PlayerControl(FirstPersonCamera camera, Context context, int surfaceWidth, int surfaceHeight) {
        mCamera = camera;
        mHeadUpDisplay = new HeadUpDisplay(context, surfaceWidth, surfaceHeight);
        mMoveController = new MoveController();
        mTurnController = new TurnController();
        mHeadUpDisplay.addMoveTouchListener(mMoveController);
        mHeadUpDisplay.addTurnTouchListener(mTurnController);
        addChild(mHeadUpDisplay);
    }

    @Override
    public boolean onUpdate(float dt) {
        float moveX = mMoveController.getMoveX();
        float moveZ = mMoveController.getMoveZ();
        float turnAroundX = mTurnController.getTurnAroundX();
        float turnAroundY = mTurnController.getTurnAroundY();
        mCamera.update(moveX, moveZ, turnAroundX, turnAroundY);

        if (mMoveController.isMoving() || mTurnController.isTurning()) {
            if (mPlayerControlListener != null) {
                mPlayerControlListener.moves();
            }
        }

        return true;
    }

    /**
     * Set player control listener.
     *
     * @param playerControlListener The listener.
     */
    public void setListener(PlayerControlListener playerControlListener) {
        mPlayerControlListener = playerControlListener;
    }
}
