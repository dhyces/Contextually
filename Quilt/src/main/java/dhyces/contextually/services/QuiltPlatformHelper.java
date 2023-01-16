package dhyces.contextually.services;

import com.mojang.blaze3d.platform.InputConstants;
import dhyces.contextually.mixins.client.KeyMappingAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.player.Player;
import org.quiltmc.loader.api.QuiltLoader;

public class QuiltPlatformHelper implements IPlatformHelper {

    @Override
    public InputConstants.Key getKey(KeyMapping keyMapping) {
        return ((KeyMappingAccessor)keyMapping).getKey();
    }

    @Override
    public double getReachDistance(Player player) {
        return player.getAbilities().instabuild ? 5 : 4.5;
    }
}
