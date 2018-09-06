package csv;

import java.util.Iterator;

public class MapAdapterIterator<I,O> implements Iterator<O> {

    private final Iterator<I> innerIter;
    private final Mapper<I,O> mapper;


    public MapAdapterIterator(Iterator<I> inputIterator, Mapper<I,O> mapper) {
        this.innerIter = inputIterator;
        this.mapper = mapper;
    }

    @Override
    public boolean hasNext() {
        return this.innerIter.hasNext();
    }

    @Override
    public O next() {
        I in = this.innerIter.next();
        return this.mapper.map(in);
    }
}
