package net.bluedash;

import java.util.*;

public class MultiValueMap<K, V> {

    private Map<K, Collection<V>> map;

    private Class<? extends Collection<V>> clazz;

    public MultiValueMap(Collection<V> coll) {
        map = new HashMap<K, Collection<V>>();
        this.clazz = (Class<? extends Collection<V>>) coll.getClass();
    }

    public void addValue(K key, V value) {
        Collection<V> collection = map.get(key);
        if (collection == null) {
            collection = createCollection();
            if (collection == null) return;
            map.put(key, collection);
        }
        collection.add(value);
    }

    public Set<K> keys() {
        return map.keySet();
    }

    public Collection<V> getValues(K key) {
        Collection<V> collection = map.get(key);
        if (collection == null)
            return Collections.emptySet();
        return collection;
    }

    private Collection<V> createCollection() {
        Collection<V> collection = null;
        try {
            collection = clazz.newInstance();
        } catch (InstantiationException ex) {
            // handling here
        } catch (IllegalAccessException ex) {
            // handling here
        }
        return collection;
    }


}
