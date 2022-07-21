package dhyces.contextually.client.contexts.objects;

import com.google.gson.JsonObject;
import dhyces.contextually.client.contexts.conditions.Condition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class BlockKeyContext extends AbstractKeyContext<BlockState> {

    public BlockKeyContext(@NotNull ResourceLocation id, @NotNull Set<ResourceLocation> keys, @NotNull Set<Condition> conditions) {
        super(id, keys, conditions);
    }

    public static class Serializer implements ISerializer<BlockState, BlockKeyContext> {

        @Override
        public BlockKeyContext deserialize(ResourceLocation id, JsonObject json) {
            return null;
        }

        @Override
        public JsonObject serialize(ResourceLocation id, BlockKeyContext context) {
            return null;
        }
    }
}
