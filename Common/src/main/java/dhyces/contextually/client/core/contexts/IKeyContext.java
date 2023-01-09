package dhyces.contextually.client.core.contexts;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.serialization.Codec;
import dhyces.contextually.client.core.KeyContextLoader;
import dhyces.contextually.client.core.conditions.IContextCondition;
import dhyces.contextually.client.core.icons.IIcon;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public interface IKeyContext<K, T> {
    Codec<IKeyContext<?, ?>> CODEC = KeyContextLoader.CONTEXT_MAP_CODEC.dispatch("context_type", IKeyContext::getType, IKeyContextType::getCodec);

    Component PLUS = Component.literal("+");
    int SMALL_PADDING = 4;
    int PADDING = 8;

    void renderIcons(T contextObject, Gui gui, PoseStack poseStack, float partialTicks, int pX, int pY, int width, int height);
    void renderText(T contextObject, Gui gui, PoseStack poseStack, float partialTicks, int pX, int pY, int width, int height);
    int width(Font font);
    boolean testConditions(@Nullable Object contextObject, @Nullable HitResult hitResult, @NotNull ClientLevel level, @NotNull AbstractClientPlayer player);

    void setIdIfNull(@NotNull ResourceLocation resourceLocation);
    @Nullable ResourceLocation getID();
    @NotNull Set<IIcon> getIcons();
    @NotNull Set<IContextCondition> getConditions();
    @NotNull Set<K> getTargets();
    @NotNull IKeyContextType<?> getType();
}
