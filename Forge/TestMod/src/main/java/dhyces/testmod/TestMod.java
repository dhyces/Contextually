package dhyces.testmod;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import dhyces.testmod.data.VanillaContextProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
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
        PackOutput packOutput = event.getGenerator().getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        VanillaContextProvider.VanillaContextEN_USLangProvider en_usLangProvider = new VanillaContextProvider.VanillaContextEN_USLangProvider(packOutput);
        event.getGenerator().addProvider(event.includeClient(), new VanillaContextProvider(packOutput));
        event.getGenerator().addProvider(event.includeClient(), en_usLangProvider);
//        event.getGenerator().addProvider(event.includeClient(), new TestContextProvider(packOutput, fileHelper, "testmod"));
    }
}
