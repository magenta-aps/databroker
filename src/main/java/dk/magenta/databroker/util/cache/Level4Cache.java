package dk.magenta.databroker.util.cache;

import dk.magenta.databroker.util.objectcontainers.Level4Container;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by lars on 04-02-15.
 */
public class Level4Cache<T extends Cacheable> extends Level4Container<T> {
    private JpaRepository<T, Long> repository;
    private boolean loaded;
    private Thread thread = null;

    public Level4Cache(JpaRepository<T, Long> repository) {
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
        //if (this.thread == null || this.thread.getState() == Thread.State.TERMINATED) {
        //    this.thread = new Thread() {
         //       public void run() {
            //           Level4Cache<T> master = Level4Cache.this;
                       Level4Cache<T> master = this;
                    System.out.println("Loading cache from " + master.repository);
                    for (T item : master.repository.findAll()) {
                        master.doPut(item);
                    }
                    System.out.println("Cache loaded from " + master.repository);
                    master.loaded = true;
        //        }
        //    };
        //    this.thread.start();
        //}

        //if (wait && this.thread != null && this.thread.getState() != Thread.State.TERMINATED) {
        //    try {
        //        this.thread.join();
        //    } catch (InterruptedException e) {
        //        e.printStackTrace();
        //    }
        //}
    }

    public void put(T item) {
        this.load();
        this.doPut(item);
    }
    private synchronized void doPut(T item) {
        try {
            String[] identifiers = item.getIdentifiers();
            this.put(identifiers[0], identifiers[1], identifiers[2], identifiers[3], item);
        } catch (NullPointerException e) {
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public void reset() {
        System.out.println("Clearing cache for "+this.repository);
        this.clear();
        this.loaded = false;
    }

    public T get(String ident4, String ident3, int ident2, int ident1) {
        return this.get(ident4, ident3, ""+ident2, ""+ident1);
    }
    public T get(String ident4, String ident3, int ident2, String ident1) {
        return this.get(ident4, ident3, ""+ident2, ident1);
    }
    public T get(String ident4, String ident3, String ident2, int ident1) {
        return this.get(ident4, ident3, ident2, ""+ident1);
    }
    public T get(String ident4, int ident3, int ident2, int ident1) {
        return this.get(ident4, ""+ident3, ""+ident2, ""+ident1);
    }
    public T get(String ident4, int ident3, int ident2, String ident1) {
        return this.get(ident4, ""+ident3, ""+ident2, ident1);
    }
    public T get(String ident4, int ident3, String ident2, int ident1) {
        return this.get(ident4, ""+ident3, ident2, ""+ident1);
    }

    public T get(int ident4, String ident3, int ident2, int ident1) {
        return this.get(""+ident4, ident3, ""+ident2, ""+ident1);
    }
    public T get(int ident4, String ident3, int ident2, String ident1) {
        return this.get(""+ident4, ident3, ""+ident2, ident1);
    }
    public T get(int ident4, String ident3, String ident2, int ident1) {
        return this.get(""+ident4, ident3, ident2, ""+ident1);
    }
    public T get(int ident4, int ident3, int ident2, int ident1) {
        return this.get(""+ident4, ""+ident3, ""+ident2, ""+ident1);
    }
    public T get(int ident4, int ident3, int ident2, String ident1) {
        return this.get(""+ident4, ""+ident3, ""+ident2, ident1);
    }
    public T get(int ident4, int ident3, String ident2, int ident1) {
        return this.get(""+ident4, ""+ident3, ident2, ""+ident1);
    }

    public T get(String ident4, String ident3, String ident2, String ident1) {
        this.load();
        return super.get(ident4, ident3, ident2, ident1);
    }
}
