package dk.magenta.databroker.core.model;

import dk.magenta.databroker.core.Session;

import java.util.HashSet;
import java.util.UUID;

/**
 * Created by lars on 03-02-15.
 */
public abstract class EntityRepositoryImplementation<T,C> extends RepositoryImplementation {


    /*public T get(C identifier) {

    }*/

    public abstract T getByDescriptor(C descriptor);
    public abstract T getByDescriptor(C descriptor, Session session);

    protected String getRandomKey() {
        return "id_"+ UUID.randomUUID().toString().replace("-","");
    }


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

    /*public void addKnownDescriptor(C descriptor, boolean dbLoad) {
        if (this.getUseCachedDescriptorList() && descriptor != null) {
            if (this.knownDescriptors == null && dbLoad) {
                this.knownDescriptors = this.getKnownDescriptors();
            }
            if (this.knownDescriptors != null) {
                this.knownDescriptors.add(descriptor);
            }
        }
    }*/

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








/*
    public int count() {return 0;}

    public Collection<T> findAll() {
        return null;
    };*/
}
