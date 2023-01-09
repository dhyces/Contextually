package dhyces.testmod;

import dhyces.testmod.data.TestContextProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("testmod")
public class TestMod {

    public TestMod() {
        if (DatagenModLoader.isRunningDataGen()) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(this::addProviders);
        }
    }

    private void addProviders(final GatherDataEvent event) {
        var packOutput = event.getGenerator().getPackOutput();
        var fileHelper = event.getExistingFileHelper();
        event.getGenerator().addProvider(event.includeClient(), new TestContextProvider(packOutput, fileHelper, "testmod"));
    }
}
