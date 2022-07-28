package dhyces.contextually.client.contexts;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import dhyces.contextually.client.contexts.objects.IKeyContext;
import net.minecraft.resources.ResourceLocation;

import java.util.Collection;

public class FreezingDefaultingMapContextRegistry<K, V> {

    private boolean frozen;
    final DefaultingMultiMapWrapper<K, IKeyContext<V>> delegate;
    final ResourceLocation registryKey;

    public FreezingDefaultingMapContextRegistry(ResourceLocation key, Multimap<K, IKeyContext<V>> multimap, Collection<IKeyContext<V>> defaultValue) {
        this(key, new DefaultingMultiMapWrapper<>(multimap, defaultValue));
    }

    public FreezingDefaultingMapContextRegistry(ResourceLocation key, DefaultingMultiMapWrapper<K, IKeyContext<V>> defaultingMultiMapWrapper) {
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

    public boolean put(K key, IKeyContext<V> value) {
        checkFrozen();
        return delegate.put(key, value);
    }

    public boolean addDefault(IKeyContext<V> value) {
        checkFrozen();
        return delegate.addDefault(value);
    }

    public Collection<IKeyContext<V>> get(K key) {
        return delegate.get(key);
    }

    public boolean containsKey(K value) {
        return delegate.containsKey(value);
    }

    public Collection<IKeyContext<V>> getDefaultValue() {
        return delegate.getDefaultValue();
    }

    public boolean isDefault(Collection<IKeyContext<V>> collection) {
        return delegate.isDefault(collection);
    }

    public void clear() {
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
