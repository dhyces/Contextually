package dhyces.contextually.client.keys;

import com.google.common.collect.Maps;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.Arrays;
import java.util.Map;

public class KeyUtils {
    static final Map<String, KeyMapping> KEYMAPPING_BY_STRING = Maps.uniqueIndex(Arrays.asList(Minecraft.getInstance().options.keyMappings), c -> c.getName());

    public static KeyMapping get(String mappingName) {
        return KEYMAPPING_BY_STRING.get(mappingName);
    }
}
