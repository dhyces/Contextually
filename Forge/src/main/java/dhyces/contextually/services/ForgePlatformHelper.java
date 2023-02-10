package dhyces.contextually.services;

import com.mojang.blaze3d.platform.InputConstants;
import dhyces.contextually.client.textures.KeyMappingTextureManager;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.Gui;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;

public class ForgePlatformHelper implements IPlatformHelper {

    @Override
    public InputConstants.Key getKey(KeyMapping keyMapping) {
        return keyMapping.getKey();
    }

    @Override
    public double getReachDistance(Player player) {
        return player.getReachDistance();
    }

    @Override
    public void setupRenderState(Gui gui) {
        ((ForgeGui)gui).setupOverlayRenderState(true, false, KeyMappingTextureManager.KEYS);
    }
}
