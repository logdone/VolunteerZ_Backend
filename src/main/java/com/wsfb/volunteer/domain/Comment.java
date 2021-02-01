package com.wsfb.volunteer.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Comment.
 */
@Entity
@Table(name = "comment")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 200)
    @Column(name = "comment_body", length = 200, nullable = false)
    private String commentBody;

    @ManyToOne
    @JoinColumn(name="event_id", nullable=false)
    @JsonIgnoreProperties({"comments","reactions"})
    private Event event;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    @JsonIgnoreProperties("comments")
    private User user;

    @ManyToMany
    @JoinTable(
    		  name = "comment_reports", 
    		  joinColumns = @JoinColumn(name = "comment_id"),
    		  inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> commentReport = new HashSet<>();
    
    
    public Comment() {}
    
    
    
    
    
    public Comment(Long id,  String commentBody, Event event, User user) {
		super();
		this.id = id;
		this.commentBody = commentBody;
		this.event = event;
		this.user = user;
	}

	// jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCommentBody() {
        return commentBody;
    }


    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public Event getEvent() {
        return event;
    }



    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User extendedUser) {
        this.user = extendedUser;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

	public Set<User> getReports() {
		return commentReport;
	}

	public void setReports(Set<User> reports) {
		this.commentReport = reports;
	}

}
