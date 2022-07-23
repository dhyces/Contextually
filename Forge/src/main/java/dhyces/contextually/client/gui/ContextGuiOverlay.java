package dhyces.contextually.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import dhyces.contextually.ContextuallyClient;
import dhyces.contextually.client.contexts.objects.IKeyContext;
import dhyces.contextually.client.textures.KeyMappingTextureManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.*;


public class ContextGuiOverlay implements IGuiOverlay {

    public ContextGuiOverlay() {
    }

    @Override
    public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int width, int height) {
        // If a screen is open, we don't want to render. Maybe there should be screen contexts?
        if (gui.getMinecraft().screen != null) {
            return;
        }
        gui.setupOverlayRenderState(true, false, KeyMappingTextureManager.KEYS);

        var mc = gui.getMinecraft();
        var clientPlayer = mc.player;
        var clientLevel = mc.level;

        // Gather contexts
        Set<ContextRenderHolder<?>> contextSet = new LinkedHashSet<>();

        // Global contexts
        if (!ContextuallyClient.getContextManager().getGlobalContexts().isEmpty()) {
            contextSet.add(new ContextRenderHolder<>(clientPlayer, ContextuallyClient.getContextManager().filterGlobalContexts(clientLevel, clientPlayer)));
        }

        // HitResult (entity and solid block) contexts
        var hitResult = mc.hitResult;
        if (!hitResult.getType().equals(HitResult.Type.MISS)) {
            if (hitResult instanceof EntityHitResult entityHitResult) {
                var entity = entityHitResult.getEntity();
                var contexts = ContextuallyClient.getContextManager().filterContextsForEntity(entity, entityHitResult, clientLevel, clientPlayer);
                // TODO: add an event
                contextSet.add(new ContextRenderHolder<>(entity, contexts));
            } else if (hitResult instanceof BlockHitResult blockHitResult) {
                var block = clientLevel.getBlockState(blockHitResult.getBlockPos());
                var contexts = ContextuallyClient.getContextManager().filterContextsForBlock(block, blockHitResult, clientLevel, clientPlayer);
                // TODO: add an event
                contextSet.add(new ContextRenderHolder<>(block, contexts));
            }
        }

        // Fluid HitResult contexts
        var fluidHitResult = fluidPass(clientPlayer, partialTick);
        if (!fluidHitResult.getType().equals(HitResult.Type.MISS) && fluidHitResult instanceof BlockHitResult fluidResult) {
            var fluid = clientLevel.getFluidState(fluidResult.getBlockPos());
            if (!fluid.isEmpty()) {
                var contexts = ContextuallyClient.getContextManager().filterContextsForFluid(fluid, fluidResult, clientLevel, clientPlayer);
                // TODO: add an event
                contextSet.add(new ContextRenderHolder<>(fluid.createLegacyBlock(), contexts));
            }
        }

        // Item contexts

        var heightPos = height - 16 - 32;
        var half = contextSet.stream().flatMap(c -> c.contextCollection.stream()).count() / 2;
        var count = 0;
        // Render contexts
        for (ContextRenderHolder<?> holder : contextSet) {
            var context = holder.contextObject();
            for (IKeyContext<?> keyContext : holder.contextCollection()) {
                var xPos = count < half ? IKeyContext.PADDING : width - keyContext.width(gui.getFont()) - IKeyContext.PADDING;
                keyContext.renderIcons(cast(context), gui, poseStack, partialTick, xPos, heightPos, width, height);
                heightPos = count != half-1 ? heightPos - (16 + IKeyContext.SMALL_PADDING) : height - 16 - 32;
                count++;
            }
        }
        heightPos = height - 16 - 32;
        count = 0;
        for (ContextRenderHolder<?> holder : contextSet) {
            var context = holder.contextObject();
            for (IKeyContext<?> keyContext : holder.contextCollection()) {
                var xPos = count < half ? IKeyContext.PADDING : width - keyContext.width(gui.getFont()) - IKeyContext.PADDING;
                keyContext.renderText(cast(context), gui, poseStack, partialTick, xPos, heightPos + 4, width, height);
                heightPos = count != half-1 ? heightPos - (16 + IKeyContext.SMALL_PADDING) : height - 16 - 32;
                count++;
            }
        }
    }

    // We should know that the type matches
    @SuppressWarnings("unchecked")
    private <X> X cast(Object o) {
        return (X)o;
    }

    private HitResult fluidPass(AbstractClientPlayer pPlayer, float partialTick) {
        return pPlayer.pick(pPlayer.getReachDistance(), partialTick, true);
    }

    record ContextRenderHolder<T>(T contextObject, Collection<IKeyContext<T>> contextCollection) {}
}
