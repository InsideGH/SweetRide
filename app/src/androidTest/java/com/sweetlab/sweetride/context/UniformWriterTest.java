package com.sweetlab.sweetride.context;

import com.sweetlab.sweetride.DebugOptions;
import com.sweetlab.sweetride.shader.FragmentShader;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.shader.VertexShader;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

public class UniformWriterTest extends OpenGLTestCase {

    private BackendContext mContext;
    private ShaderProgram mProgram;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                mContext = getBackendContext();
                VertexShader vertexShader = new VertexShader(ShaderCompilerTest.VALID_VERTEX_SOURCE);
                FragmentShader fragmentShader = new FragmentShader(ShaderCompilerTest.VALID_FRAGMENT_SOURCE);
                mProgram = new ShaderProgram(vertexShader, fragmentShader);
                mProgram.create(mContext);
                assertTrue(mProgram.isCreated());
                return null;
            }
        });
    }

    public void testWriteDefaultFloat() throws Exception {
        if (DebugOptions.DEBUG_UNIFORM_WRITES) {
            boolean gotException = false;
            try {
                mContext.getUniformWriter().writeDefault(mProgram, "u_AmountRed");
            } catch (Exception e) {
                gotException = true;
            }
            assertTrue(gotException);
        } else {
            mContext.getState().useProgram(mProgram);
            mContext.getUniformWriter().writeDefault(mProgram, "u_AmountRed");
        }
    }

    public void testWriteDefaultInt() throws Exception {
        if (DebugOptions.DEBUG_UNIFORM_WRITES) {
            boolean gotException = false;
            try {
                mContext.getUniformWriter().writeDefault(mProgram, "u_AmountRedInt");
            } catch (Exception e) {
                gotException = true;
            }
            assertTrue(gotException);
        } else {
            mContext.getState().useProgram(mProgram);
            mContext.getUniformWriter().writeDefault(mProgram, "u_AmountRedInt");
        }
    }

    public void testWriteFloat() throws Exception {
        float[] data = new float[]{123};
        if (DebugOptions.DEBUG_UNIFORM_WRITES) {
            boolean gotException = false;
            try {
                mContext.getUniformWriter().writeFloat(mProgram, "u_AmountRed", data);
            } catch (Exception e) {
                gotException = true;
            }
            assertTrue(gotException);
        } else {
            mContext.getState().useProgram(mProgram);
            mContext.getUniformWriter().writeFloat(mProgram, "u_AmountRed", data);
        }
    }

    public void testWriteInt() throws Exception {
        int[] data = new int[]{123};
        if (DebugOptions.DEBUG_UNIFORM_WRITES) {
            boolean gotException = false;
            try {
                mContext.getUniformWriter().writeInt(mProgram, "u_AmountRedInt", data);
            } catch (Exception e) {
                gotException = true;
            }
            assertTrue(gotException);
        } else {
            mContext.getState().useProgram(mProgram);
            mContext.getUniformWriter().writeInt(mProgram, "u_AmountRedInt", data);
        }
    }
}