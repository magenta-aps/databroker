package dk.magenta.databroker.util.objectcontainers;

/**
 * Created by lars on 18-12-14.
 */
public class Pair<L,R> {

    private final L left;
    private final R right;

    public Pair(L left, R right) {
        this.left = left;
        this.right = right;
    }

    public L getLeft() { return left; }
    public R getRight() { return right; }

    @Override
    public int hashCode() { return left.hashCode() ^ right.hashCode(); }

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == null) return false;
        if (!(otherObject instanceof Pair)) return false;
        Pair otherPair = (Pair) otherObject;
        return this.left.equals(otherPair.getLeft()) && this.right.equals(otherPair.getRight());
    }
}

