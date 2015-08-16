package com.sweetlab.sweetride.engine;

import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.demo.demo.DemoApplication;
import com.sweetlab.sweetride.engine.frame.RenderTask;
import com.sweetlab.sweetride.engine.frame.render.RenderFrame;
import com.sweetlab.sweetride.engine.frame.update.UpdateFrame;
import com.sweetlab.sweetride.pool.RenderTaskPool;
import com.sweetlab.sweetride.rendernode.RenderNode;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

import java.util.ArrayDeque;

/**
 * A GL render frame test
 */
public class RenderFrameTest extends OpenGLTestCase {

    /**
     * The task pool.
     */
    private RenderTaskPool mTaskPool = new RenderTaskPool();

    /**
     * The render queue.
     */
    private ArrayDeque<RenderTask> mRenderQueue = new ArrayDeque<>();

    /**
     * The backend context
     */
    private BackendContext mContext;

    /**
     * The engine root.
     */
    private RenderNode mEngineRoot = new RenderNode();

    /**
     * The frame update.
     */
    private UpdateFrame mUpdateFrame = new UpdateFrame(mTaskPool);

    /**
     * The render frame.
     */
    private RenderFrame mRenderFrame = new RenderFrame(mTaskPool);

    /**
     * The demo application.
     */
    private DemoApplication mApplication;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mApplication = new DemoApplication(getActivity().getApplicationContext());
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
        for (int i = 0; i < 120; i++) {
            mUpdateFrame.update(mApplication, mEngineRoot, mRenderQueue);

            runOnDrawFrame(new ResultRunnable() {
                @Override
                public Object run() {
                    mRenderFrame.render(mContext, mRenderQueue);
                    return null;
                }
            });
        }
    }
}
