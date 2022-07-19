package dhyces.contextually;

import net.minecraft.resources.ResourceLocation;

public class ContextuallyCommon {
    public static final String MOD_ID = "contextually";

    public static ResourceLocation modloc(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
