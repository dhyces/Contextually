package dhyces.contextually.client.core.icons;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import dhyces.contextually.client.core.KeyContextLoader;
import net.minecraft.client.gui.Gui;

public interface IIcon {
    Codec<IIcon> CODEC = KeyContextLoader.ICON_MAP_CODEC.dispatch("icon_type", IIcon::getType, IIconType::getCodec);

    void render(Gui gui, PoseStack poseStack, float partialTicks, int blitOffset, int x, int y, int width, int height);

    IIconType<?> getType();
}
