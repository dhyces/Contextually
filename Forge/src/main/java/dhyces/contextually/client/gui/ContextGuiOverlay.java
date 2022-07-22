package dhyces.contextually.client.gui;

import com.google.common.collect.Sets;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.datafixers.util.Pair;
import dhyces.contextually.Contextually;
import dhyces.contextually.ContextuallyClient;
import dhyces.contextually.client.contexts.objects.IKeyContext;
import dhyces.contextually.client.textures.KeyMappingTextureManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.Event;

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
        Set<ContextRenderHolder<?>> contextList = new LinkedHashSet<>();

        // Global contexts
        if (!ContextuallyClient.getContextManager().getGlobalContexts().isEmpty()) {
            contextList.add(new ContextRenderHolder<>(clientPlayer, ContextuallyClient.getContextManager().getGlobalContexts()));
        }

        // HitResult (entity and solid block) contexts
        var hitResult = mc.hitResult;
        if (!hitResult.getType().equals(HitResult.Type.MISS)) {
            if (hitResult instanceof EntityHitResult entityHitResult) {
                var entity = entityHitResult.getEntity();
                var contexts = ContextuallyClient.getContextManager().getContextsForEntity(entity);
                // TODO: add an event
                contextList.add(new ContextRenderHolder<>(entity, contexts));
            } else if (hitResult instanceof BlockHitResult blockHitResult) {
                var block = clientLevel.getBlockState(blockHitResult.getBlockPos());
                var contexts = ContextuallyClient.getContextManager().getContextsForBlock(block);
                // TODO: add an event
                contextList.add(new ContextRenderHolder<>(block, contexts));
            }
        }

        // Fluid HitResult contexts
        var fluidHitResult = fluidPass(clientPlayer, partialTick);
        if (!fluidHitResult.getType().equals(HitResult.Type.MISS) && fluidHitResult instanceof BlockHitResult fluidResult) {
            var fluid = clientLevel.getFluidState(fluidResult.getBlockPos());
            if (!fluid.isEmpty()) {
                var contexts = ContextuallyClient.getContextManager().getContextsForFluid(fluid);
                // TODO: add an event
                contextList.add(new ContextRenderHolder<>(fluid.createLegacyBlock(), contexts));
            }
        }

        var heightPos = height - 16 - 32;
        // Render contexts
        for (ContextRenderHolder<?> holder : contextList) {
            var context = holder.contextObject();
            for (IKeyContext<?> keyContext : holder.contextCollection()) {
                if (checkConditions(keyContext, context, gui.getMinecraft().level, gui.getMinecraft().player)) {
                    keyContext.renderIcons(cast(context), gui, poseStack, partialTick, width - keyContext.width(gui.getFont()) - IKeyContext.PADDING, heightPos, width, height);
                    heightPos -= 16 + IKeyContext.SMALL_PADDING;
                }
            }
        }
        heightPos = height - 16 - 32;
        for (ContextRenderHolder<?> holder : contextList) {
            var context = holder.contextObject();
            for (IKeyContext<?> keyContext : holder.contextCollection()) {
                if (checkConditions(keyContext, context, gui.getMinecraft().level, gui.getMinecraft().player)) {
                    keyContext.renderText(cast(context), gui, poseStack, partialTick, width - keyContext.width(gui.getFont()) - IKeyContext.PADDING, heightPos + 4, width, height);
                    heightPos -= 16 + IKeyContext.SMALL_PADDING;
                }
            }
        }
    }

    // We should know that the type matches
    @SuppressWarnings("unchecked")
    private <X> X cast(Object o) {
        return (X)o;
    }

    private <T> void renderContexts(ForgeGui gui, PoseStack poseStack, T contextObject, Collection<IKeyContext<T>> contexts, float partialTick, int width, int height) {
        var heightPos = height - 16 - 32;
        List<IKeyContext<T>> textToRender = new LinkedList<>();
        for (IKeyContext<T> context : contexts) {
            if (checkConditions(context, contextObject, gui.getMinecraft().level, gui.getMinecraft().player)) {
                context.renderIcons(contextObject, gui, poseStack, partialTick, width - context.width(gui.getFont()) - IKeyContext.PADDING, heightPos, width, height);
                heightPos -= 16 + IKeyContext.SMALL_PADDING;
                textToRender.add(context);
            }
        }
        heightPos = height - 16 - 32;
        for (IKeyContext<T> context : textToRender) {
            context.renderText(contextObject, gui, poseStack, partialTick, width - context.width(gui.getFont()) - IKeyContext.PADDING, heightPos + 4, width, height);
            heightPos -= 16 + IKeyContext.SMALL_PADDING;
        }
    }

    private boolean checkConditions(IKeyContext<?> context, Object target, ClientLevel level, AbstractClientPlayer player) {
        return context.getConditions().isEmpty() || context.getConditions().stream().allMatch(c -> c.test(target, level, player));
    }

    private HitResult fluidPass(AbstractClientPlayer pPlayer, float partialTick) {
        return pPlayer.pick(pPlayer.getReachDistance(), partialTick, true);
    }

    record ContextRenderHolder<T>(T contextObject, Collection<IKeyContext<T>> contextCollection) {}
}
