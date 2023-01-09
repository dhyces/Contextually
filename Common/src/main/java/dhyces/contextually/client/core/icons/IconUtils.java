package dhyces.contextually.client.core.icons;

import dhyces.contextually.client.core.icons.objects.KeyIcon;
import dhyces.contextually.client.core.icons.objects.KeyTextureIcon;
import dhyces.contextually.client.keys.CodeKey;
import dhyces.contextually.client.keys.MappingKey;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class IconUtils {

    static final Int2ObjectMap<KeyIcon> KEYS_BY_VALUE = new Int2ObjectOpenHashMap<>();
    static final Map<String, KeyIcon> KEYS_BY_MAPPING_STRING = new HashMap<>();
    static final Map<ResourceLocation, KeyTextureIcon> KEYS_BY_LOCATION = new HashMap<>();

    public static KeyIcon of(String mappingName) {
        var ret = KEYS_BY_MAPPING_STRING.get(mappingName);
        if (ret == null) {
            ret = new KeyIcon(new MappingKey(mappingName));
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

    public static KeyIcon of(MappingKey mappingKey) {
        var ret = KEYS_BY_MAPPING_STRING.get(mappingKey.mapping());
        if (ret == null) {
            ret = new KeyIcon(mappingKey);
            KEYS_BY_MAPPING_STRING.put(mappingKey.mapping(), ret);
        }
        return ret;
    }

    public static KeyIcon of(CodeKey codeKey) {
        var ret = KEYS_BY_VALUE.get(codeKey.getValue());
        if (ret == null) {
            ret = new KeyIcon(codeKey);
            KEYS_BY_VALUE.put(codeKey.getValue(), ret);
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
