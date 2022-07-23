package dhyces.contextually.client.contexts.icons;

import com.google.common.collect.Maps;
import dhyces.contextually.client.keys.KeyUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class IconUtils {

    static final Int2ObjectMap<KeyCodeIcon> KEYS_BY_VALUE = new Int2ObjectOpenHashMap<>();
    static final Map<String, KeyMappingIcon> KEYS_BY_MAPPING_STRING = Maps.newHashMap();
    static final Map<ResourceLocation, KeyTextureIcon> KEYS_BY_LOCATION = Maps.newHashMap();

    public static KeyMappingIcon of(String mappingName) {
        var ret = KEYS_BY_MAPPING_STRING.get(mappingName);
        if (ret == null) {
            ret = new KeyMappingIcon(KeyUtils.get(mappingName));
            KEYS_BY_MAPPING_STRING.put(mappingName, ret);
        }
        return ret;
    }

    public static KeyCodeIcon of(int value) {
        var ret = KEYS_BY_VALUE.get(value);
        if (ret == null) {
            ret = new KeyCodeIcon(value);
            KEYS_BY_VALUE.put(value, ret);
        }
        return ret;
    }

    public static KeyTextureIcon of(ResourceLocation mappingName) {
        var ret = KEYS_BY_LOCATION.get(mappingName);
        if (ret == null) {
            ret = new KeyTextureIcon(mappingName);
            KEYS_BY_LOCATION.put(mappingName, ret);
        }
        return ret;
    }
}
