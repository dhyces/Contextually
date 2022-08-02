package dhyces.contextually;

import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Contextually {
    public static final String MOD_ID = "contextually";

    public static final Logger LOGGER = LogManager.getLogger(Contextually.class);

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static ResourceLocation defaultingId(String path) {
        return path.contains(":") ? ResourceLocation.of(path, ':') : id(path);
    }
}
