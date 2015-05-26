package com.sweetlab.sweetride.context;

import android.opengl.GLES20;

import com.sweetlab.sweetride.DebugOptions;
import com.sweetlab.sweetride.attributedata.AttributeData;
import com.sweetlab.sweetride.shader.Attribute;

import java.nio.Buffer;

/**
 * The array target.
 */
public class ArrayTarget {
    /**
     * The array target.
     */
    private static final int TARGET = GLES20.GL_ARRAY_BUFFER;

    /**
     * The array target buffer binding.
     */
    private static final int TARGET_BUFFER_BINDING = GLES20.GL_ARRAY_BUFFER_BINDING;

    /**
     * Buffer to read GL information.
     */
    private int[] mReadBuffer = new int[4];

    /**
     * The backend context.
     */
    private final BackendContext mContext;

    /**
     * Constructor.
     *
     * @param backendContext The backend context.
     */
    public ArrayTarget(BackendContext backendContext) {
        mContext = backendContext;
    }

    /**
     * Load attribute data to gpu.
     *
     * @param attributeData Attribute data.
     */
    public void load(AttributeData attributeData) {
        if (DebugOptions.DEBUG_ARRAY_TARGET) {
            if (!attributeData.isCreated()) {
                throw new RuntimeException("Trying to load attribute data that has not been created");
            }
        }
        final int bufferId = attributeData.getBufferId();
        if (!isBufferBound(bufferId)) {
            GLES20.glBindBuffer(TARGET, bufferId);
        }

        final Buffer data = attributeData.getData();
        final int totalByteCount = attributeData.getTotalByteCount();
        final int bufferUsage = attributeData.getBufferUsage();
        GLES20.glBufferData(TARGET, totalByteCount, data, bufferUsage);
    }

    /**
     * Enable a shader program attribute to use provided attribute data.
     *
     * @param attribute     The attribute in the shader program.
     * @param attributeData The attribute data.
     */
    public void enableAttribute(Attribute attribute, AttributeData attributeData) {
        /**
         * Check if buffer needs to be bound.
         */
        final int bufferId = attributeData.getBufferId();
        if (!isBufferBound(bufferId)) {
            GLES20.glBindBuffer(TARGET, bufferId);
        }

        /**
         * Check if attribute need to be enabled.
         */
        final int attributeLocation = attribute.getLocation();
        if (!isAttributeEnabled(attributeLocation)) {
            GLES20.glEnableVertexAttribArray(attributeLocation);
        }

        /**
         * Check if attribute has correct buffer bound. If not, reconfigure attribute.
         */
        if (getBoundAttributeBuffer(attributeLocation) != bufferId) {
            final int dataVertexSize = attributeData.getVertexSize();
            if (dataVertexSize > attribute.getVertexSize()) {
                throw new RuntimeException("Element count in data is larger than specified in shader program, data element count = " + dataVertexSize + " while shader attribute specifies " + attribute.getVertexSize());
            }

            /**
             * In case we have data with element count less than specified in the shader attribute we want to configure
             * with the correct vertex size (element count). Typically vertex data contains 3 instead of 4 component because
             * only 'xyz' is specified but 'w' is ignored/skipped. That is the reason for using dataVertexSize.
             */
            final int strideBytes = attributeData.getStrideBytes();
            final boolean shouldNormalize = attributeData.getShouldNormalize();
            final int offsetBytes = attributeData.getOffsetBytes();
            final int typeFamily = attribute.getTypeFamily();
            GLES20.glVertexAttribPointer(attributeLocation, dataVertexSize, typeFamily, shouldNormalize, strideBytes, offsetBytes);
        }
    }

    /**
     * Disable a shader program attribute.
     *
     * @param attribute The shader program attribute.
     */
    public void disableAttribute(Attribute attribute) {
        if (!isAnyBufferBound()) {
            throw new RuntimeException("Can't disable active attribute with not bound buffer object to target GL_ARRAY_BUFFER");
        }
        GLES20.glDisableVertexAttribArray(attribute.getLocation());
    }

    /**
     * Draw.
     *
     * @param mode       Drawing mode.
     * @param startIndex Start index.
     * @param count      Number of vertices.
     */
    public void draw(int mode, int startIndex, int count) {
        if (mContext.getState().readActiveProgram() <= ProgramLinker.INVALID_ID) {
            throw new RuntimeException("Invalid shader program while array draw is called");
        }
        if (!isAnyBufferBound()) {
            throw new RuntimeException("Can't draw without a bound buffer to GL_ARRAY_BUFFER");
        }
        GLES20.glDrawArrays(mode, startIndex, count);
    }

    /**
     * Get if target has any buffer bound.
     *
     * @return True if target has buffer bound.
     */
    public boolean isAnyBufferBound() {
        GLES20.glGetIntegerv(TARGET_BUFFER_BINDING, mReadBuffer, 0);
        return mReadBuffer[0] != 0;
    }

    /**
     * Get if buffer id is bound to target.
     *
     * @param id The buffer id.
     * @return True if bound.
     */
    private boolean isBufferBound(int id) {
        if (id > 0) {
            GLES20.glGetIntegerv(TARGET_BUFFER_BINDING, mReadBuffer, 0);
            return mReadBuffer[0] == id;
        }
        return false;
    }

    /**
     * Get if attribute is enabled.
     *
     * @param location Location of attribute.
     * @return True if enabled.
     */
    public boolean isAttributeEnabled(int location) {
        GLES20.glGetVertexAttribiv(location, GLES20.GL_VERTEX_ATTRIB_ARRAY_ENABLED, mReadBuffer, 0);
        return mReadBuffer[0] != 0;
    }

    /**
     * Get which buffer is bound to attribute.
     *
     * @param location Location of attribute.
     * @return The buffer bound to attribute.
     */
    public int getBoundAttributeBuffer(int location) {
        GLES20.glGetVertexAttribiv(location, GLES20.GL_VERTEX_ATTRIB_ARRAY_BUFFER_BINDING, mReadBuffer, 0);
        return mReadBuffer[0];
    }
}
