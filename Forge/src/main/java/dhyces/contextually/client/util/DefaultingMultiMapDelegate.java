package dhyces.contextually.client.util;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import java.util.Collection;
import java.util.Collections;

public class DefaultingMultiMapDelegate<K, V> {

    final Multimap<K, V> map;
    final Collection<V> defaultValue;

    public DefaultingMultiMapDelegate(Multimap<K, V> multimap, Collection<V> defaultValue) {
        this.map = multimap;
        this.defaultValue = defaultValue;
    }

    public static <K, V> DefaultingMultiMapDelegate<K, V> createArrayListMultiMap(Collection<V> defaultValue) {
        return new DefaultingMultiMapDelegate<>(ArrayListMultimap.create(), defaultValue);
    }

    public boolean isDefault(Collection<V> collection) {
        return defaultValue.equals(collection);
    }

    public Collection<V> getDefaultValue() {
        return Collections.unmodifiableCollection(defaultValue);
    }

    public boolean put(K key, V value) {
        return map.put(key, value);
    }

    public Collection<V> get(K key) {
        var ret = map.get(key);
        if (ret.isEmpty()) {
            ret = defaultValue;
        }
        return ret;
    }
}
