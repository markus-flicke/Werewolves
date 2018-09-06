package csv;

public interface FilterPredicate<T> {
    boolean test(T value);
}
