package dk.magenta.databroker.util.cache;

import dk.magenta.databroker.util.objectcontainers.Level3Container;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lars on 04-02-15.
 */
public class Level3Cache<T extends Cacheable> extends Level3Container<T> {
    private JpaRepository<T, Long> repository;
    private boolean loaded;

    public Level3Cache(JpaRepository<T, Long> repository) {
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
            try {
                String[] identifiers = item.getIdentifiers();
                this.put(identifiers[0], identifiers[1], identifiers[2], item);
            } catch (NullPointerException e) {
            } catch (IndexOutOfBoundsException e) {}
        }
        this.loaded = true;
    }

    public void put(T item) {
        this.load();
        try {
            String[] identifiers = item.getIdentifiers();
            this.put(identifiers[0], identifiers[1], identifiers[2], item);
        } catch (NullPointerException e) {}
    }

    public void reset() {
        this.clear();
        this.loaded = false;
    }

    public T get(String ident3, int ident2, int ident1) {
        return this.get(ident3, ""+ident2, ""+ident1);
    }
    public T get(String ident3, int ident2, String ident1) {
        return this.get(ident3, ""+ident2, ident1);
    }
    public T get(String ident3, String ident2, int ident1) {
        return this.get(ident3, ident2, ""+ident1);
    }
    public T get(int ident3, int ident2, int ident1) {
        return this.get(""+ident3, ""+ident2, ""+ident1);
    }
    public T get(int ident3, int ident2, String ident1) {
        return this.get(""+ident3, ""+ident2, ident1);
    }
    public T get(int ident3, String ident2, int ident1) {
        return this.get(""+ident3, ident2, ""+ident1);
    }

    public T get(String ident3, String ident2, String ident1) {
        this.load();
        return super.get(ident3, ident2, ident1);
    }
}
