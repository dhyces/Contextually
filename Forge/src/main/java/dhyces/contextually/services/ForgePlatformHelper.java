package dhyces.contextually.services;

import com.mojang.blaze3d.platform.InputConstants;
import dhyces.contextually.client.gui.ContextGuiOverlay;
import net.minecraft.client.KeyMapping;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public InputConstants.Key getKey(KeyMapping keyMapping) {
        return keyMapping.getKey();
    }

    @Override
    public int getContextRendererTicks() {
        return ContextGuiOverlay.getTicks();
    }
}
