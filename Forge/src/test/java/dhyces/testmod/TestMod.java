package dhyces.testmod;

import dhyces.contextually.client.core.contexts.IKeyContext;
import dhyces.contextually.client.core.contexts.objects.ItemKeyContext;
import dhyces.contextually.common.data.ContextProvider;
import dhyces.testmod.data.TestContextProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.BiConsumer;

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
        event.getGenerator().addProvider(event.includeClient(), new ContextProvider(packOutput, fileHelper, "addendum") {
            @Override
            protected void addContexts(BiConsumer<String, IKeyContext<?, ?>> exporter) {
                ContextProvider.ContextBuilder.create(ItemKeyContext::new).addCondition(ContextProvider.ConditionFactories.heldKey(49)).export("testing123", exporter);
            }
        });
    }
}
