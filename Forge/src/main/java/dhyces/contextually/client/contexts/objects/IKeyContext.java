package dhyces.contextually.client.contexts.objects;

import com.mojang.blaze3d.vertex.PoseStack;
import dhyces.contextually.client.contexts.conditions.IConditionPredicate;
import dhyces.contextually.client.contexts.keys.Key;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public interface IKeyContext<T> {

    Component PLUS = Component.literal("+");
    int SMALL_PADDING = 4;
    int PADDING = 8;

    void renderIcons(T contextObject, ForgeGui gui, PoseStack poseStack, float partialTicks, int pX, int pY, int width, int height);
    void renderText(T contextObject, ForgeGui gui, PoseStack poseStack, float partialTicks, int pX, int pY, int width, int height);
    int width(Font font);
    void invalidateCache();

    @NotNull ResourceLocation getID();
    @NotNull Set<Key> getKeys();
    @NotNull Set<IConditionPredicate> getConditions();
}
