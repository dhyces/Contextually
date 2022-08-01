package dhyces.contextually.client.contexts.icons;

import com.mojang.blaze3d.vertex.PoseStack;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.util.IntPair;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public record AnimatedIcon(List<IntPair<IIcon>> icons, int maxTicks) implements IIcon {

    public AnimatedIcon(@NotNull List<IntPair<IIcon>> icons) {
        this(icons.stream().sorted(Comparator.comparingInt(IntPair::value)).toList(), icons.stream().mapToInt(IntPair::value).sum());
    }

    @Override
    public void render(Gui gui, PoseStack poseStack, float partialTicks, int blitOffset, int x, int y, int width, int height) {
        int ticks = 0;
        var fragmentedTicks = gui.getGuiTicks() % maxTicks;
        for (IntPair<IIcon> pair : icons) {
            ticks += pair.value();
            if (fragmentedTicks < ticks) {
                pair.object().render(gui, poseStack, partialTicks, blitOffset, x, y, width, height);
                break;
            }
        }
    }

    @Override
    public ResourceLocation getId() {
        return ContextuallyCommon.id("animated");
    }
}
