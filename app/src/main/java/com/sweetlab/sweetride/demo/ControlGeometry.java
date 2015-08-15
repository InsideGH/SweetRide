package com.sweetlab.sweetride.demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sweetlab.sweetride.context.MagFilter;
import com.sweetlab.sweetride.context.MinFilter;
import com.sweetlab.sweetride.demo.mesh.QuadMesh;
import com.sweetlab.sweetride.geometry.Geometry;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.node.NodeController;
import com.sweetlab.sweetride.node.RenderSettings;
import com.sweetlab.sweetride.node.rendersettings.BlendDstFact;
import com.sweetlab.sweetride.node.rendersettings.BlendSrcFact;
import com.sweetlab.sweetride.shader.FragmentShader;
import com.sweetlab.sweetride.shader.ShaderProgram;
import com.sweetlab.sweetride.shader.VertexShader;
import com.sweetlab.sweetride.texture.Texture2D;

import rx.functions.Action1;

/**
 * Control geometry. This is Geometry with a texture that is
 * loaded using a node controller.
 * <p/>
 * This is used as the graphical representation of the moving
 * and turning controls in the demo application.
 */
public class ControlGeometry extends Geometry {
    /**
     * Vertex shader.
     */
    private static final String VERTEX_SHADER =
            "attribute vec4 a_Pos; \n" +
                    "uniform mat4 u_worldViewProjMat; \n" +
                    "attribute vec2 a_texCoord;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "void main() { " +
                    "    v_texCoord = a_texCoord;\n" +
                    "    gl_Position = u_worldViewProjMat * a_Pos;" +
                    "} ";


    /**
     * Fragment shader.
     */
    private static final String FRAGMENT_SHADER =
            "precision mediump float;\n" +
                    "varying vec2 v_texCoord;\n" +
                    "uniform sampler2D s_texture;\n" +
                    "void main() {\n" +
                    "vec4 color = texture2D(s_texture, v_texCoord);\n" +
                    "gl_FragColor = color;\n" +
                    "}";

    /**
     * Contructor.
     *
     * @param resource Android image resource.
     * @param size     Size in model space.
     */
    public ControlGeometry(Context context, int resource, float size) {
        /**
         * Set render settings.
         */
        RenderSettings renderSettings = getRenderSettings();
        renderSettings.setBlend(true);
        renderSettings.setBlendFact(BlendSrcFact.SRC_ALPHA, BlendDstFact.ONE_MINUS_SRC_ALPHA);

        /**
         * Create and set material.
         */
        Material material = new Material();
        ShaderProgram program = new ShaderProgram(new VertexShader(VERTEX_SHADER), new FragmentShader(FRAGMENT_SHADER));
        material.setShaderProgram(program);
        setMaterial(material);

        /**
         * Set the mesh.
         */
        setMesh(new QuadMesh(size, size, "a_Pos", "a_texCoord"));

        /**
         * Load bitmap resources.
         */
        addNodeController(new ResourceLoader(context, resource));
    }

    /**
     * A resource loader that will be called once the first update.
     */
    private class ResourceLoader implements NodeController {
        private final Context mContext;
        private final int mResource;

        /**
         * Constructor.
         *
         * @param context  Android context.
         * @param resource Android image resource.
         */
        public ResourceLoader(Context context, int resource) {
            mContext = context;
            mResource = resource;
        }

        @Override
        public boolean onUpdate(float dt) {
            /**
             * Bitmap config.
             */
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inPreferredConfig = Bitmap.Config.ARGB_8888;

            /**
             * Decode and create texture.
             */
            new AssetsLoader(mContext).loadBitmapAsync(mResource, opts).subscribe(new Action1<Bitmap>() {
                @Override
                public void call(Bitmap bitmap) {
                    Material material = getMaterial();
                    if (material != null) {
                        Texture2D texture = new Texture2D("s_texture", bitmap, MinFilter.NEAREST, MagFilter.NEAREST);
                        material.addTexture(texture);
                    }
                }
            });

            /**
             * Return false means that this node controller is finished and is removed.
             */
            return false;
        }
    }
}
