/*
 * Copyright 2013 gitblit.com.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package br.com.etank.cache;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.TreeMap;

import br.com.etank.ObjectContainer;

/**
 * @author edgardleal
 * @since 11/12/2013 11:05:36
 * 
 */
public class ObjectCache<T> {
	HashMap<String, ObjectContainer<T>> map = new HashMap<String, ObjectContainer<T>>();
	TreeMap<String, ObjectContainer<T>> sorted_map = null;
	int sizeToGarbageCollect = 8;
	/**
	 * taxa de redução apos o garbaging collect
	 */
	float garbageReduceTax = .8F;

	public ObjectCache(int maxSize) {
		super();
		this.sizeToGarbageCollect = maxSize;
	}

	public ObjectCache() {
		super();
		// TODO Auto-generated constructor stub
	}

	public void put(String key, T object) {
		map.put(key, new ObjectContainer<T>(object));
		if (map.size() == sizeToGarbageCollect)
			cleanup();
	}

	public void put(T object) {
		put(String.format("%d", object.hashCode()), object);
	}

	public T get(int hashCode) {
		return get(String.format("%d", hashCode));
	}

	public T get(String key) {
		ObjectContainer<T> obj = map.get(key);
		if (obj == null)
			return null;
		else
			return obj.getObject();
	}

	/**
	 * Remove old Objects
	 */
	public void cleanup() {
		sorted_map = new TreeMap<String, ObjectContainer<T>>(
				new ValuesComparator<T>(map));
		double finalItens = map.size() * garbageReduceTax;
		sorted_map.putAll(map);
		for (Entry<String, ObjectContainer<T>> c : sorted_map.entrySet()) {
			if (finalItens >= map.size())
				break;
			map.remove(c.getKey());
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		for (Entry<String, ObjectContainer<T>> e : map.entrySet())
			builder.append(e.getValue().getObject().toString()).append('\n');

		return builder.toString();
	}

	
	
	public int getSizeToGarbageCollect() {
		return sizeToGarbageCollect;
	}

	public void setSizeToGarbageCollect(int sizeToGarbageCollect) {
		this.sizeToGarbageCollect = sizeToGarbageCollect;
	}

	public float getGarbageReduceTax() {
		return garbageReduceTax;
	}

	public void setGarbageReduceTax(float garbageReduceTax) {
		this.garbageReduceTax = garbageReduceTax;
	}

	public static void main(String[] args) {
		ObjectCache<String> cache = new ObjectCache<String>();
		cache.put("Z", "Z");
		cache.put("E", "E");
		cache.put("G", "E");
		cache.put("H", "H");
		cache.put("J", "J");
		cache.put("A", "A");
		cache.put("I", "I");
		for (int i = 0; i < 50; i++) {
			cache.get("A");
		}
		System.out.println(String.format(" %s", cache.toString()));
		cache.put("T", "T");
		cache.get("A");
		cache.put("Y", "Y");
		cache.put("H");
		cache.put("N");

		System.out.println(String.format(" %s", cache.toString()));

	}
}



class ValuesComparator<T> implements Comparator<String> {

	HashMap<String, ObjectContainer<T>> map = new HashMap<String, ObjectContainer<T>>();

	public ValuesComparator(HashMap<String, ObjectContainer<T>> map) {
		super();
		this.map = map;
	}

	@Override
	public int compare(String o1, String o2) {

		return map.get(o1).compareTo(map.get(o2));
	}

}
