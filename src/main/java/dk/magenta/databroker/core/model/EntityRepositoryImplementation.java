package dk.magenta.databroker.core.model;

import dk.magenta.databroker.core.Session;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

/**
 * Created by lars on 03-02-15.
 */
public abstract class EntityRepositoryImplementation<T,C> extends RepositoryImplementation {

    public abstract T getByDescriptor(C descriptor);
    public abstract T getByDescriptor(C descriptor, Session session);

    public abstract HashSet<C> getKnownDescriptors();

    protected HashSet<C> knownDescriptors = null;

    protected boolean hasKnownDescriptor(C descriptor, boolean dbLoad) {
        if (this.getUseCachedDescriptorList()) {
            if (this.knownDescriptors == null && dbLoad) {
                this.knownDescriptors = this.getKnownDescriptors();
            }
            return (this.knownDescriptors != null && this.knownDescriptors.size() > 0 && this.knownDescriptors.contains(descriptor));
        }
        return true;
    }

    public void addKnownDescriptor(C descriptor, boolean dbLoad) {
        if (this.getUseCachedDescriptorList() && descriptor != null) {
            if (this.knownDescriptors == null && dbLoad) {
                this.knownDescriptors = this.getKnownDescriptors();
            }
            if (this.knownDescriptors != null) {
                this.knownDescriptors.add(descriptor);
            }
        }
    }

    protected String getRandomKey() {
        return "id_"+ UUID.randomUUID().toString().replace("-","");
    }

    private boolean useCachedDescriptorList;

    public void setUseCachedDescriptorList(boolean useCachedDescriptorList) {
        this.useCachedDescriptorList = useCachedDescriptorList;
    }

    public boolean getUseCachedDescriptorList() {
        return useCachedDescriptorList;
    }


    //public void save(T item) {
    //    System.out.println("Save item");
        /*if (item!= null && session != null) {
            session.save(item);
        }*/
    //}

    public void save(Object item, Session session, boolean alreadyExists) {
        if (item != null) {
            if (session == null) {
                this.entityManager.persist(item);
            } else {
                session.save(item, alreadyExists);
            }
        }
    }



    public abstract long count(Session session);

    public abstract List<T> findAll(Session session);
}
