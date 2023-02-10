package dhyces.contextually.services;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public interface IPlatformHelper {

    InputConstants.Key getKey(KeyMapping keyMapping);

    double getReachDistance(Player player);

    void setupRenderState(Gui gui);
}
