package com.sweetlab.sweetride.tryouts.stars;

import com.sweetlab.sweetride.UserApplication;
import com.sweetlab.sweetride.Util.Verify;
import com.sweetlab.sweetride.engine.FrontEndActionHandler;
import com.sweetlab.sweetride.engine.frame.Frame;
import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.node.rendersettings.ClearBit;
import com.sweetlab.sweetride.renderer.DefaultNodeRenderer;
import com.sweetlab.sweetride.rendernode.RenderNode;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

/**
 * Clear screen using just one node (no geometry).
 */
public class BareMinimumTest extends OpenGLTestCase {

    private RenderNode mRenderNode;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mRenderNode = new RenderNode();
        mRenderNode.getRenderSettings().setClearColor(new float[]{1, 0, 0, 1});
        mRenderNode.getRenderSettings().setClear(0, ClearBit.COLOR_BUFFER_BIT);
        mRenderNode.getRenderSettings().setViewPort(0, 0, getSurfaceWidth(), getSurfaceHeight());
    }

    public void test() {
        new FrontEndActionHandler().handleActions(mRenderNode);

        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                mRenderNode.draw(getBackendContext());
                return null;
            }
        });

        sleepOnDrawFrame(Verify.TERMINATE_TIME);
    }

    public void test2() {
        mRenderNode.setRenderer(new DefaultNodeRenderer());

        final Frame frame = new Frame();
        frame.update(new App(), mRenderNode);

        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                frame.render(getBackendContext());
                return null;
            }
        });

        sleepOnDrawFrame(Verify.TERMINATE_TIME);
    }

    private class App extends UserApplication {
        @Override
        public void onInitialized(Node engineRoot, int width, int height) {

        }

        @Override
        public void onSurfaceChanged(int width, int height) {

        }

        @Override
        public void onUpdate(float dt) {

        }
    }
}
