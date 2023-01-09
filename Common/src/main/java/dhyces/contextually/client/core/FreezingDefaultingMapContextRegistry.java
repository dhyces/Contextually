package dhyces.contextually.client.core;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import dhyces.contextually.client.core.contexts.IKeyContext;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;

public class FreezingDefaultingMapContextRegistry<K, V> {

    private boolean frozen;
    final DefaultingMultiMapWrapper<K, IKeyContext<K, V>> delegate;
    final ResourceLocation registryKey;

    public FreezingDefaultingMapContextRegistry(ResourceLocation key, Multimap<K, IKeyContext<K, V>> multimap, Collection<IKeyContext<K, V>> defaultValue) {
        this(key, new DefaultingMultiMapWrapper<>(multimap, defaultValue));
    }

    public FreezingDefaultingMapContextRegistry(ResourceLocation key, DefaultingMultiMapWrapper<K, IKeyContext<K, V>> defaultingMultiMapWrapper) {
        this.delegate = defaultingMultiMapWrapper;
        this.frozen = true;
        this.registryKey = key;
    }

    public static <K, V> FreezingDefaultingMapContextRegistry<K, V> create(ResourceLocation registryKey) {
        return new FreezingDefaultingMapContextRegistry<>(registryKey, ArrayListMultimap.create(), Lists.newArrayList());
    }

    void unfreezeClearRefreeze(Runnable action) {
        frozen = false;
        clear();
        action.run();
        frozen = true;
    }

    public boolean put(K key, IKeyContext<K, V> value) {
        checkFrozen();
        return delegate.put(key, value);
    }

    public boolean addDefault(IKeyContext<K, V> value) {
        checkFrozen();
        return delegate.addDefault(value);
    }

    public Collection<IKeyContext<K, V>> get(K key) {
        return delegate.get(key);
    }

    public boolean containsKey(K value) {
        return delegate.containsKey(value);
    }

    public Collection<IKeyContext<K, V>> getDefaultValue() {
        return delegate.getDefaultValue();
    }

    public boolean isDefault(Collection<IKeyContext<K, V>> collection) {
        return delegate.isDefault(collection);
    }

    void clear() {
        checkFrozen();
        delegate.reset();
    }

    private void checkFrozen() {
        if (frozen)
            throw new IllegalStateException("Context registry frozen. Key: " + registryKey);
    }

    void unfreezeReset() {
        this.frozen = false;
        clear();
    }

    public ResourceLocation getRegistryKey() {
        return registryKey;
    }

    void setFrozen(boolean value) {
        this.frozen = value;
    }
}
