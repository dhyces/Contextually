package dhyces.contextually.client.core.icons.objects;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import dhyces.contextually.client.core.icons.IIcon;
import dhyces.contextually.client.core.icons.IIconType;
import dhyces.contextually.client.textures.KeyMappingTextureManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;

public record ItemIcon(Item item) implements IIcon {
    public static final Codec<ItemIcon> CODEC = BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").xmap(ItemIcon::new, ItemIcon::item).codec();

    @Override
    public void render(Gui gui, PoseStack poseStack, float partialTicks, int blitOffset, int x, int y, int width, int height) {
        Minecraft.getInstance().getItemRenderer().renderGuiItem(item.getDefaultInstance(), x, y);
        RenderSystem.setShaderTexture(0, KeyMappingTextureManager.KEYS);
    }

    @Override
    public IIconType<?> getType() {
        return IIconType.ITEM;
    }
}
