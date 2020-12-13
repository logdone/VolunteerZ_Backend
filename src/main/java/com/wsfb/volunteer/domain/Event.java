package com.wsfb.volunteer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
    @Column(name = "location", nullable = false )
    private String location;

    @OneToMany(mappedBy = "event", fetch = FetchType.LAZY)
    private Set<Comment> comments = new HashSet<>();

    @OneToMany(mappedBy = "event" , fetch = FetchType.LAZY)
    private Set<Reaction> reactions = new HashSet<>();

    @ManyToMany
    @JoinTable(
      name = "event_participants", 
      joinColumns = @JoinColumn(name = "event_id"), 
      inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> participants = new HashSet<>();

    @ManyToOne
    private User owner;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public String getEventImage() {
		return eventImage;
	}

	public void setEventImage(String eventImage) {
		this.eventImage = eventImage;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public ZonedDateTime getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(ZonedDateTime creationDate) {
		this.creationDate = creationDate;
	}

	public ZonedDateTime getEventDate() {
		return eventDate;
	}

	public void setEventDate(ZonedDateTime eventDate) {
		this.eventDate = eventDate;
	}

	public Integer getMaxNumberVolunteers() {
		return maxNumberVolunteers;
	}

	public void setMaxNumberVolunteers(Integer maxNumberVolunteers) {
		this.maxNumberVolunteers = maxNumberVolunteers;
	}

	public Integer getNbrReports() {
		return nbrReports;
	}

	public void setNbrReports(Integer nbrReports) {
		this.nbrReports = nbrReports;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Set<Comment> getComments() {
		return comments;
	}

	public void setComments(Set<Comment> comments) {
		this.comments = comments;
	}

	public Set<Reaction> getReactions() {
		return reactions;
	}

	public void setReactions(Set<Reaction> reactions) {
		this.reactions = reactions;
	}

	public Set<User> getParticipants() {
		return participants;
	}

	public void setParticipants(Set<User> participants) {
		this.participants = participants;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", title=" + title + ", eventDescription=" + eventDescription + ", eventImage="
				+ eventImage + ", category=" + category + ", creationDate=" + creationDate + ", eventDate=" + eventDate
				+ ", maxNumberVolunteers=" + maxNumberVolunteers + ", nbrReports=" + nbrReports + ", link=" + link
				+ ", location=" + location + ", owner=" + owner + "]";
	}

	

}
