package dhyces.contextually.client.core.contexts.objects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dhyces.contextually.client.core.conditions.IContextCondition;
import dhyces.contextually.client.core.contexts.IKeyContextType;
import dhyces.contextually.client.core.icons.IIcon;
import net.minecraft.world.level.material.FluidState;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;

public class FluidKeyContext extends AbstractKeyContext<FluidState, FluidState> {
    public static final Codec<FluidKeyContext> CODEC = RecordCodecBuilder.create(instance ->
            fillBaseParts(instance).and(// TODO: switch to an either for BuiltinRegistries.FLUID
                    FluidState.CODEC.listOf().fieldOf("targets").xmap(Set::copyOf, List::copyOf).forGetter(FluidKeyContext::getTargets)
            ).apply(instance, FluidKeyContext::new)
    );

    public FluidKeyContext(@NotNull Set<IIcon> icons, @NotNull Set<IContextCondition> conditions, @NotNull Set<FluidState> targets) {
        super(icons, targets, conditions);
    }


    @NotNull
    @Override
    public IKeyContextType<?> getType() {
        return IKeyContextType.FLUID;
    }
}
