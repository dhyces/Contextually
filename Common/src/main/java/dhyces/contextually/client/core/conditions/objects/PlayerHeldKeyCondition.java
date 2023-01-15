package dhyces.contextually.client.core.conditions.objects;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.serialization.Codec;
import dhyces.contextually.client.core.conditions.IConditionType;
import dhyces.contextually.client.core.conditions.IContextCondition;
import dhyces.contextually.client.keys.IKey;
import dhyces.contextually.mixins.client.InputConstantsTypeAccessor;
import dhyces.contextually.services.Services;
import dhyces.contextually.util.KeyUtils;
import dhyces.contextually.util.MoreCodecs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.world.phys.HitResult;
import org.lwjgl.glfw.GLFW;

public record PlayerHeldKeyCondition(IKey key) implements IContextCondition {
    public static final Codec<PlayerHeldKeyCondition> CODEC = MoreCodecs.KEY_CODEC.fieldOf("key").xmap(
            PlayerHeldKeyCondition::new,
            PlayerHeldKeyCondition::key
    ).codec();

    @Override
    public boolean test(Object target, HitResult pos, ClientLevel level, AbstractClientPlayer player) {
        InputConstants.Key key = KeyUtils.getKey(key().getValue());
        if (key.getType().equals(InputConstants.Type.MOUSE)) {
            return GLFW.glfwGetMouseButton(Minecraft.getInstance().getWindow().getWindow(), key.getValue()) == 1;
        } else if (key.getType().equals(InputConstants.Type.KEYSYM)) {
            return InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), key.getValue());
        } else {
            return false;
        }
    }

    @Override
    public IConditionType<?> getType() {
        return IConditionType.HELD_KEY;
    }
}
