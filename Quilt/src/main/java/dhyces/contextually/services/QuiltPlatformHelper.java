package dhyces.contextually.services;

import com.mojang.blaze3d.platform.InputConstants;
import dhyces.contextually.mixins.client.KeyMappingAccessor;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.level.Level;

public class QuiltPlatformHelper implements IPlatformHelper {

    @Override
    public InputConstants.Key getKey(KeyMapping keyMapping) {
        return ((KeyMappingAccessor)keyMapping).getKey();
    }
}
