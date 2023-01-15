package dhyces.contextually.services;

import com.mojang.blaze3d.platform.InputConstants;
import dhyces.contextually.mixins.client.KeyMappingAccessor;
import net.minecraft.client.KeyMapping;

public class QuiltPlatformHelper implements IPlatformHelper {

    @Override
    public InputConstants.Key getKey(KeyMapping keyMapping) {
        return ((KeyMappingAccessor)keyMapping).getKey();
    }
}
