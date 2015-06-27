package com.sweetlab.sweetride.Util;

import com.sweetlab.sweetride.node.Node;
import com.sweetlab.sweetride.node.RenderSettings;
import com.sweetlab.sweetride.node.rendersettings.ClearBit;

/**
 * Some commonly used render settings nodes used in test.
 */
public class RenderSettingsUtil {

    /**
     * Create a node with render settings that clears the screen with grey color, sets the view
     * port to provided width and height.
     *
     * @param width  Viewport width.
     * @param height Viewport height.
     * @return A node with render settings defined.
     */
    public static Node getDefaultGrey(int width, int height) {
        Node root = new Node();
        RenderSettings settings = root.getRenderSettings();
        settings.setViewPort(0, 0, width, height);
        settings.setClearColor(new float[]{0.5f, 0.5f, 0.5f, 1.0f});
        settings.setClear(0, ClearBit.COLOR_BUFFER_BIT);
        return root;
    }
}
