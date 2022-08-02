package dhyces.contextually.client.gui;

import com.google.common.collect.Sets;
import dhyces.contextually.client.keys.IKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class RenderRect implements IRenderRect {

    double x, y, width, height;
    @Nullable ResourceLocation matchingId;
    @Nullable Set<IKey> matchingKeys;
    boolean visible = true;

    RenderRect(double x, double y, double width, double height, @Nullable ResourceLocation matchingId, @Nullable Set<IKey> matchingKeys) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.matchingId = matchingId;
        this.matchingKeys = matchingKeys;
    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getWidth() {
        return width;
    }

    @Override
    public double getHeight() {
        return height;
    }

    @Override
    public void setX(double value) {
        this.x = value;
    }

    @Override
    public void setY(double value) {
        this.y = value;
    }

    @Override
    public void setWidth(double value) {
        this.width = value;
    }

    @Override
    public void setHeight(double value) {
        this.height = value;
    }

    public static Builder builder(double x, double y, double width, double height) {
        return new Builder(x, y, width, height);
    }

    public static class Builder {
        double x, y, width, height;
        ResourceLocation matchId;
        Set<IKey> matchKeys;

        Builder(double x, double y, double width, double height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        public Builder matchId(ResourceLocation id) {
            this.matchId = id;
            return this;
        }

        public Builder matchKey(IKey key) {
            if (matchKeys == null)
                matchKeys = Sets.newHashSet();
            this.matchKeys.add(key);
            return this;
        }

        public RenderRect build() {
            return new RenderRect(x, y, width, height, matchId, matchKeys);
        }
    }
}
