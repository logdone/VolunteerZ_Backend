package com.wsfb.volunteer.domain;

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

    @ManyToMany
    @JoinTable(
    		  name = "comment_reports", 
    		  joinColumns = @JoinColumn(name = "comment_id"),
    		  inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> commentReports = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = "comments", allowSetters = true)
    private Event event;

    @ManyToOne
    @JsonIgnoreProperties(value = "owner", allowSetters = true)
    private User user;

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

    public Comment commentBody(String commentBody) {
        this.commentBody = commentBody;
        return this;
    }

    public void setCommentBody(String commentBody) {
        this.commentBody = commentBody;
    }

    public Event getEvent() {
        return event;
    }

    public Comment event(Event event) {
        this.event = event;
        return this;
    }

    public void setEvent(Event event) {
        this.event = event;
    }




    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Comment)) {
            return false;
        }
        return id != null && id.equals(((Comment) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

	public Set<User> getCommentReports() {
		return commentReports;
	}

	public void setCommentReports(Set<User> commentReports) {
		this.commentReports = commentReports;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Comment [id=" + id + ", commentBody=" + commentBody + ", commentReports=" + commentReports + 
				 ", user=" + user + "]";
	}

	

}
