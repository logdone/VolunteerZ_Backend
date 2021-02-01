package com.wsfb.volunteer.repository;

import com.wsfb.volunteer.domain.Event;
import com.wsfb.volunteer.domain.Reaction;

import java.util.List;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Reaction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
	List<Reaction> findByEvent(Event event);
}
