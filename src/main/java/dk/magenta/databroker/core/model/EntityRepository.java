package dk.magenta.databroker.core.model;

import dk.magenta.databroker.core.Session;
import dk.magenta.databroker.dawa.model.SearchParameters;
import dk.magenta.databroker.util.TransactionCallback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * Created by lars on 09-04-15.
 */
@NoRepositoryBean
public interface EntityRepository<T,C> extends JpaRepository<T, Long> {
    public List<TransactionCallback> getBulkwireCallbacks();
    public Collection<T> search(SearchParameters parameters);
    public void clear();
    public HashSet<C> getKnownDescriptors();
    public T getByDescriptor(C descriptor);
    public T getByDescriptor(C descriptor, Session session);
    public void setUseCachedDescriptorList(boolean useCachedDescriptorList);
    //public void addKnownDescriptor(C descriptor, boolean dbLoad);

    public void save(Object item, Session session, boolean alreadyExists);
}
