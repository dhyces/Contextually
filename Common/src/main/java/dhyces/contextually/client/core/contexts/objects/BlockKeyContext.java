package dhyces.contextually.client.core.contexts.objects;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dhyces.contextually.client.core.conditions.IContextCondition;
import dhyces.contextually.client.core.contexts.IKeyContextType;
import dhyces.contextually.client.core.icons.IIcon;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.Set;
import java.util.stream.Collectors;

public class BlockKeyContext extends AbstractKeyContext<BlockState, BlockState> {
    public static final Codec<BlockKeyContext> CODEC = RecordCodecBuilder.create(instance ->
            fillBaseParts(instance).and(
                    Codec.either(
                            BuiltInRegistries.BLOCK.byNameCodec(),
                            BlockState.CODEC
                    ).listOf().fieldOf("targets").xmap(
                            targets -> targets.stream().map(either -> either.left().isPresent() ? either.left().get().defaultBlockState() : either.right().get()).collect(Collectors.toSet()),
                            blockStates -> blockStates.stream().<Either<Block, BlockState>>map(blockState -> blockState == blockState.getBlock().defaultBlockState() ? Either.left(blockState.getBlock()) : Either.right(blockState)).toList()
                    ).forGetter(BlockKeyContext::getTargets)
            ).apply(instance, BlockKeyContext::new)
    );

    public BlockKeyContext(@Nonnull Set<IIcon> icons, @Nonnull Set<IContextCondition> conditions, @Nonnull Set<BlockState> targetedStates) {
        super(icons, targetedStates, conditions);
    }

    @Override
    public @Nonnull IKeyContextType<?> getType() {
        return IKeyContextType.BLOCK;
    }
}
