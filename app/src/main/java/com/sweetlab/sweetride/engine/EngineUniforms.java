package com.sweetlab.sweetride.engine;

import com.sweetlab.sweetride.engine.uniform.EngineUniform;
import com.sweetlab.sweetride.engine.uniform.EngineUniformType;
import com.sweetlab.sweetride.engine.uniform.ModelMatrix;
import com.sweetlab.sweetride.engine.uniform.ProjectionMatrix;
import com.sweetlab.sweetride.engine.uniform.ViewMatrix;
import com.sweetlab.sweetride.engine.uniform.WorldMatrix;
import com.sweetlab.sweetride.engine.uniform.WorldViewMatrix;
import com.sweetlab.sweetride.engine.uniform.WorldViewProjectionMatrix;

import java.util.ArrayList;
import java.util.List;

/**
 * Supported engine uniforms.
 */
public class EngineUniforms {

    /**
     * Create a new list of all available engine uniforms.
     *
     * @return
     */
    public List<EngineUniform> createList() {
        List<EngineUniform> list = new ArrayList<>();
        list.add(new ModelMatrix());
        list.add(new WorldMatrix());
        list.add(new ViewMatrix());
        list.add(new ProjectionMatrix());
        list.add(new WorldViewMatrix());
        list.add(new WorldViewProjectionMatrix());

        if (EngineUniformType.values().length != list.size()) {
            throw new RuntimeException("Didn't create all engine uniforms");
        }

        return list;
    }
}
