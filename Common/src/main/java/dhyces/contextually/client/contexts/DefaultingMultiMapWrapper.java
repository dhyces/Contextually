package dhyces.contextually.client.contexts;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.Collections;

public class DefaultingMultiMapWrapper<K, V> {

    final Multimap<K, V> map;
    final Collection<V> defaultValue;

    public DefaultingMultiMapWrapper(Multimap<K, V> multimap, Collection<V> defaultValue) {
        this.map = multimap;
        this.defaultValue = defaultValue;
    }

    public static <K, V> DefaultingMultiMapWrapper<K, V> createArrayListMultiMap(Collection<V> defaultValue) {
        return new DefaultingMultiMapWrapper<>(ArrayListMultimap.create(), defaultValue);
    }

    public boolean isDefault(Collection<V> collection) {
        return defaultValue.equals(collection) || collection.containsAll(defaultValue);
    }

    public Collection<V> getDefaultValue() {
        return Collections.unmodifiableCollection(defaultValue);
    }

    public boolean containsKey(K value) {
        return map.containsKey(value);
    }

    public boolean put(K key, V value) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        map.clear();
    }

    public Collection<V> get(K key) {
        var ret = map.get(key);
        if (ret.isEmpty()) {
            ret = defaultValue;
        }
        return Collections.unmodifiableCollection(ret);
    }
}
