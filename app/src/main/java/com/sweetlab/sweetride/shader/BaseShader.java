package com.sweetlab.sweetride.shader;

import com.sweetlab.sweetride.action.Action;
import com.sweetlab.sweetride.action.ActionId;
import com.sweetlab.sweetride.action.HandleThread;
import com.sweetlab.sweetride.action.NoHandleNotifier;
import com.sweetlab.sweetride.context.BackendContext;
import com.sweetlab.sweetride.resource.Resource;

/**
 * Base shader class.
 */
public abstract class BaseShader extends NoHandleNotifier implements Resource {
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
        addAction(new Action(this, ActionId.SHADER_CREATE, HandleThread.GL));
    }

    @Override
    public void handleAction(BackendContext context, Action action) {
        if (action.getType().equals(ActionId.SHADER_CREATE)) {
            create(context);
            return;
        }
        throw new RuntimeException("wtf");
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
