package dhyces.contextually.client.contexts;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.gson.*;
import com.mojang.datafixers.util.Pair;
import dhyces.contextually.ContextuallyCommon;
import dhyces.contextually.client.contexts.conditions.IConditionPredicate;
import dhyces.contextually.client.contexts.conditions.INamedCondition;
import dhyces.contextually.client.contexts.conditions.serializers.ConditionSerializers;
import dhyces.contextually.client.contexts.conditions.serializers.IConditionSerializer;
import dhyces.contextually.client.contexts.icons.IIcon;
import dhyces.contextually.client.contexts.icons.serializers.IIconSerializer;
import dhyces.contextually.client.contexts.icons.serializers.IconSerializers;
import dhyces.contextually.client.contexts.objects.*;
import dhyces.contextually.client.contexts.objects.serializers.IContextSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.*;

public class KeyContextLoader {

    private static final ImmutableMap<ResourceLocation, IIconSerializer<? extends IIcon>> ICON_SERIALIZERS_MAP;
    private static final ImmutableMap<ResourceLocation, IConditionSerializer<? extends INamedCondition>> CONDITION_SERIALIZERS;
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
                        var id = new ResourceLocation(path.getNamespace(), path.getPath().substring("contexts/".length(), path.getPath().indexOf(fileExtension)).replace('/', '.'));
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
    public static IConditionPredicate deserializeCondition(@NotNull JsonObject conditionObject) {
        Preconditions.checkNotNull(conditionObject);
        var conditionId = conditionObject.get("condition").getAsString();
        try {
            var serializer = CONDITION_SERIALIZERS.get(ResourceLocation.of(conditionId, ':'));
            return serializer.deserialize(conditionObject);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("No serializer for condition: " + conditionId + " exists.");
        }
    }

    @NotNull
    public static <T extends INamedCondition> JsonObject serializeCondition(@NotNull T conditionObject) {
        Preconditions.checkNotNull(conditionObject);
        try {
            return CONDITION_SERIALIZERS.get(conditionObject.getId()).serialize(sillyCast(conditionObject));
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("No serializer for condition: " + conditionObject.getId() + " exists.");
        }
    }

    @NotNull
    public static IIcon deserializeIcon(JsonObject keyObject) {
        Objects.requireNonNull(keyObject);
        var type = keyObject.get("type");

        if (type == null)
            throw new JsonParseException("key \"type\" is must be defined");

        if (!(type instanceof JsonPrimitive primitive && primitive.isString()))
            throw new JsonParseException("key \"type\" is expected to be a string.");

        var key = ContextuallyCommon.modDefaultingloc(type.getAsString());
        if (ICON_SERIALIZERS_MAP.containsKey(key)) {
            var serializer = ICON_SERIALIZERS_MAP.get(key);
            return serializer.deserialize(keyObject);
        }
        throw new IllegalArgumentException("No serializer for icon: " + type + " exists.");
    }

    @NotNull
    public static <T extends IIcon> JsonObject serializeIcon(T iconObject) {
        Objects.requireNonNull(iconObject);
        var serializer = ICON_SERIALIZERS_MAP.get(iconObject.getId());
        if (serializer != null) {
            var jsonObject = new JsonObject();
            jsonObject.add("type", new JsonPrimitive(iconObject.getId().toString()));
            return serializer.serialize(jsonObject, sillyCast(iconObject));
        }
        throw new IllegalArgumentException("No serializer for icon: " + iconObject.getId() + " exists.");
    }

    private static <X> X sillyCast(Object o) {
        return (X)o;
    }

    private static <T extends INamedCondition> void put(ImmutableMap.Builder<ResourceLocation, IConditionSerializer<? extends T>> map, IConditionSerializer<? extends T> serializer) {
        map.put(serializer.getId(), serializer);
    }

    static {
        ImmutableMap.Builder<ResourceLocation, IContextSerializer<?, ?>> contextSerializerBuilder = ImmutableMap.builder();
        contextSerializerBuilder.put(ContextuallyCommon.modloc("block_context"), new BlockKeyContext.Serializer());
        contextSerializerBuilder.put(ContextuallyCommon.modloc("item_context"), new ItemKeyContext.Serializer());
        contextSerializerBuilder.put(ContextuallyCommon.modloc("entity_context"), new EntityKeyContext.Serializer());
        contextSerializerBuilder.put(ContextuallyCommon.modloc("global_context"), new GlobalKeyContext.Serializer());

        // TODO: Fire registry event
        CONTEXT_SERIALIZERS = contextSerializerBuilder.build();

        ImmutableMap.Builder<ResourceLocation, IConditionSerializer<? extends INamedCondition>> conditionSerializerBuilder = ImmutableMap.builder();
        put(conditionSerializerBuilder, ConditionSerializers.TARGET_ENTITY_NBT_SERIALIZER);
        put(conditionSerializerBuilder, ConditionSerializers.TARGET_HELD_NBT_SERIALIZER);
        put(conditionSerializerBuilder, ConditionSerializers.PLAYER_HELD_ITEM_SERIALIZER);
        put(conditionSerializerBuilder, ConditionSerializers.PLAYER_HELD_BLOCK_SERIALIZER);
        put(conditionSerializerBuilder, ConditionSerializers.PLAYER_KEY_HELD_SERIALIZER);
        put(conditionSerializerBuilder, ConditionSerializers.VILLAGER_PROFESSION_SERIALIZER);
        put(conditionSerializerBuilder, ConditionSerializers.NOT_SERIALIZER);
        put(conditionSerializerBuilder, ConditionSerializers.AND_SERIALIZER);
        put(conditionSerializerBuilder, ConditionSerializers.OR_SERIALIZER);

        // TODO: Fire registry event
        CONDITION_SERIALIZERS = conditionSerializerBuilder.build();

        ImmutableMap.Builder<ResourceLocation, IIconSerializer<? extends IIcon>> iconSerializerBuilder = ImmutableMap.builder();
        iconSerializerBuilder.put(ContextuallyCommon.modloc("mapping"), IconSerializers.KEY_MAPPING_SERIALIZER);
        iconSerializerBuilder.put(ContextuallyCommon.modloc("key"), IconSerializers.KEYCODE_SERIALIZER);
        iconSerializerBuilder.put(ContextuallyCommon.modloc("key_texture"), IconSerializers.KEY_TEXTURE_SERIALIZER);
        iconSerializerBuilder.put(ContextuallyCommon.modloc("item"), IconSerializers.ITEM_SERIALIZER);
        iconSerializerBuilder.put(ContextuallyCommon.modloc("animated"), IconSerializers.ANIMATED_SERIALIZER);

        // TODO: Fire registry event
        ICON_SERIALIZERS_MAP = iconSerializerBuilder.build();
    }
}
