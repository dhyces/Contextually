package dhyces.contextually.client.contexts.objects;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.datafixers.util.Pair;
import dhyces.contextually.client.contexts.conditions.IConditionPredicate;
import dhyces.contextually.client.contexts.icons.IIcon;
import dhyces.contextually.client.contexts.objects.serializers.IContextSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Objects;
import java.util.Set;

public class ItemKeyContext extends AbstractKeyContext<ItemStack> {

    public ItemKeyContext(@NotNull ResourceLocation id, @NotNull Set<IIcon> icons, @NotNull Set<IConditionPredicate> conditions) {
        super(id, icons, conditions);
    }

    public static class Serializer implements IContextSerializer<Item, ItemKeyContext> {

        @Override
        public Pair<Collection<Item>, ItemKeyContext> deserialize(@NotNull ResourceLocation id, @NotNull JsonObject json) {
            var targetJson = json.get("target_item");
            ImmutableSet.Builder<Item> builder = null;
            if (targetJson.isJsonPrimitive()) {
                builder = ImmutableSet.builder();
                if (!isDefault(targetJson)) {
                    var item = getItem(targetJson);
                    builder.add(item);
                }
            } else if (targetJson.isJsonArray()) {
                builder = ImmutableSet.builder();
                for (JsonElement e : targetJson.getAsJsonArray()) {
                    Objects.requireNonNull(e, "Element null in array. Likely unterminated array.");
                    builder.add(getItem(e));
                }
            }

            if (builder == null) {
                throw new JsonParseException("Key \"target_item\" not present.");
            }

            var icons = readIcons(json);
            var conditions = readConditions(json);
            return Pair.of(builder.build(), new ItemKeyContext(id, icons, conditions));
        }

        private Item getItem(JsonElement idElement) {
            var key = ResourceLocation.of(idElement.getAsString(), ':');
            var item = ForgeRegistries.ITEMS.getValue(key);
            if (!ForgeRegistries.ITEMS.getKey(item).equals(key)) {
                throw new NullPointerException("Item for given key: " + key + " not found.");
            }
            return item;
        }

        @Override
        public JsonObject serialize(@NotNull ResourceLocation id, @NotNull Pair<Collection<Item>, ItemKeyContext> context) {
            return null;
        }
    }
}
