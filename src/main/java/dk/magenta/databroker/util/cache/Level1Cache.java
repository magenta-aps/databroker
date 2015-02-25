package dk.magenta.databroker.util.cache;

import dk.magenta.databroker.util.objectcontainers.Level1Container;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lars on 04-02-15.
 */
public class Level1Cache<T extends Cacheable> extends Level1Container<T> {
    private JpaRepository<T, Long> repository;
    private boolean loaded;
    private Thread thread = null;

    public Level1Cache(JpaRepository<T, Long> repository) {
        this.repository = repository;
        this.loaded = false;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public void load() {
        if (!this.loaded) {
            this.reload(true);
        }
    }

    public void reload(boolean wait) {
        //System.out.println("Loading cache from " + master.repository);
        for (T item : this.repository.findAll()) {
            this.doPut(item);
        }
        //System.out.println("Cache loaded from " + master.repository);
        this.loaded = true;
    }

    public void put(T item) {
        this.load();
        this.doPut(item);
    }
    private synchronized void doPut(T item) {
        try {
            String[] identifiers = item.getIdentifiers();
            this.put(identifiers[0], item);
        } catch (NullPointerException e) {
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public void reset() {
        this.clear();
        this.loaded = false;
    }

    public T get(int ident1) {
        return this.get(""+ident1);
    }
    public T get(String ident1) {
        this.load();
        return super.get(ident1);
    }
}
