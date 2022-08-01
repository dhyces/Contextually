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
        for (Map.Entry<ResourceLocation, Resource> entry : resourceManager.listResources("contexts", resourceLocation -> resourceLocation.getPath().endsWith(fileExtension)).entrySet()) {
            try(var reader = entry.getValue().openAsReader()) {
                var jsonElement = JsonParser.parseReader(reader);
                // get the object of the file
                // iterate through context serializer entries to find matching targets
                // TODO: redo this whole process
                var obj = jsonElement.getAsJsonObject();
                var loaderJson = obj.get("loader");
                checkParse(loaderJson != null, "Key \"loader\" must not be null. Must be a valid loader id.");
                checkParse(loaderJson.isJsonPrimitive() && loaderJson.getAsJsonPrimitive().isString(), "Value of key \"loader\" must be a valid string.");
                var loader = loaderJson.getAsString();
                var loaderKey = loader.contains(":") ? ResourceLocation.of(loader, ':') : ContextuallyCommon.id(loader);
                var serializer = CONTEXT_SERIALIZERS.get(loaderKey);
                checkParse(serializer != null,"Loader \"" + loaderKey + "\" does not exist.");
                var path = entry.getKey();
                var id = new ResourceLocation(path.getNamespace(), path.getPath().substring(0, path.getPath().indexOf(fileExtension)));
                var context = serializer.deserialize(id, obj);
                Objects.requireNonNull(context, "Deserializer \"" + loaderKey + "\" returned null context. Check " + serializer);
                read.add(context);
            } catch (IOException | JsonParseException e) {
                ContextuallyCommon.LOGGER.error("Failed to parse context: " + entry.getKey() + ". " + e.getMessage());
            }
        }
        return read;
    }

    public static void checkParse(boolean expression, String message) {
        if (!expression)
            throw new JsonParseException(message);
    }

    @NotNull
    public static IConditionPredicate deserializeCondition(@NotNull JsonObject conditionObject) {
        checkParse(conditionObject != null, "Object in \"conditions\" array is null.");
        var conditionJson = conditionObject.get("condition");
        checkParse(conditionJson != null, "Key \"condition\" must not be null. Must be a valid condition id.");
        checkParse(conditionJson.isJsonPrimitive() && conditionJson.getAsJsonPrimitive().isString(), "Value of key \"condition\" must be a valid string.");
        var conditionId = conditionJson.getAsString();
        var serializer = CONDITION_SERIALIZERS.get(ContextuallyCommon.defaultingId(conditionId));
        checkParse(serializer != null, "Condition serializer \"" + conditionId + "\" does not exist.");
        return serializer.deserialize(conditionObject);
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
    public static IIcon deserializeIcon(JsonObject iconObject) {
        checkParse(iconObject != null, "Object in \"icons\" array is null.");
        var type = iconObject.get("type");

        checkParse(type != null, "Key \"type\" is must be defined.");
        checkParse(type.isJsonPrimitive() && type.getAsJsonPrimitive().isString(), "Value of key \"type\" must be a valid string.");

        var icon = ContextuallyCommon.defaultingId(type.getAsString());
        var serializer = ICON_SERIALIZERS_MAP.get(icon);
        checkParse(serializer != null, "Icon serializer \"" + icon + "\" does not exist.");
        return serializer.deserialize(iconObject);
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
        contextSerializerBuilder.put(ContextuallyCommon.id("block_context"), new BlockKeyContext.Serializer());
        contextSerializerBuilder.put(ContextuallyCommon.id("item_context"), new ItemKeyContext.Serializer());
        contextSerializerBuilder.put(ContextuallyCommon.id("entity_context"), new EntityKeyContext.Serializer());
        contextSerializerBuilder.put(ContextuallyCommon.id("global_context"), new GlobalKeyContext.Serializer());

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
        iconSerializerBuilder.put(ContextuallyCommon.id("key"), IconSerializers.KEY_SERIALIZER);
        iconSerializerBuilder.put(ContextuallyCommon.id("key_texture"), IconSerializers.KEY_TEXTURE_SERIALIZER);
        iconSerializerBuilder.put(ContextuallyCommon.id("item"), IconSerializers.ITEM_SERIALIZER);
        iconSerializerBuilder.put(ContextuallyCommon.id("animated"), IconSerializers.ANIMATED_SERIALIZER);

        // TODO: Fire registry event
        ICON_SERIALIZERS_MAP = iconSerializerBuilder.build();
    }

    public static record Result(ResourceLocation registryKey, Collection<?> targets, IKeyContext<?> context) {

    }
}
