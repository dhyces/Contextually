package dhyces.contextually.client.core;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import dhyces.contextually.Contextually;
import dhyces.contextually.client.core.conditions.IConditionType;
import dhyces.contextually.client.core.conditions.IContextCondition;
import dhyces.contextually.client.core.contexts.IKeyContext;
import dhyces.contextually.client.core.contexts.IKeyContextType;
import dhyces.contextually.client.core.icons.IIcon;
import dhyces.contextually.client.core.icons.IIconType;
import dhyces.contextually.util.MoreCodecs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;

import java.io.IOException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class KeyContextLoader {

    private static final BiMap<ResourceLocation, IIconType<?>> ICON_TYPE_MAP = HashBiMap.create();
    private static final BiMap<ResourceLocation, IConditionType<?>> CONDITION_TYPE_MAP = HashBiMap.create();
    private static final BiMap<ResourceLocation, IKeyContextType<?>> CONTEXT_TYPE_MAP = HashBiMap.create();

    public static final Codec<IIconType<?>> ICON_MAP_CODEC = MoreCodecs.CONTEXTUALLY_RESOURCE_LOCATION.xmap(ICON_TYPE_MAP::get, ICON_TYPE_MAP.inverse()::get);
    public static final Codec<IConditionType<?>> CONDITION_MAP_CODEC = MoreCodecs.CONTEXTUALLY_RESOURCE_LOCATION.xmap(CONDITION_TYPE_MAP::get, CONDITION_TYPE_MAP.inverse()::get);
    public static final Codec<IKeyContextType<?>> CONTEXT_MAP_CODEC = MoreCodecs.CONTEXTUALLY_RESOURCE_LOCATION.xmap(CONTEXT_TYPE_MAP::get, CONTEXT_TYPE_MAP.inverse()::get);

    private final ResourceManager resourceManager;

    final String fileExtension = ".json";

    public KeyContextLoader(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public List<Pair<? extends Collection<?>, ? extends IKeyContext<?, ?>>> load() {
        List<Pair<? extends Collection<?>, ? extends IKeyContext<?, ?>>> read = new LinkedList<>();
        for (Map.Entry<ResourceLocation, Resource> entry : resourceManager.listResources("contexts", resourceLocation -> resourceLocation.getPath().endsWith(fileExtension)).entrySet()) {
            try(var reader = entry.getValue().openAsReader()) {
                IKeyContext<?, ?> context = IKeyContext.CODEC.parse(JsonOps.INSTANCE, JsonParser.parseReader(reader)).getOrThrow(false, Contextually.LOGGER::error);
                context.setIdIfNull(entry.getKey());
                // get the object of the file
                // iterate through context serializer entries to find matching targets
                // TODO: redo this whole process
//                var obj = jsonElement.getAsJsonObject();
//                var loaderJson = obj.get("loader");
//                checkParse(loaderJson != null, "Key \"loader\" must not be null. Must be a valid loader id.");
//                checkParse(loaderJson.isJsonPrimitive() && loaderJson.getAsJsonPrimitive().isString(), "Value of key \"loader\" must be a valid string.");
//                var loader = loaderJson.getAsString();
//                var loaderKey = loader.contains(":") ? ResourceLocation.of(loader, ':') : Contextually.id(loader);
//                var serializer = CONTEXT_TYPE_MAP.get(loaderKey);
//                checkParse(serializer != null,"Loader \"" + loaderKey + "\" does not exist.");
//                var path = entry.getKey();
//                var id = new ResourceLocation(path.getNamespace(), path.getPath().substring(0, path.getPath().indexOf(fileExtension)));
//                var context = serializer.deserialize(id, obj);
//                Objects.requireNonNull(context, "Deserializer \"" + loaderKey + "\" returned null context. Check " + serializer);
                read.add(Pair.of(context.getTargets(), context));
            } catch (RuntimeException | IOException e) {
                Contextually.LOGGER.error("Failed to parse context: " + entry.getKey() + ". " + e.getMessage());
            }
        }
        return read;
    }

    public static void checkParse(boolean expression, String message) {
        if (!expression)
            throw new JsonParseException(message);
    }

    public static <T extends IIcon> IIconType<T> registerIconType(ResourceLocation id, Codec<T> codec) {
        IIconType<T> type = IIconType.create(id, codec);
        ICON_TYPE_MAP.put(id, type);
        return type;
    }

    public static <T extends IContextCondition> IConditionType<T> registerConditionType(ResourceLocation id, Codec<T> codec) {
        IConditionType<T> type = IConditionType.create(id, codec);
        CONDITION_TYPE_MAP.put(id, type);
        return type;
    }

    public static <T extends IKeyContext<?, ?>> IKeyContextType<T> registerKeyContextType(ResourceLocation id, Codec<T> codec) {
        IKeyContextType<T> type = IKeyContextType.create(id, codec);
        CONTEXT_TYPE_MAP.put(id, type);
        return type;
    }

    public static record Result(ResourceLocation registryKey, Collection<?> targets, IKeyContext<?, ?> context) {

    }
}
