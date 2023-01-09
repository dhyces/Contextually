package dhyces.contextually.client.core.conditions.predicates;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dhyces.contextually.util.MoreCodecs;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

import java.util.*;

public record ItemContextPredicate(Optional<TagKey<Item>> tag, Set<Item> items, MinMaxBounds.Ints count,
                                   MinMaxBounds.Ints durability, Set<EnchantmentContextPredicate> enchantments,
                                   Optional<Potion> potion, Optional<NbtContextPredicate> nbt) {
    public static final Codec<ItemContextPredicate> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.optionalField("tag", TagKey.codec(Registries.ITEM)).forGetter(ItemContextPredicate::tag),
                    Codec.optionalField("items", BuiltInRegistries.ITEM.byNameCodec().listOf().xmap(Set::copyOf, List::copyOf)).orElse(Optional.of(Set.of())).xmap(Optional::get, Optional::of).forGetter(ItemContextPredicate::items),
                    Codec.optionalField("count", MoreCodecs.INT_MIN_MAX_BOUNDS_CODEC).orElse(Optional.of(MinMaxBounds.Ints.ANY)).xmap(Optional::get, Optional::of).forGetter(ItemContextPredicate::count),
                    Codec.optionalField("durability", MoreCodecs.INT_MIN_MAX_BOUNDS_CODEC).orElse(Optional.of(MinMaxBounds.Ints.ANY)).xmap(Optional::get, Optional::of).forGetter(ItemContextPredicate::durability),
                    Codec.optionalField("enchantment_predicates", EnchantmentContextPredicate.CODEC.listOf().xmap(Set::copyOf, List::copyOf)).orElse(Optional.of(Set.of())).xmap(Optional::get, Optional::of).forGetter(ItemContextPredicate::enchantments),
                    Codec.optionalField("potion", BuiltInRegistries.POTION.byNameCodec()).forGetter(ItemContextPredicate::potion),
                    Codec.optionalField("nbt_predicate", NbtContextPredicate.CODEC).forGetter(ItemContextPredicate::nbt)
            ).apply(instance, ItemContextPredicate::new)
    );

    public boolean matches(ItemStack stack) {
        if (tag.isPresent() && !stack.is(tag.get())) {
            return false;
        } else if (!items.isEmpty() && !items.contains(stack.getItem())) {
            return false;
        } else if (!count.matches(stack.getCount())) {
            return false;
        } else if (stack.isDamageableItem() && !durability.matches(stack.getDamageValue())) {
            return false;
        } else if (potion.isPresent() && !PotionUtils.getPotion(stack).equals(potion.get())) {
            return false;
        } else if (nbt.isPresent() && !nbt.get().matches(stack.getTag())) {
            return false;
        } else {
            if (!enchantments.isEmpty()) {
                var itemEnchants = EnchantmentHelper.deserializeEnchantments(stack.getEnchantmentTags());
                for (EnchantmentContextPredicate predicate : enchantments) {
                    if (!predicate.isIn(itemEnchants)) {
                        return false;
                    }
                }
            }
            return true;
        }
    }
    
    public static class Builder {
        TagKey<Item> tag = null;
        Set<Item> items = new HashSet<>();
        MinMaxBounds.Ints count = MinMaxBounds.Ints.ANY;
        MinMaxBounds.Ints durability = MinMaxBounds.Ints.ANY;
        Set<EnchantmentContextPredicate> enchantments = new HashSet<>();
        Potion potion = null;
        NbtContextPredicate nbt = NbtContextPredicate.ANY;

        private Builder() {}
        private Builder(Item... items) {this.items.addAll(Arrays.asList(items));}

        public static Builder create() {
            return new Builder();
        }

        public static Builder create(Item... items) {
            return new Builder(items);
        }

        public Builder item(Item item) {
            this.items.add(item);
            return this;
        }

        public Builder count(MinMaxBounds.Ints range) {
            this.count = range;
            return this;
        }

        public Builder minCount(int minimum) {
            this.count = MinMaxBounds.Ints.atLeast(minimum);
            return this;
        }

        public Builder maxCount(int maximum) {
            this.count = MinMaxBounds.Ints.atMost(maximum);
            return this;
        }

        public Builder exactCount(int exactly) {
            this.count = MinMaxBounds.Ints.exactly(exactly);
            return this;
        }

        public Builder betweenCount(int min, int max) {
            this.count = MinMaxBounds.Ints.between(min, max);
            return this;
        }

        public Builder durability(MinMaxBounds.Ints range) {
            this.durability = range;
            return this;
        }

        public Builder minDurability(int minimum) {
            this.durability = MinMaxBounds.Ints.atLeast(minimum);
            return this;
        }

        public Builder maxDurability(int maximum) {
            this.durability = MinMaxBounds.Ints.atMost(maximum);
            return this;
        }

        public Builder exactDurability(int exactly) {
            this.durability = MinMaxBounds.Ints.exactly(exactly);
            return this;
        }

        public Builder betweenDurability(int min, int max) {
            this.durability = MinMaxBounds.Ints.between(min, max);
            return this;
        }

        public Builder enchantment(EnchantmentContextPredicate predicate) {
            this.enchantments.add(predicate);
            return this;
        }

        public Builder enchantment(Enchantment enchantment, MinMaxBounds.Ints levelRange) {
            this.enchantments.add(new EnchantmentContextPredicate(Optional.of(enchantment), levelRange));
            return this;
        }

        public Builder enchantment(Enchantment enchantment) {
            this.enchantments.add(new EnchantmentContextPredicate(Optional.of(enchantment), MinMaxBounds.Ints.ANY));
            return this;
        }

        public Builder enchantment(MinMaxBounds.Ints levelRange) {
            this.enchantments.add(new EnchantmentContextPredicate(Optional.empty(), levelRange));
            return this;
        }

        public Builder potion(Potion potion) {
            this.potion = potion;
            return this;
        }

        public Builder nbt(NbtContextPredicate predicate) {
            this.nbt = predicate;
            return this;
        }

        public Builder nbt(CompoundTag nbt) {
            this.nbt = new NbtContextPredicate(Optional.of(nbt));
            return this;
        }

        public ItemContextPredicate build() {
            return new ItemContextPredicate(Optional.ofNullable(tag), items, count, durability, enchantments, Optional.ofNullable(potion), Optional.ofNullable(nbt));
        }
    }
}
