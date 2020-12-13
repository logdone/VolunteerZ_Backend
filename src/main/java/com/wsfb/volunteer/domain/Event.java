package com.wsfb.volunteer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * A Event.
 */
@Entity
@Table(name = "event")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false, unique = true)
    private String title;

    @Size(max = 600)
    @Column(name = "event_description", length = 600)
    private String eventDescription;

    @Column(name = "event_image")
    private String eventImage;

    @Column(name = "category")
    private String category;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private ZonedDateTime creationDate;

    @NotNull
    @Column(name = "event_date", nullable = false)
    private ZonedDateTime eventDate;

    @Min(value = 0)
    @Column(name = "max_number_volunteers")
    private Integer maxNumberVolunteers;

    @Column(name = "nbr_reports")
    private Integer nbrReports;

    @Column(name = "link")
    private String link;

    @NotNull
    @Column(name = "location", nullable = false)
    private String location;

    @OneToMany(mappedBy = "event")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "event")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Reaction> reactions = new HashSet<>();

    @OneToMany(mappedBy = "event")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<User> participants = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "events", allowSetters = true)
    private User owner;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public Event title(String title) {
        this.title = title;
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public Event eventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
        return this;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public String getEventImage() {
        return eventImage;
    }

    public Event eventImage(String eventImage) {
        this.eventImage = eventImage;
        return this;
    }

    public void setEventImage(String eventImage) {
        this.eventImage = eventImage;
    }

    public String getCategory() {
        return category;
    }

    public Event category(String category) {
        this.category = category;
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public ZonedDateTime getCreationDate() {
        return creationDate;
    }

    public Event creationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(ZonedDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public ZonedDateTime getEventDate() {
        return eventDate;
    }

    public Event eventDate(ZonedDateTime eventDate) {
        this.eventDate = eventDate;
        return this;
    }

    public void setEventDate(ZonedDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public Integer getMaxNumberVolunteers() {
        return maxNumberVolunteers;
    }

    public Event maxNumberVolunteers(Integer maxNumberVolunteers) {
        this.maxNumberVolunteers = maxNumberVolunteers;
        return this;
    }

    public void setMaxNumberVolunteers(Integer maxNumberVolunteers) {
        this.maxNumberVolunteers = maxNumberVolunteers;
    }

    public Integer getNbrReports() {
        return nbrReports;
    }

    public Event nbrReports(Integer nbrReports) {
        this.nbrReports = nbrReports;
        return this;
    }

    public void setNbrReports(Integer nbrReports) {
        this.nbrReports = nbrReports;
    }

    public String getLink() {
        return link;
    }

    public Event link(String link) {
        this.link = link;
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLocation() {
        return location;
    }

    public Event location(String location) {
        this.location = location;
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Set<Comment> getComments() {
        return comments;
    }

    public Event comments(Set<Comment> comments) {
        this.comments = comments;
        return this;
    }

    public Event addComment(Comment comment) {
        this.comments.add(comment);
        comment.setEvent(this);
        return this;
    }

    public Event removeComment(Comment comment) {
        this.comments.remove(comment);
        comment.setEvent(null);
        return this;
    }

    public void setComments(Set<Comment> comments) {
        this.comments = comments;
    }

    public Set<Reaction> getReactions() {
        return reactions;
    }

    public Event reactions(Set<Reaction> reactions) {
        this.reactions = reactions;
        return this;
    }

    public Event addReaction(Reaction reaction) {
        this.reactions.add(reaction);
        reaction.setEvent(this);
        return this;
    }

    public Event removeReaction(Reaction reaction) {
        this.reactions.remove(reaction);
        reaction.setEvent(null);
        return this;
    }

    public void setReactions(Set<Reaction> reactions) {
        this.reactions = reactions;
    }

    public Set<User> getParticipants() {
        return participants;
    }

    public Event participants(Set<User> extendedUsers) {
        this.participants = extendedUsers;
        return this;
    }

    public Event addParticipants(User extendedUser) {
        this.participants.add(extendedUser);
        extendedUser.setEvent(this);
        return this;
    }

    public Event removeParticipants(User extendedUser) {
        this.participants.remove(extendedUser);
        extendedUser.setEvent(null);
        return this;
    }

    public void setParticipants(Set<User> extendedUsers) {
        this.participants = extendedUsers;
    }

    public User getOwner() {
        return owner;
    }

    public Event owner(User extendedUser) {
        this.owner = extendedUser;
        return this;
    }

    public void setOwner(User extendedUser) {
        this.owner = extendedUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Event)) {
            return false;
        }
        return id != null && id.equals(((Event) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Event{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", eventDescription='" + getEventDescription() + "'" +
            ", eventImage='" + getEventImage() + "'" +
            ", category='" + getCategory() + "'" +
            ", creationDate='" + getCreationDate() + "'" +
            ", eventDate='" + getEventDate() + "'" +
            ", maxNumberVolunteers=" + getMaxNumberVolunteers() +
            ", nbrReports=" + getNbrReports() +
            ", link='" + getLink() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
