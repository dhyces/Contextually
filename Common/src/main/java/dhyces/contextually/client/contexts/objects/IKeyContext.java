package dhyces.contextually.client.contexts.objects;

import com.mojang.blaze3d.vertex.PoseStack;
import dhyces.contextually.client.contexts.conditions.IConditionPredicate;
import dhyces.contextually.client.contexts.icons.IIcon;
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

public interface IKeyContext<T> {

    Component PLUS = Component.literal("+");
    int SMALL_PADDING = 4;
    int PADDING = 8;

    void renderIcons(T contextObject, Gui gui, PoseStack poseStack, float partialTicks, int pX, int pY, int width, int height);
    void renderText(T contextObject, Gui gui, PoseStack poseStack, float partialTicks, int pX, int pY, int width, int height);
    int width(Font font);
    boolean testConditions(@Nullable Object contextObject, @Nullable HitResult hitResult, @NotNull ClientLevel level, @NotNull AbstractClientPlayer player);
    void invalidateCache();

    @NotNull ResourceLocation getID();
    @NotNull Set<IIcon> getIcons();
    @NotNull Set<IConditionPredicate> getConditions();
}
