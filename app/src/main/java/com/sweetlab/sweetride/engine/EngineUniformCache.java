package com.sweetlab.sweetride.engine;

import com.sweetlab.sweetride.shader.ShaderProgram;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;

/**
 * Given a shader program, this engine uniform cache can be asked for a list of active
 * engine uniforms in a shader program.
 * If the shader program has not yet been created, a complete list of engine uniforms
 * is provided.
 * Once the shader program is created, the actual list of active engine uniforms is created
 * and placed in the cache for future queries.
 */
public class EngineUniformCache {
    /**
     * A cache of active engine uniforms with shader program as key.
     */
    private HashMap<ShaderProgram, List<EngineUniform>> mCache = new HashMap<>();

    /**
     * A list of all supported engine uniforms.
     */
    private final List<EngineUniform> mCompleteList = Collections.unmodifiableList(new ArrayList<>(EnumSet.allOf(EngineUniform.class)));

    /**
     * Get active engine uniforms in program.
     *
     * @param program Shader program.
     * @return The list of active engine uniforms.
     */
    public List<EngineUniform> getEngineUniforms(ShaderProgram program) {
        if (program.isCreated()) {
            List<EngineUniform> engineUniforms = mCache.get(program);
            if (engineUniforms == null) {
                engineUniforms = getActiveEngineUniforms(program);
                mCache.put(program, engineUniforms);
            }
            return engineUniforms;
        }
        return mCompleteList;
    }

    /**
     * Get active engine uniforms in the shader program.
     *
     * @param program Shader program.
     * @return List of active uniforms.
     */
    private List<EngineUniform> getActiveEngineUniforms(ShaderProgram program) {
        List<EngineUniform> engineUniforms = new ArrayList<>();
        for (EngineUniform u : mCompleteList) {
            if (program.getUniform(u.getName()) != null) {
                engineUniforms.add(u);
            }
        }
        return engineUniforms;
    }
}
