package com.googlecode.mp4parser.util;

import java.lang.Comparable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class RangeStartMap<K extends Comparable, V> implements Map<K, V> {
    TreeMap<K, V> base = new TreeMap<>(new Comparator<K>() {
        public int compare(K k, K k2) {
            return -k.compareTo(k2);
        }
    });

    public boolean containsValue(Object obj) {
        return false;
    }

    public RangeStartMap() {
    }

    public RangeStartMap(K k, V v) {
        put(k, v);
    }

    public int size() {
        return this.base.size();
    }

    public boolean isEmpty() {
        return this.base.isEmpty();
    }

    public boolean containsKey(Object obj) {
        return this.base.get(obj) != null;
    }

    public V get(Object obj) {
        if (!(obj instanceof Comparable)) {
            return null;
        }
        Comparable comparable = (Comparable) obj;
        if (isEmpty()) {
            return null;
        }
        Iterator<K> it = this.base.keySet().iterator();
        K next = it.next();
        while (true) {
            Comparable comparable2 = (Comparable) next;
            if (!it.hasNext()) {
                return this.base.get(comparable2);
            }
            if (comparable.compareTo(comparable2) >= 0) {
                return this.base.get(comparable2);
            }
            next = it.next();
        }
    }

    public V put(K k, V v) {
        return this.base.put(k, v);
    }

    public V remove(Object obj) {
        if (!(obj instanceof Comparable)) {
            return null;
        }
        Comparable comparable = (Comparable) obj;
        if (isEmpty()) {
            return null;
        }
        Iterator<K> it = this.base.keySet().iterator();
        K next = it.next();
        while (true) {
            Comparable comparable2 = (Comparable) next;
            if (!it.hasNext()) {
                return this.base.remove(comparable2);
            }
            if (comparable.compareTo(comparable2) >= 0) {
                return this.base.remove(comparable2);
            }
            next = it.next();
        }
    }

    public void putAll(Map<? extends K, ? extends V> map) {
        this.base.putAll(map);
    }

    public void clear() {
        this.base.clear();
    }

    public Set<K> keySet() {
        return this.base.keySet();
    }

    public Collection<V> values() {
        return this.base.values();
    }

    public Set<Map.Entry<K, V>> entrySet() {
        return this.base.entrySet();
    }
}
