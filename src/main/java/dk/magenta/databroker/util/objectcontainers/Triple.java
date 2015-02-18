package dk.magenta.databroker.util.objectcontainers;

/**
 * Created by lars on 18-12-14.
 */
public class Triple<L,C,R> {

    private final L left;
    private final C center;
    private final R right;

    public Triple(L left, C center, R right) {
        this.left = left;
        this.center = center;
        this.right = right;
    }

    public L getLeft() { return left; }
    public C getCenter() { return center; }
    public R getRight() { return right; }

    @Override
    public int hashCode() { return left.hashCode() ^ right.hashCode(); }

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == null) return false;
        if (!(otherObject instanceof Triple)) return false;
        Triple otherTriple = (Triple) otherObject;
        return this.left.equals(otherTriple.getLeft()) && this.center.equals(otherTriple.getCenter()) && this.right.equals(otherTriple.getRight());
    }
}

