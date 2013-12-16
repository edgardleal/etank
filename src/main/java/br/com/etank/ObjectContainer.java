/**
 * 
 */
package br.com.etank;

import java.util.Date;

/**
 * @author edgardleal
 * @since 11/12/2013 11:03:10
 * 
 */
public class ObjectContainer<T> implements Comparable<ObjectContainer<T>> {
	private static final int ONE_DAY = 10;// 86400000;
	private T object;
	private long timesRequired = 0L;
	private long lastTimeRequired = 0L;

	public ObjectContainer(T object) {
		super();
		this.object = object;
	}

	public long getTimesRequired() {
		return timesRequired;
	}

	public long getLastTimeRequired() {
		return lastTimeRequired;
	}

	public T getObject() {
		lastTimeRequired = new Date().getTime();
		timesRequired++;
		return object;
	}

	/**
	 * Ordena decrescente
	 */
	@Override
	public int compareTo(ObjectContainer<T> obj) {
		if (getTimesRequired() > obj.getTimesRequired())
			return 1;
		else if (getTimesRequired() < obj.getTimesRequired())
			return -1;
		else
			return 0;
	}
}
