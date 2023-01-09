package dhyces.contextually.client.core.icons.objects;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import dhyces.contextually.client.core.icons.IIcon;
import dhyces.contextually.client.core.icons.IIconType;
import net.minecraft.client.gui.Gui;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.List;

public record AnimatedIcon(List<Pair<IIcon, Integer>> icons, int maxTicks) implements IIcon {
    public static final Codec<AnimatedIcon> CODEC = Codec.pair(
            IIcon.CODEC.fieldOf("icon").codec(),
            Codec.INT.fieldOf("ticks").codec()
    ).listOf().fieldOf("animation").xmap(AnimatedIcon::new, AnimatedIcon::icons).codec();

    public AnimatedIcon(@NotNull List<Pair<IIcon, Integer>> icons) {
        this(icons.stream().sorted(Comparator.comparingInt(Pair::getSecond)).toList(), icons.stream().mapToInt(Pair::getSecond).sum());
    }

    @Override
    public void render(Gui gui, PoseStack poseStack, float partialTicks, int blitOffset, int x, int y, int width, int height) {
        int ticks = 0;
        var fragmentedTicks = gui.getGuiTicks() % maxTicks;
        for (Pair<IIcon, Integer> pair : icons) {
            ticks += pair.getSecond();
            if (fragmentedTicks < ticks) {
                pair.getFirst().render(gui, poseStack, partialTicks, blitOffset, x, y, width, height);
                break;
            }
        }
    }

    @Override
    public IIconType<?> getType() {
        return IIconType.ANIMATED;
    }
}
