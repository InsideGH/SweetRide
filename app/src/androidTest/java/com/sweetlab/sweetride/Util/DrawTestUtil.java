package com.sweetlab.sweetride.Util;

import android.opengl.GLES20;

import com.sweetlab.sweetride.attributedata.AttributePointer;
import com.sweetlab.sweetride.attributedata.IndicesBuffer;
import com.sweetlab.sweetride.attributedata.InterleavedVertexBuffer;
import com.sweetlab.sweetride.attributedata.VertexBuffer;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.context.TextureUnit;
import com.sweetlab.sweetride.material.Material;
import com.sweetlab.sweetride.mesh.Mesh;
import com.sweetlab.sweetride.resource.TextureResource;
import com.sweetlab.sweetride.shader.Attribute;
import com.sweetlab.sweetride.shader.ShaderProgram;

import java.util.ArrayList;
import java.util.List;

/**
 * Collection of various ways of drawing.
 */
public class DrawTestUtil {
    /**
     * Draw array with a shader program and vertex buffers in a non-interleaved way.
     *
     * @param context Backend context.
     * @param program Shader program.
     * @param buffers Vertex buffers.
     */
    public static void drawArraySeparateBuffers(BackendContext context, ShaderProgram program, VertexBuffer... buffers) {
        int vertexCount = buffers[0].getAttributePointer(0).getVertexCount();

        for (VertexBuffer buffer : buffers) {
            if (vertexCount != buffer.getAttributePointer(0).getVertexCount()) {
                throw new RuntimeException("Trying to drawArraySeparateBuffers using vertex buffers of different sizes");
            }
            context.getArrayTarget().enableAttribute(program, buffer);
        }

        context.getState().useProgram(program);
        context.getArrayTarget().draw(GLES20.GL_TRIANGLES, 0, vertexCount);

        for (VertexBuffer buffer : buffers) {
            Attribute attribute = program.getAttribute(buffer.getAttributePointer(0).getName());
            if (attribute != null) {
                context.getArrayTarget().disableAttribute(attribute);
            }
        }
    }

    /**
     * Draw array with a shader program and a interleaved vertex buffer.
     *
     * @param context      Backend context.
     * @param program      Shader program.
     * @param vertexBuffer Interleaved vertex buffer.
     */
    public static void drawArrayInterleavedBuffer(BackendContext context, ShaderProgram program, InterleavedVertexBuffer vertexBuffer) {
        context.getArrayTarget().enableAttribute(program, vertexBuffer);

        context.getState().useProgram(program);
        int vertexCount = vertexBuffer.getAttributePointer(0).getVertexCount();
        context.getArrayTarget().draw(GLES20.GL_TRIANGLES, 0, vertexCount);

        context.getArrayTarget().disableAttribute(program, vertexBuffer);
    }

    /**
     * Draw elements with a shader program and vertex buffers in a non-interleaved way.
     *
     * @param context Backend context.
     * @param program Shader program.
     * @param buffers Vertex buffers.
     */
    public static void drawElementsSeparateBuffers(BackendContext context, ShaderProgram program, IndicesBuffer indicesBuffer, VertexBuffer... buffers) {
        int vertexCount = buffers[0].getAttributePointer(0).getVertexCount();
        for (VertexBuffer buffer : buffers) {
            if (vertexCount != buffer.getAttributePointer(0).getVertexCount()) {
                throw new RuntimeException("Trying to drawElementsSeparateBuffers using vertex buffers of different sizes");
            }
            Attribute attribute = program.getAttribute(buffer.getAttributePointer(0).getName());
            if (attribute != null) {
                context.getArrayTarget().enableAttribute(attribute, buffer, buffer.getAttributePointer(0));
            }
        }

        context.getState().useProgram(program);

        context.getElementTarget().enableElements(indicesBuffer);
        context.getElementTarget().draw(GLES20.GL_TRIANGLES, 0, indicesBuffer.getIndicesCount());
        context.getElementTarget().disableElements();

        for (VertexBuffer buffer : buffers) {
            Attribute attribute = program.getAttribute(buffer.getAttributePointer(0).getName());
            if (attribute != null) {
                context.getArrayTarget().disableAttribute(attribute);
            }
        }
    }

    /**
     * Draw with a shader program and a interleaved vertex buffer.
     *
     * @param context      Backend context.
     * @param program      Shader program.
     * @param vertexBuffer Interleaved vertex buffer.
     * @param texture      Texture.
     */
    public static void drawArrayInterleavedBufferWithTexture(BackendContext context, ShaderProgram program, InterleavedVertexBuffer vertexBuffer, TextureResource texture) {
        int pointerCount = vertexBuffer.getAttributePointerCount();

        for (int i = 0; i < pointerCount; i++) {
            AttributePointer pointer = vertexBuffer.getAttributePointer(i);
            Attribute attribute = program.getAttribute(pointer.getName());
            if (attribute != null) {
                context.getArrayTarget().enableAttribute(attribute, vertexBuffer, pointer);
            }
        }

        context.getState().useProgram(program);

        TextureUnit textureUnit = context.getTextureUnitManager().takeTextureUnit();
        textureUnit.getTexture2DTarget().enable(program, texture);

        int vertexCount = vertexBuffer.getAttributePointer(0).getVertexCount();
        context.getArrayTarget().draw(GLES20.GL_TRIANGLE_STRIP, 0, vertexCount);

        textureUnit.getTexture2DTarget().disable(texture);
        context.getTextureUnitManager().returnTextureUnit(textureUnit);


        for (int i = 0; i < pointerCount; i++) {
            AttributePointer pointer = vertexBuffer.getAttributePointer(i);
            Attribute attribute = program.getAttribute(pointer.getName());
            if (attribute != null) {
                context.getArrayTarget().disableAttribute(attribute);
            }
        }
    }

    /**
     * Draw elements with a shader program, interleaved vertex buffer and dual textures.
     *
     * @param context      Backend context.
     * @param program      Shader program.
     * @param vertexBuffer Interleaved vertex buffer.
     * @param texture      Texture.
     * @param textureChess Chess texture.
     */
    public static void drawElementsInterleavedBufferWithDualTextures(BackendContext context, ShaderProgram program, IndicesBuffer ib, InterleavedVertexBuffer vertexBuffer, TextureResource texture, TextureResource textureChess) {
        context.getArrayTarget().enableAttribute(program, vertexBuffer);

        context.getState().useProgram(program);

        TextureUnit textureUnit1 = context.getTextureUnitManager().takeTextureUnit();
        textureUnit1.getTexture2DTarget().enable(program, texture);

        TextureUnit textureUnit2 = context.getTextureUnitManager().takeTextureUnit();
        textureUnit2.getTexture2DTarget().enable(program, textureChess);

        context.getElementTarget().enableElements(ib);
        context.getElementTarget().draw(GLES20.GL_TRIANGLE_STRIP, 0, ib.getIndicesCount());
        context.getElementTarget().disableElements();

        textureUnit2.getTexture2DTarget().disable(textureChess);
        textureUnit1.getTexture2DTarget().disable(texture);

        context.getTextureUnitManager().returnTextureUnit(textureUnit2);
        context.getTextureUnitManager().returnTextureUnit(textureUnit1);

        context.getArrayTarget().disableAttribute(program, vertexBuffer);
    }

    /**
     * Draw using material and mesh. Assumes that material and mesh is created and loaded.
     *
     * @param context  Backend context.
     * @param material Material.
     * @param mesh     Mesh.
     */
    public static void drawUsingMaterialAndMesh(BackendContext context, Material material, Mesh mesh) {
        final ShaderProgram program = material.getShaderProgram();
        final int vbCount = mesh.getVertexBufferCount();
        if (program != null && vbCount != 0) {

            for (int i = 0; i < vbCount; i++) {
                context.getArrayTarget().enableAttribute(program, mesh.getVertexBuffer(i));
            }

            context.getState().useProgram(program);

            List<TextureUnit> takenUnits = new ArrayList<>();
            int textureCount = material.getTextureCount();
            for (int i = 0; i < textureCount; i++) {
                TextureUnit textureUnit = context.getTextureUnitManager().takeTextureUnit();
                takenUnits.add(textureUnit);
                textureUnit.getTexture2DTarget().enable(program, material.getTexture(i));
            }

            final IndicesBuffer ib = mesh.getIndicesBuffer();
            if (ib == null) {
                context.getArrayTarget().draw(mesh.getMode().getGlMode(), 0, mesh.getVertexCount());
            } else {
                context.getElementTarget().enableElements(ib);
                context.getElementTarget().draw(mesh.getMode().getGlMode(), 0, ib.getIndicesCount());
                context.getElementTarget().disableElements();
            }

            for (int i = (textureCount - 1); i > -1; i--) {
                TextureUnit textureUnit = takenUnits.get(i);
                textureUnit.getTexture2DTarget().disable(material.getTexture(i));
                context.getTextureUnitManager().returnTextureUnit(textureUnit);
            }

            for (int i = 0; i < vbCount; i++) {
                context.getArrayTarget().disableAttribute(program, mesh.getVertexBuffer(i));
            }
        }
    }
}
