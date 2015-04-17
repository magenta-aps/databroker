package dk.magenta.databroker.core.model;

import dk.magenta.databroker.core.Session;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.util.TransactionCallback;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by lars on 09-04-15.
 */
public interface EntityRepositoryCustom<T,C> {
    public List<TransactionCallback> getBulkwireCallbacks();
    public Collection<T> search(SearchParameters parameters);
    public void clear();
    public HashSet<C> getKnownDescriptors();
    public T getByDescriptor(C descriptor);
    public T getByDescriptor(C descriptor, Session session);
    public void setUseCachedDescriptorList(boolean useCachedDescriptorList);
    public void addKnownDescriptor(C descriptor, boolean dbLoad);


    public long count(Session session);
    public List<T> findAll(Session session);
/*
    public void save(T item);
    public int count();
    public Collection<T> findAll();*/
}
