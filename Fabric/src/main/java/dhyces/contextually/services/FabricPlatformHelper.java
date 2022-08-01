package dhyces.contextually.services;

import com.mojang.blaze3d.platform.InputConstants;
import dhyces.contextually.mixins.client.IKeyMappingMixin;
import net.minecraft.client.KeyMapping;

public class FabricPlatformHelper implements IPlatformHelper {
    @Override
    public InputConstants.Key getKey(KeyMapping keyMapping) {
        return ((IKeyMappingMixin)keyMapping).getKey();
    }
}
