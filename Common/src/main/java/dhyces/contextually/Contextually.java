package dhyces.contextually;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

public class Contextually {
    public static final String MOD_ID = "contextually";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static ResourceLocation defaultingId(String path) {
        return path.contains(":") ? ResourceLocation.of(path, ':') : id(path);
    }
}
