package com.wsfb.volunteer.domain;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;



@Entity
@Table(name = "time_line")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TimeLine implements Serializable {

    private static final long serialVersionUID = 1L;


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
	private String action;
	private String targetObject ;
	private Instant time;
	
    @ManyToOne
    @JoinColumn(name="owner", nullable=false)
    @JsonIgnoreProperties({"comments","reactions","timeLine"})
    private User owner;
    
    
    
    public TimeLine() {}

	public TimeLine(String action, String to, User timeline_owner) {
		super();
		this.action = action;
		this.targetObject = to;
		this.owner = timeline_owner;
		this.time = Instant.now();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getTo() {
		return targetObject;
	}

	public void setTo(String to) {
		this.targetObject = to;
	}

	public Instant getTime() {
		return time;
	}

	public void setTime(Instant time) {
		this.time = time;
	}

	public User getUser() {
		return owner;
	}

	public void setUser(User timeline_owner) {
		this.owner = timeline_owner;
	}
	


    
    
    
    
    
}
