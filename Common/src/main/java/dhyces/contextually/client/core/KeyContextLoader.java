package dhyces.contextually.client.core;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.gson.JsonElement;
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

import java.io.BufferedReader;
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

    static final String FILE_EXTENSION = ".json";

    public KeyContextLoader(ResourceManager resourceManager) {
        this.resourceManager = resourceManager;
    }

    public List<Pair<? extends Collection<?>, ? extends IKeyContext<?, ?>>> load() {
        List<Pair<? extends Collection<?>, ? extends IKeyContext<?, ?>>> read = new LinkedList<>();
        int count = 0;
        for (Map.Entry<ResourceLocation, Resource> entry : resourceManager.listResources("contexts", resourceLocation -> resourceLocation.getPath().endsWith(FILE_EXTENSION)).entrySet()) {
            try(BufferedReader reader = entry.getValue().openAsReader()) {
                JsonElement element = JsonParser.parseReader(reader);
                IKeyContext<?, ?> context = IKeyContext.CODEC.parse(JsonOps.INSTANCE, element).getOrThrow(false, Contextually.LOGGER::error);
                context.setIdIfNull(createId(entry.getKey()));
                read.add(Pair.of(context.getTargets(), context));
                count++; // TODO: for some reason I don't remember, this was counting the number of targets before
            } catch (RuntimeException | IOException e) {
                Contextually.LOGGER.error("Failed to parse context: " + entry.getKey() + ". " + e.getMessage());
            }
        }
        Contextually.LOGGER.info("Loaded " + count + " contexts");
        return read;
    }

    private static ResourceLocation createId(ResourceLocation fileRL) {
        String path = fileRL.getPath();
        return new ResourceLocation(fileRL.getNamespace(), path.substring(0, path.indexOf(FILE_EXTENSION)));
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
