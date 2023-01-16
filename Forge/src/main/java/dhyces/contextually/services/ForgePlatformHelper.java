package dhyces.contextually.services;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.world.entity.player.Player;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public InputConstants.Key getKey(KeyMapping keyMapping) {
        return keyMapping.getKey();
    }

    @Override
    public double getReachDistance(Player player) {
        return player.getReachDistance();
    }
}
