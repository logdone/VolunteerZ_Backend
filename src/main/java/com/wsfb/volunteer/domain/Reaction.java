package com.wsfb.volunteer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Reaction.
 */
@Entity
@Table(name = "reaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Reaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    
    //fuck this shit im out 
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="event_id", nullable=false)
    @JsonIgnoreProperties({"comments","reactions"})
    private Event event;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    @JsonIgnoreProperties({"comments","reactions"})
    private User user;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public Reaction event(Event event) {
        this.event = event;
        return this;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public Reaction user(User extendedUser) {
        this.user = extendedUser;
        return this;
    }

    public void setUser(User extendedUser) {
        this.user = extendedUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Reaction)) {
            return false;
        }
        return id != null && id.equals(((Reaction) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Reaction{" +
            "id=" + getId() +
            "}";
    }
}
