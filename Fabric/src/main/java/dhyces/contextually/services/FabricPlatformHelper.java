package dhyces.contextually.services;

import com.jamieswhiteshirt.reachentityattributes.ReachEntityAttributes;
import com.mojang.blaze3d.platform.InputConstants;
import dhyces.contextually.mixins.client.KeyMappingAccessor;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Debug;

@Debug(export = true)
public class FabricPlatformHelper implements IPlatformHelper {
    @Override
    public InputConstants.Key getKey(KeyMapping keyMapping) {
        return ((KeyMappingAccessor)keyMapping).getKey();
    }

    @Override
    public double getReachDistance(Player player) {
        return ReachEntityAttributes.getReachDistance(player, 4.5);
    }
}
