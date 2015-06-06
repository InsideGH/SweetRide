package com.sweetlab.sweetride.engine;

import android.opengl.GLES20;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.demo.DemoApplication;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

import java.util.ArrayList;
import java.util.List;

/**
 * A GL draw frame test
 */
public class FrameDrawTest extends OpenGLTestCase {
    /**
     * The backend context
     */
    private BackendContext mContext;

    /**
     * The render queue.
     */
    private List<RenderNodeTask> mRenderQueue = new ArrayList<>();

    /**
     * The engine root.
     */
    private Node mEngineRoot = new Node();

    /**
     * The frame update.
     */
    private FrameUpdate mFrameUpdate = new FrameUpdate();

    /**
     * The draw frame.
     */
    private FrameDraw mFrameDraw = new FrameDraw();

    /**
     * The demo application.
     */
    private DemoApplication mApplication = new DemoApplication();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();
                return null;
            }
        });

        /**
         * Tell application that engine is initialized.
         */
        mApplication.onInitialized(mEngineRoot, getSurfaceWidth(), getSurfaceHeight());
    }

    public void testDraw() {
        for (int i = 0; i < 30; i++) {
            List<RenderNodeTask> renderList = mFrameUpdate.update(mApplication, mEngineRoot);
            mRenderQueue.addAll(renderList);

            runOnDrawFrame(new ResultRunnable() {
                @Override
                public Object run() {
                    /**
                     * Some gl stuff...to be removed/replaced.
                     */
                    GLES20.glEnable(GLES20.GL_DEPTH_TEST);
                    GLES20.glViewport(0, 0, getSurfaceWidth(), getSurfaceHeight());
                    GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1f);
                    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

                    mFrameDraw.draw(mContext, mRenderQueue);

                    mRenderQueue.clear();
                    return null;
                }
            });
        }
    }
}
