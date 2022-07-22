package dhyces.contextually.client.contexts;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.contexts.conditions.Condition;
import dhyces.contextually.client.contexts.conditions.serializers.ConditionSerializers;
import dhyces.contextually.client.contexts.conditions.serializers.IConditionSerializer;
import dhyces.contextually.client.contexts.keys.Key;
import dhyces.contextually.client.contexts.keys.serializers.IKeySerializer;
import dhyces.contextually.client.contexts.keys.serializers.KeySerializers;
import dhyces.contextually.client.contexts.objects.BlockKeyContext;
import dhyces.contextually.client.contexts.objects.EntityKeyContext;
import dhyces.contextually.client.contexts.objects.GlobalKeyContext;
import dhyces.contextually.client.contexts.objects.IKeyContext;
import dhyces.contextually.client.contexts.objects.serializers.IContextSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

public class KeyContextLoader {

    private static final ImmutableMap<String, IKeySerializer> KEY_SERIALIZERS_MAP;
    private static final ImmutableSet<IConditionSerializer> CONDITION_SERIALIZERS;
    private static final ImmutableMap<ResourceLocation, IContextSerializer<?, ?>> CONTEXT_SERIALIZERS;
    private final ResourceManager resourceManager;

    final String fileExtension = ".json";

    public KeyContextLoader(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public List<Pair<? extends Collection<?>, ? extends IKeyContext<?>>> load() {
        List<Pair<? extends Collection<?>, ? extends IKeyContext<?>>> read = new LinkedList<>();
        for (Map.Entry<ResourceLocation, Resource> entry : resourceManager.listResources("contexts", resourceLocation -> resourceLocation.getPath().endsWith(".json")).entrySet()) {
            try(var reader = entry.getValue().openAsReader()) {
                var jsonElement = JsonParser.parseReader(reader);
                try {
                    var obj = jsonElement.getAsJsonObject();
                    var loader = obj.get("loader").getAsString();
                    var loaderKey = loader.contains(":") ? ResourceLocation.of(loader, ':') : ContextuallyCommon.modloc(loader);
                    var serializer = CONTEXT_SERIALIZERS.get(loaderKey);
                    try {
                        Objects.requireNonNull(serializer, "Loader \"" + loaderKey + "\" does not exist.");
                        var path = entry.getKey();
                        var id = new ResourceLocation(path.getNamespace(), path.getPath().substring("contexts/".length(), path.getPath().indexOf(fileExtension)).replaceAll("\\/", "."));
                        var context = serializer.deserialize(id, obj);
                        Objects.requireNonNull(context, "Deserializer \"" + loaderKey + "\" returned null context.");
                        read.add(context);
                    } catch (NullPointerException e) {
                        ContextuallyCommon.LOGGER.error(e.getMessage());
                    }
                } catch (NullPointerException e) {
                    throw new IllegalStateException("Context json must contain the \"loader\" key with a valid loader.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return read;
    }

    @NotNull
    public static Condition deserializeCondition(JsonObject conditionObject) {
        Objects.requireNonNull(conditionObject);
        for (IConditionSerializer serializer : CONDITION_SERIALIZERS) {
            if (serializer.getId().toString().equals(conditionObject.get("condition").getAsString())) {
                return serializer.deserialize(conditionObject);
            }
        }
        throw new IllegalArgumentException("No serializer for condition: " + conditionObject + " exists.");
    }

    @NotNull
    public static Key deserializeKey(JsonObject keyObject) {
        Objects.requireNonNull(keyObject);
        for (String e : keyObject.keySet()) {
            if (KEY_SERIALIZERS_MAP.containsKey(e)) {
                var serializer = KEY_SERIALIZERS_MAP.get(e);
                return serializer.deserialize(keyObject.get(e));
            }
        }
        throw new IllegalArgumentException("No serializer for condition: " + keyObject + " exists.");
    }

    static {
        ImmutableMap.Builder<ResourceLocation, IContextSerializer<?, ?>> contextSerializerBuilder = ImmutableMap.builder();
        contextSerializerBuilder.put(ContextuallyCommon.modloc("block_context"), new BlockKeyContext.Serializer());
        contextSerializerBuilder.put(ContextuallyCommon.modloc("entity_context"), new EntityKeyContext.Serializer());
        contextSerializerBuilder.put(ContextuallyCommon.modloc("global_context"), new GlobalKeyContext.Serializer());

        // TODO: Fire registry event
        CONTEXT_SERIALIZERS = contextSerializerBuilder.build();

        ImmutableSet.Builder<IConditionSerializer> conditionSerializerBuilder = ImmutableSet.builder();
        conditionSerializerBuilder.add(ConditionSerializers.PLAYER_HELD_ITEM_SERIALIZER);
        conditionSerializerBuilder.add(ConditionSerializers.PLAYER_HELD_BLOCK_SERIALIZER);
        conditionSerializerBuilder.add(ConditionSerializers.VILLAGER_PROFESSION_SERIALIZER);
        conditionSerializerBuilder.add(ConditionSerializers.NOT_SERIALIZER);

        // TODO: Fire registry event
        CONDITION_SERIALIZERS = conditionSerializerBuilder.build();

        ImmutableMap.Builder<String, IKeySerializer> keySerializerBuilder = ImmutableMap.builder();
        keySerializerBuilder.put("mapping", KeySerializers.MAPPING_KEY_SERIALIZER);
//        keySerializerBuilder.put("key", KeySerializers.KEYCODE_KEY_SERIALIZER);
        keySerializerBuilder.put("texture", KeySerializers.TEXTURE_KEY_SERIALIZER);

        // TODO: Fire registry event
        KEY_SERIALIZERS_MAP = keySerializerBuilder.build();
    }
}
