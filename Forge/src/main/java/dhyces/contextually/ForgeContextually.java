package dhyces.contextually;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod(Contextually.MOD_ID)
public class ForgeContextually {

    public ForgeContextually() {
        var modBus = FMLJavaModLoadingContext.get().getModEventBus();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ForgeContextuallyClient.init(modBus);
        }
    }
}
