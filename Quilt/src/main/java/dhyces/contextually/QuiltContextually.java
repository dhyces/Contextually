package dhyces.contextually;

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
import org.quiltmc.qsl.resource.loader.api.ResourceLoader;
import org.quiltmc.qsl.resource.loader.api.client.ClientResourceLoaderEvents;
import org.quiltmc.qsl.resource.loader.api.reloader.IdentifiableResourceReloader;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

public class QuiltContextually implements ClientModInitializer {
    @Override
    public void onInitializeClient(ModContainer mod) {
        ClientResourceLoaderEvents.START_RESOURCE_PACK_RELOAD.register(this::clientInit);
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(new WrappedReloader("key_textures", ContextuallyClient::getTextureManager));
        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(new WrappedReloader("contexts", ContextuallyClient::getContextManager));
    }

    private void clientInit(Minecraft client, ResourceManager resourceManager, boolean first) {
        if (first) {
            ContextuallyClient.init();
        }
    }

    class WrappedReloader implements IdentifiableResourceReloader {

        final ResourceLocation id;
        final Supplier<PreparableReloadListener> listener;

        WrappedReloader(String id, Supplier<PreparableReloadListener> listener) {
            this.id = Contextually.id(id);
            this.listener = listener;
        }

        @Override
        public @NotNull ResourceLocation getQuiltId() {
            return id;
        }

        @Override
        public CompletableFuture<Void> reload(PreparationBarrier preparationBarrier, ResourceManager resourceManager, ProfilerFiller preparationsProfiler, ProfilerFiller reloadProfiler, Executor backgroundExecutor, Executor gameExecutor) {
            return listener.get().reload(preparationBarrier, resourceManager, preparationsProfiler, reloadProfiler, backgroundExecutor, gameExecutor);
        }
    }
}
