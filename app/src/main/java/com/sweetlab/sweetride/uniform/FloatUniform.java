package com.sweetlab.sweetride.uniform;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionId;
import com.sweetlab.sweetride.action.HandleThread;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.shader.ShaderProgram;

/**
 * A general uniform that can be used with multiple programs.
 */
public class FloatUniform extends CustomUniform {
    /**
     * Max uniform length, corresponds to a 4x4 matrix.
     */
    private static final int MAX = 16;

    /**
     * Action when uniform has changed.
     */
    private final Action mUniformChanged = new Action(this, ActionId.CUSTOM_UNIFORM_UPDATE, HandleThread.MAIN);

    /**
     * The data storage.
     */
    private final float[] mData = new float[MAX];

    /**
     * The data storage for writing program uniform in backend.
     */
    private final float[] mDataGL = new float[MAX];

    /**
     * Uniform name in program.
     */
    private final String mName;

    /**
     * Length of data is held to avoid creating new array on each update.
     */
    private int mLength;

    /**
     * Constructor.
     *
     * @param name Uniform name in program.
     */
    public FloatUniform(String name) {
        mName = name;
    }

    @Override
    public void handleAction(Action action) {
        if (action.getType().equals(ActionId.CUSTOM_UNIFORM_UPDATE)) {
            System.arraycopy(mData, 0, mDataGL, 0, mLength);
        }
    }

    @Override
    public void writeProgramUniform(BackendContext context, ShaderProgram program) {
        context.getUniformWriter().writeFloat(program, mName, mDataGL);
    }

    /**
     * Set value to uniform.
     *
     * @param data The data.
     */
    public void set(float[] data) {
        if (data.length > MAX) {
            throw new RuntimeException("Only supporting " + MAX + " length uniforms");
        }
        mLength = data.length;
        System.arraycopy(data, 0, mData, 0, mLength);
        addAction(mUniformChanged);
    }
}
