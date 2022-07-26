package dhyces.contextually.client.contexts.icons;

import com.mojang.blaze3d.vertex.PoseStack;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.ContextuallyClient;
import dhyces.contextually.util.IntPair;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.function.IntSupplier;

public class AnimatedIcon implements IIcon {

    static final int TICK_MAX = 200;

    //TODO: im not a fan of storing a funny int for animated things
    static final IntSupplier RENDER_TIME = () -> ContextuallyClient.funnyInt;

    final List<IntPair<IIcon>> icons;

    int index = 0;

    public AnimatedIcon(@NotNull List<IntPair<IIcon>> icons) {
        this.icons = icons;
    }

    public List<IntPair<IIcon>> getIcons() {
        return icons;
    }

    @Override
    public void render(PoseStack poseStack, float partialTicks, int blitOffset, int x, int y, int width, int height) {
        var animatedObject = icons.get(index);
        animatedObject.object().render(poseStack, partialTicks, blitOffset, x, y, width, height);
        var p = RENDER_TIME.getAsInt();
        if (RENDER_TIME.getAsInt() % animatedObject.value() == 0) {
            index = (index+1) % icons.size();
        }
        // increment tickPos
        // check if it's equal to the current object's tickMax or TICK_MAX
        //   true: reset tickPos to 0 and increment/reset index
        //   false: do nothing

    }

    @Override
    public ResourceLocation getId() {
        return ContextuallyCommon.modloc("animated");
    }
}
