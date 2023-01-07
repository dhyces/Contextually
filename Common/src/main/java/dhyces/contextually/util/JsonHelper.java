package dhyces.contextually.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import dhyces.contextually.client.keys.CodeKey;
import dhyces.contextually.client.keys.IKey;
import dhyces.contextually.client.keys.KeyUtils;
import dhyces.contextually.client.keys.MappingKey;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.util.GsonHelper;

public class JsonHelper {

    public static CompoundTag readNbt(JsonElement element) {
        try {
            if (element.isJsonObject()) {
                return TagParser.parseTag(element.toString());
            } else {
                return TagParser.parseTag(GsonHelper.convertToString(element, "nbt"));
            }
        } catch (CommandSyntaxException e) {
            throw new JsonSyntaxException("Invalid NBT Entry: " + e);
        }
    }

    public static IKey getKeyFor(JsonPrimitive primitive) {
        if (primitive.isString()) {
            return new MappingKey(KeyUtils.get(primitive.getAsString()));
        }
        return new CodeKey(primitive.getAsInt());
    }
}
