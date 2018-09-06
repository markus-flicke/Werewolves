package csv;

public interface Mapper<I,O> {
    O map(I in);
}
