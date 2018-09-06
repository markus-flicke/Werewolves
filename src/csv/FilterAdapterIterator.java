package csv;

import java.util.Arrays;
import java.util.Iterator;

public class FilterAdapterIterator<T> implements Iterator<T> {

    private final Iterator<T> innerIterator;
    private final FilterPredicate predicate;
    private T next;

    public FilterAdapterIterator(Iterator<T> input, FilterPredicate<T> predicate)
    {
        this.innerIterator = input;
        this.predicate = predicate;
        this.forward();
    }

    @Override
    public boolean hasNext() {
        return this.next != null;
    }

    @Override
    public T next() {
        T temp = this.next;
        this.forward();
        return temp;
    }

    private void forward() {

        while( innerIterator.hasNext() ) {
            T tmep = innerIterator.next();
            if (!predicate.test(tmep)) {
                this.next = tmep;
                return;
            }
        }
        this.next = null;
    }

    public static void main(String[] args) {
        new FilterAdapterIterator<>(Arrays.asList(1,2,3,4,5,6,7,8,9).iterator(), (Integer i) -> i % 2 == 1)
                .forEachRemaining(e -> System.out.println(e));
    }
}
