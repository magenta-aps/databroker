package dk.magenta.databroker.util.cache;

import dk.magenta.databroker.util.objectcontainers.Level1Container;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lars on 04-02-15.
 */
public class Level1Cache<T extends Cacheable> extends Level1Container<T> {
    private JpaRepository<T, Long> repository;
    private boolean loaded;

    public Level1Cache(JpaRepository<T, Long> repository) {
        this.repository = repository;
        this.loaded = false;
    }

    public boolean isLoaded() {
        return this.loaded;
    }

    public void load() {
        if (!this.loaded) {
            this.reload();
        }
    }

    public void reload() {
        for (T item : this.repository.findAll()) {
            String[] identifiers = item.getIdentifiers();
            this.put(identifiers[0], item);
        }
        this.loaded = true;
    }

    public void put(T item) {
        this.load();
        String[] identifiers = item.getIdentifiers();
        this.put(identifiers[0], item);
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
