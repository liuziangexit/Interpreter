package com.miwan.interpreter.util;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * @author liuziang
 * @contact liuziang@liuziangexit.com
 * @date 3/4/2020
 * <p>
 * 把多个Collection绑成一个Collection的工具
 * <p>
 * 虽然用addAll可以达成同样的效果，但是使用本类会大幅降低开销
 */

public class CollectionCombinator<T> implements Collection<T> {

	private ArrayList<Collection<T>> collections;

	private CollectionCombinator(ArrayList<Collection<T>> collections) {
		this.collections = collections;
	}

	@SafeVarargs
	static public <T> CollectionCombinator<T> createFrom(final Collection<T>... cs) {
		return new CollectionCombinator<>(new ArrayList<>(Arrays.asList(cs)));
	}

	@Override
	public int size() {
		int result = 0;
		for (Collection<T> c : collections) {
			result += c.size();
		}
		return result;
	}

	@Override
	public boolean isEmpty() {
		for (Collection<T> c : collections) {
			if (!c.isEmpty())
				return false;
		}
		return true;
	}

	@Override
	public boolean contains(Object o) {
		for (Collection<T> c : collections) {
			if (c.contains(o))
				return true;
		}
		return false;
	}

	@Override
	public Iterator<T> iterator() {
		final Iterator<Collection<T>> containerIt = collections.iterator();
		final Supplier<Iterator<T>> findNextIterator = () -> {
			while (containerIt.hasNext()) {
				Collection<T> current = containerIt.next();
				if (current.size() != 0) {
					return current.iterator();
				}
			}
			return null;
		};

		return new Iterator<T>() {
			Iterator<T> it = findNextIterator.get();

			@Override
			public boolean hasNext() {
				if (it == null)
					return false;
				if (it.hasNext())
					return true;
				return (it = findNextIterator.get()) != null;
			}

			@Override
			public T next() {
				return it.next();
			}
		};
	}

	@Override
	public void forEach(Consumer<? super T> action) {
		Iterator<T> iterator = this.iterator();
		while (iterator.hasNext()) {
			action.accept(iterator.next());
		}
	}

	@Override
	public Object[] toArray() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T1> T1[] toArray(T1[] a) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean add(T t) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return false;
	}

	@Override
	public boolean addAll(Collection<? extends T> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean removeIf(Predicate<? super T> filter) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clear() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		CollectionCombinator<?> that = (CollectionCombinator<?>) o;
		return that.collections.equals(this.collections);
	}

	@Override
	public int hashCode() {
		return this.collections.hashCode();
	}

	@Override
	public Spliterator<T> spliterator() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Stream<T> stream() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Stream<T> parallelStream() {
		throw new UnsupportedOperationException();
	}
}
