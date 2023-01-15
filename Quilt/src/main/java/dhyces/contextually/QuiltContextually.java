package dhyces.contextually;

import dhyces.contextually.client.ContextGui;
import dhyces.contextually.client.ContextuallyClient;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.client.ClientModInitializer;
import org.quiltmc.qsl.lifecycle.api.client.event.ClientTickEvents;
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.client.ClientResourceLoaderEvents;
import org.quiltmc.qsl.resource.loader.api.reloader.IdentifiableResourceReloader;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class QuiltContextually implements ClientModInitializer {

    private static ContextGui contextHud;

    @Override
    public void onInitializeClient(ModContainer mod) {
        ClientResourceLoaderEvents.START_RESOURCE_PACK_RELOAD.register(this::clientInit);
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(new WrappedReloader(Contextually.id("key_textures"), ContextuallyClient::getTextureManager));
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(new WrappedReloader(Contextually.id("contexts"), ContextuallyClient::getContextManager));
        ClientTickEvents.START.register(this::guiRenderTick);
    }

    private void clientInit(Minecraft client, ResourceManager resourceManager, boolean first) {
        if (first) {
            ContextuallyClient.init();
            contextHud = new ContextGui(client, client.getItemRenderer());
        }
    }

    private void guiRenderTick(Minecraft client) {
        contextHud.tick(client.isPaused());
    }

    record WrappedReloader(ResourceLocation id, Supplier<PreparableReloadListener> listenerSupplier) implements IdentifiableResourceReloader {
        @Override
        public @NotNull ResourceLocation getQuiltId() {
            return id;
        }

        @Override
        public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
            return listenerSupplier.get().reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
        }
    }
}
