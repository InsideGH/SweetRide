package com.sweetlab.sweetride.uniform;


import com.sweetlab.sweetride.action.GlobalActionId;
import com.sweetlab.sweetride.action.NoHandleNotifier;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.shader.ShaderProgram;

/**
 * A uniform that contains a some value primitive of any type.
 */
public abstract class CustomUniform extends NoHandleNotifier<GlobalActionId> {
    /**
     * Write the value to shader program uniform.
     *
     * @param context Backend context.
     * @param program Shader program.
     */
    public abstract void writeProgramUniform(BackendContext context, ShaderProgram program);
}
