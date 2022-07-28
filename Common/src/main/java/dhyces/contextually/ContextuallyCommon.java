package dhyces.contextually;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ContextuallyCommon {
    public static final String MOD_ID = "contextually";

    public static final Logger LOGGER = LogManager.getLogger(ContextuallyCommon.class);

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static ResourceLocation modDefaultingloc(String path) {
        return path.contains(":") ? ResourceLocation.of(path, ':') : id(path);
    }
}
