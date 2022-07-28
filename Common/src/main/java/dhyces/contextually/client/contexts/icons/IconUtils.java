package dhyces.contextually.client.contexts.icons;

import com.google.common.collect.Maps;
import dhyces.contextually.client.keys.CodeKey;
import dhyces.contextually.client.keys.KeyUtils;
import dhyces.contextually.client.keys.MappingKey;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;

import java.util.Map;

public class IconUtils {

    static final Int2ObjectMap<KeyIcon> KEYS_BY_VALUE = new Int2ObjectOpenHashMap<>();
    static final Map<String, KeyIcon> KEYS_BY_MAPPING_STRING = Maps.newHashMap();
    static final Map<ResourceLocation, KeyTextureIcon> KEYS_BY_LOCATION = Maps.newHashMap();

    public static KeyIcon of(String mappingName) {
        var ret = KEYS_BY_MAPPING_STRING.get(mappingName);
        if (ret == null) {
            ret = new KeyIcon(new MappingKey(KeyUtils.get(mappingName)));
            KEYS_BY_MAPPING_STRING.put(mappingName, ret);
        }
        return ret;
    }

    public static KeyIcon of(int value) {
        var ret = KEYS_BY_VALUE.get(value);
        if (ret == null) {
            ret = new KeyIcon(new CodeKey(value));
            KEYS_BY_VALUE.put(value, ret);
        }
        return ret;
    }

    public static KeyTextureIcon of(ResourceLocation textureLocation) {
        var ret = KEYS_BY_LOCATION.get(textureLocation);
        if (ret == null) {
            ret = new KeyTextureIcon(textureLocation);
            KEYS_BY_LOCATION.put(textureLocation, ret);
        }
        return ret;
    }
}
