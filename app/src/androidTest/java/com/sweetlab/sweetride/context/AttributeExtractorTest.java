package com.sweetlab.sweetride.context;

import com.sweetlab.sweetride.shader.Attribute;
import com.sweetlab.sweetride.shader.FragmentShader;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.shader.VertexShader;
import com.sweetlab.sweetride.testframework.OpenGLTestCase;
import com.sweetlab.sweetride.testframework.ResultRunnable;

import java.util.Map;

public class AttributeExtractorTest extends OpenGLTestCase {

    /**
     * Backend context.
     */
    private BackendContext mContext;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        runOnGLThread(new ResultRunnable() {
            @Override
            public Object run() {
                // Assures that the backend context is not null.
                mContext = getBackendContext();
                return null;
            }
        });
    }

    /**
     * Test extract attributes from a shader program.
     *
     * @throws Exception
     */
    public void testAttributeExtractorTest() throws Exception {
        final VertexShader vertexShader = new VertexShader(ShaderCompilerTest.VALID_VERTEX_SOURCE);
        final FragmentShader fragmentShader = new FragmentShader(ShaderCompilerTest.VALID_FRAGMENT_SOURCE);
        final ShaderProgram program = new ShaderProgram(vertexShader, fragmentShader);

        runOnDrawFrame(new ResultRunnable() {
            @Override
            public Object run() {
                vertexShader.create(mContext);
                fragmentShader.create(mContext);
                program.create(mContext);
                Map<String, Attribute> attributeMap = mContext.getAttributeExtractor().extract(program);

                assertEquals(3, attributeMap.size());
                assertTrue(attributeMap.containsKey("a_Pos"));
                assertTrue(attributeMap.containsKey("a_Color"));
                assertTrue(attributeMap.containsKey("a_TexCoord"));

                return null;
            }
        });
    }
}