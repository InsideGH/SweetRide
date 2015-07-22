package com.sweetlab.sweetride.demo;

import android.content.Context;

import com.sweetlab.sweetride.R;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.rendernode.AndroidRenderNode;

/**
 * Contains two controls, move and turn. Add a move and/or turn
 * touch listener to get feedback.
 */
public class HeadUpDisplay extends AndroidRenderNode {
    /**
     * Margin around controls.
     */
    private static final int CONTROL_MARGIN_DP = 20;

    /**
     * Size of controls.
     */
    private static final int CONTROL_SIZE_DP = 100;

    /**
     * The left quad geometry, used for moving around.
     */
    private final Geometry mMoveGeometry;

    /**
     * The right quad geometry, used for looking around.
     */
    private final Geometry mTurnGeometry;

    /**
     * Constructor. Once added to engine, resources (bitmaps) will be loaded
     * on the first node update.
     *
     * @param context Android application context.
     * @param width   Surface width.
     * @param height  Surface height.
     */
    public HeadUpDisplay(Context context, int width, int height) {
        super(width, height);
        /**
         * Create geometries for move and turn control.
         */
        float size = DpPx.dpToPx(context, CONTROL_SIZE_DP);
        mMoveGeometry = new ControlGeometry(context, R.drawable.compass, size);
        mTurnGeometry = new ControlGeometry(context, R.drawable.turn, size);

        /**
         * Set position and size based on DP.
         */
        float half = size / 2;
        float margin = DpPx.dpToPx(context, CONTROL_MARGIN_DP);
        mMoveGeometry.getModelTransform().translate(half + margin, height - half - margin, 0);
        mTurnGeometry.getModelTransform().translate(width - half - margin, height - half - margin, 0);

        /**
         * Add move and turn control graphics to graph.
         */
        addChild(mMoveGeometry);
        addChild(mTurnGeometry);
    }

    /**
     * Add move touch listener.
     *
     * @param lister The listener.
     */
    public void addMoveTouchListener(OnTouchListener lister) {
        mMoveGeometry.addOnTouchListener(lister);
    }

    /**
     * Add turn touch listener.
     *
     * @param lister The listener.
     */
    public void addTurnTouchListener(OnTouchListener lister) {
        mTurnGeometry.addOnTouchListener(lister);
    }
}
