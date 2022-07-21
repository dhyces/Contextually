package dhyces.contextually.client.gui;

import com.mojang.blaze3d.vertex.PoseStack;
import dhyces.contextually.ContextuallyClient;
import dhyces.contextually.client.contexts.objects.IKeyContext;
import dhyces.contextually.client.textures.KeyMappingTextureManager;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;


public class ContextGuiOverlay implements IGuiOverlay {

    public static int LAST_PRESS = -1;

    public ContextGuiOverlay() {
    }

    @Override
    public void render(ForgeGui gui, PoseStack poseStack, float partialTick, int width, int height) {
        gui.setupOverlayRenderState(true, false, KeyMappingTextureManager.KEYS);

        var mc = gui.getMinecraft();
        var clientPlayer = mc.player;
        var clientLevel = mc.level;
        var font = mc.font;
        var hitResult = mc.hitResult;
        var fluidHitResult = fluidPass(clientPlayer, partialTick);

//        if (!fluidHitResult.getType().equals(HitResult.Type.MISS)) {
//            hitResult = fluidHitResult;
//        }

        if (hitResult.getType().equals(HitResult.Type.MISS))
            return;

        if (hitResult instanceof EntityHitResult entityHitResult) {
            var entity = entityHitResult.getEntity();
            var contexts = ContextuallyClient.getContextManager().getContextsForEntity(entity);
            renderContexts(gui, poseStack, entity, contexts, partialTick, width, height);
        } else if (hitResult instanceof BlockHitResult blockHitResult) {
            var block = clientLevel.getBlockState(blockHitResult.getBlockPos());
            var contexts = ContextuallyClient.getContextManager().getContextsForBlock(block);
            renderContexts(gui, poseStack, block, contexts, partialTick, width, height);
        }
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
}
