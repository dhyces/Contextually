package dhyces.contextually.client.contexts.icons;

import com.mojang.blaze3d.vertex.PoseStack;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.services.Services;
import dhyces.contextually.util.IntPair;
import net.minecraft.client.gui.Gui;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class AnimatedIcon implements IIcon {

    final List<IntPair<IIcon>> icons;

    float index;

    public AnimatedIcon(@NotNull List<IntPair<IIcon>> icons) {
        this.icons = icons;
    }

    public List<IntPair<IIcon>> getIcons() {
        return icons;
    }

    @Override
    public void render(Gui gui, PoseStack poseStack, float partialTicks, int blitOffset, int x, int y, int width, int height) {
        tick(gui, partialTicks);
        var animatedObject = icons.get((int)index);
        animatedObject.object().render(gui, poseStack, partialTicks, blitOffset, x, y, width, height);
        // increment tickPos
        // check if it's equal to the current object's tickMax or TICK_MAX
        //   true: reset tickPos to 0 and increment/reset index
        //   false: do nothing

    }

    private void tick(Gui gui, float partialTicks) {
        var progress = Mth.lerp(partialTicks, (int)index, ((int)index)+1);
        if (progress < index && gui.getGuiTicks() % icons.get((int)index).value() == 0) {
            index = ((int)index+1) % icons.size();
        } else {
            index = progress;
        }
    }

    @Override
    public ResourceLocation getId() {
        return ContextuallyCommon.id("animated");
    }
}
