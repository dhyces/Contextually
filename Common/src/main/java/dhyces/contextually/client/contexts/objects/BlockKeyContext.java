package dhyces.contextually.client.contexts.objects;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import dhyces.contextually.client.contexts.conditions.IConditionPredicate;
import dhyces.contextually.client.contexts.icons.IIcon;
import dhyces.contextually.client.contexts.objects.serializers.IContextSerializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class BlockKeyContext extends AbstractKeyContext<BlockState> {

    public BlockKeyContext(@NotNull ResourceLocation id, @NotNull Set<IIcon> icons, @NotNull Set<IConditionPredicate> conditions) {
        super(id, icons, conditions);
    }

    public static class Serializer implements IContextSerializer<BlockState, BlockKeyContext> {

        @Override
        public Pair<Collection<BlockState>, BlockKeyContext> deserialize(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            var targetJson = json.get("target_block");
            ImmutableSet.Builder<BlockState> builder = null;
            if (targetJson.isJsonPrimitive()) {
                builder = ImmutableSet.builder();
                if (!targetJson.getAsString().equals("*")) {
                    var block = getBlock(targetJson);
                    builder.add(block.defaultBlockState());
                }
            } else if (targetJson.isJsonObject()) {
                builder = ImmutableSet.builder();
                builder.addAll(getBlockStates(targetJson.getAsJsonObject()));
            } else if (targetJson.isJsonArray()) {
                builder = ImmutableSet.builder();

                for (JsonElement e : targetJson.getAsJsonArray()) {
                    Objects.requireNonNull(e, "Element null in array. Likely unterminated array.");
                    if (e instanceof JsonObject o) {
                        builder.addAll(getBlockStates(o));
                    } else {
                        builder.add(getBlock(e).defaultBlockState());
                    }
                }
            }

            if (builder == null) {
                throw new IllegalStateException("Key \"target_block\" not present.");
            }

            var keys = readIcons(json);
            var conditions = readConditions(json);
            return Pair.of(builder.build(), new BlockKeyContext(id, keys, conditions));
        }

        private Block getBlock(JsonElement idElement) {
            var key = ResourceLocation.of(idElement.getAsString(), ':');
            var block = Registry.BLOCK.get(key);
            if (!Registry.BLOCK.getKey(block).equals(key)) {
                throw new NullPointerException("Block for given key: " + key + " not found.");
            }
            return block;
        }

        private Collection<BlockState> getBlockStates(JsonObject stateObject) {
            JsonElement blockJson = null;
            Collection<Pair<String, String>> propertyList = new LinkedList<>();
            for (String key : stateObject.keySet()) {
                if (key.equals("id")) {
                    blockJson = stateObject.get(key);
                    continue;
                }
                propertyList.add(Pair.of(key, stateObject.get(key).getAsString()));
            }
            if (blockJson == null) {
                throw new JsonParseException("Block id must not be null.");
            }
            return statesWithProperties(getBlock(blockJson), propertyList);
        }

        private Collection<BlockState> statesWithProperties(Block block, Collection<Pair<String, String>> properties) {
            var stateDefinition = block.getStateDefinition();
            var otherStates = Lists.newArrayList(stateDefinition.getPossibleStates());
            var propertyStates = properties.stream().map(strProperty -> {
                var property = stateDefinition.getProperty(strProperty.getFirst());
                if (property == null) {
                    throw new IllegalStateException("Block property \"" + strProperty.getFirst() + "\" does not exist on block: " + block);
                }
                var propertyValue = property.getValue(strProperty.getSecond());
                if (propertyValue.isEmpty()) {
                    throw new IllegalStateException("Block property value \"" + strProperty.getSecond() + "\" does not exist on property: " + property);
                }
                return Pair.of(property, propertyValue.get());
            }).toList();
            return otherStates.stream().filter(c -> propertyStates.stream().anyMatch(p -> c.getValue(p.getFirst()).compareTo(cast(p.getSecond())) == 0)).toList();
        }

        private <X> X cast(Object o) {
            return (X)o;
        }

        private <T extends Comparable<T>> BlockState withValue(BlockState state, Property<T> property, Object value) {
            return state.setValue(property, (T)value);
        }

        @Override
        public JsonObject serialize(@NotNull ResourceLocation id, @NotNull Pair<Collection<BlockState>, BlockKeyContext> context) {
            return null;
        }
    }
}