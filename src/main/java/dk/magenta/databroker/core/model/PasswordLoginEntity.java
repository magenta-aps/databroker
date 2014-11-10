package dk.magenta.databroker.core.model;

import javax.persistence.*;

/**
 * Created by jubk on 07-11-2014.
 */
@Entity
@Table(name = "PasswordLogin", schema = "", catalog = "DataBrokerCore")
public class PasswordLoginEntity {
    private Integer id;
    private String login;
    private String secret;
    private Integer consumerId;
    private ConsumerEntity consumer;

    @Id
    @Column(name = "PasswordLoginId", nullable = false, insertable = true, updatable = true)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "Login", nullable = false, insertable = true, updatable = true, length = 255)
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Basic
    @Column(name = "Secret", nullable = false, insertable = true, updatable = true, length = 34)
    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PasswordLoginEntity that = (PasswordLoginEntity) o;

        if (consumerId != null ? !consumerId.equals(that.consumerId) : that.consumerId != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (login != null ? !login.equals(that.login) : that.login != null) return false;
        if (secret != null ? !secret.equals(that.secret) : that.secret != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (secret != null ? secret.hashCode() : 0);
        result = 31 * result + (consumerId != null ? consumerId.hashCode() : 0);
        return result;
    }

    @ManyToOne
    @JoinColumn(name = "ConsumerId", referencedColumnName = "ConsumerId", nullable = false)
    public ConsumerEntity getConsumer() {
        return consumer;
    }

    public void setConsumer(ConsumerEntity consumer) {
        this.consumer = consumer;
    }
}
