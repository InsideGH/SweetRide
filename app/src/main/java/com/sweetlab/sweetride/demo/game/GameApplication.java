package com.sweetlab.sweetride.demo.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.sweetlab.sweetride.R;
import com.sweetlab.sweetride.UserApplication;
import com.sweetlab.sweetride.camera.FirstPersonCamera;
import com.sweetlab.sweetride.camera.Frustrum;
import com.sweetlab.sweetride.context.MagFilter;
import com.sweetlab.sweetride.context.MinFilter;
import com.sweetlab.sweetride.demo.game.assets.AssetsLoader;
import com.sweetlab.sweetride.demo.game.player.PlayerControl;
import com.sweetlab.sweetride.demo.game.player.PlayerControlListener;
import com.sweetlab.sweetride.demo.game.terrain.newtake.Grid;
import com.sweetlab.sweetride.demo.game.terrain.newtake.UnlimitedTerrain;
import com.sweetlab.sweetride.math.Vec3;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.node.rendersettings.BlendDstFact;
import com.sweetlab.sweetride.node.rendersettings.BlendSrcFact;
import com.sweetlab.sweetride.node.rendersettings.ClearBit;
import com.sweetlab.sweetride.rendernode.DefaultRenderNode;
import com.sweetlab.sweetride.texture.Texture2D;

import rx.functions.Action1;

/**
 * A game application.
 */
public class GameApplication extends UserApplication {
    /**
     * The size of a grid in world space gl.
     */
    private static final float GRID_SIDE_SIZE = 4f;

    /**
     * Camera field of view.
     */
    private static final float FIELD_OF_VIEW = 90;

    /**
     * The camera near field.
     */
    private static final float NEAR_FIELD = 0.1f;

    /**
     * The camera far field.
     */
    private static final float FAR_FIELD = 100f;

    /**
     * Default system window render node.
     */
    private final DefaultRenderNode mGameRenderNode = new DefaultRenderNode();

    /**
     * Android context.
     */
    private final Context mContext;

    /**
     * The grid the terrain is in.
     */
    private final Grid mGrid;

    /**
     * The camera used.
     */
    private FirstPersonCamera mCamera;

    /**
     * The player controls.
     */
    private PlayerControl mPlayerControl;

    /**
     * The terrain.
     */
    private UnlimitedTerrain mUnlimitedTerrain;

    /**
     * Constructor.
     */
    public GameApplication(Context context) {
        mContext = context;
        mGameRenderNode.enableViewFrustrumCulling(true);
        mGameRenderNode.getRenderSettings().setClearColor(new float[]{0.0f, 0.0f, 0.0f, 1});
        mGameRenderNode.getRenderSettings().setClear(0, ClearBit.COLOR_BUFFER_BIT, ClearBit.DEPTH_BUFFER_BIT);
        mGameRenderNode.getRenderSettings().setDepthTest(true);
        mGameRenderNode.getRenderSettings().setBlend(true);
        mGameRenderNode.getRenderSettings().setBlendFact(BlendSrcFact.SRC_ALPHA, BlendDstFact.ONE_MINUS_SRC_ALPHA);

        mGrid = new Grid(GRID_SIDE_SIZE);

        AssetsLoader assetsLoader = new AssetsLoader(mContext.getApplicationContext());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        assetsLoader.loadBitmap(R.drawable.grass, options).subscribe(new Action1<Bitmap>() {
            @Override
            public void call(Bitmap bitmap) {
                Texture2D texture = new Texture2D("s_texture", bitmap, MinFilter.NEAREST, MagFilter.NEAREST);
                mUnlimitedTerrain = new UnlimitedTerrain(0, 0, mGrid, texture);
                mGameRenderNode.addChild(mUnlimitedTerrain);
            }
        });

    }


    @Override
    public void onInitialized(Node engineRoot, int surfaceWidth, int surfaceHeight) {
        /**
         * Add the render node to the engine root.
         */
        engineRoot.addChild(mGameRenderNode);

        /**
         * Create, setup and attach camera to game render node.
         */
        mCamera = new FirstPersonCamera();
        mCamera.lookAt(0, 2, 0, 0, 2, -1);
        mCamera.getFrustrum().setPerspectiveProjection(FIELD_OF_VIEW, Frustrum.FovType.AUTO_FIT, NEAR_FIELD, FAR_FIELD, surfaceWidth, surfaceHeight);
        mGameRenderNode.setCamera(mCamera);

        /**
         * Create and attach player controls.
         */
        mPlayerControl = new PlayerControl(mCamera, mContext, surfaceWidth, surfaceHeight);
        engineRoot.addChild(mPlayerControl);

        /**
         * Add a player control listener.
         */
        mPlayerControl.setListener(new PlayerListener());
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
    }

    @Override
    public void onUpdate(float dt) {
    }

    /**
     * The player listener.
     */
    private class PlayerListener implements PlayerControlListener {
        private final Vec3 mPos = new Vec3();

        @Override
        public void moves() {
            mCamera.getPosition(mPos);
            int cameraGridPosX = mGrid.convertToGridPos(mPos.x);
            int cameraGridPosZ = mGrid.convertToGridPos(mPos.z);

            int terrainPosX = mUnlimitedTerrain.getGridPosX();
            int terrainPosZ = mUnlimitedTerrain.getGridPosZ();
            if (terrainPosX != cameraGridPosX || terrainPosZ != cameraGridPosZ) {
                Log.d("Peter100", "mPos = " + mPos + " cameraGridPosXZ = " + cameraGridPosX + " " + cameraGridPosZ);
                Log.d("Peter100", "Repositioning terrain to " + cameraGridPosX + " " + cameraGridPosZ);
                mUnlimitedTerrain.rePositionMap(cameraGridPosZ, cameraGridPosX);
            }
        }
    }
}
