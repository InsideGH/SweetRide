package com.sweetlab.sweetride.shader;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.GlobalActionId;
import com.sweetlab.sweetride.action.ActionThread;
import com.sweetlab.sweetride.action.NoHandleNotifier;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.resource.Resource;

/**
 * Base shader class.
 */
public abstract class BaseShader extends NoHandleNotifier<GlobalActionId> implements Resource {
    /**
     * Shader source code.
     */
    protected final String mSource;

    /**
     * Constructor.
     *
     * @param source The shader source.
     */
    public BaseShader(String source) {
        mSource = source;
        addAction(new Action<>(this, GlobalActionId.SHADER_CREATE, ActionThread.GL));
    }

    @Override
    public boolean handleAction(BackendContext context, Action action) {
        if (action.getType().equals(GlobalActionId.SHADER_CREATE)) {
            create(context);
            return true;
        }
        return false;
    }

    /**
     * Get the shader source code.
     *
     * @return The source.
     */
    public String getSource() {
        return mSource;
    }
}
