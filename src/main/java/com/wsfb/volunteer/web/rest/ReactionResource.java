package com.wsfb.volunteer.web.rest;

import com.wsfb.volunteer.domain.Reaction;
import com.wsfb.volunteer.repository.EventRepository;
import com.wsfb.volunteer.repository.ReactionRepository;
import com.wsfb.volunteer.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.wsfb.volunteer.domain.Reaction}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ReactionResource {

    private final Logger log = LoggerFactory.getLogger(ReactionResource.class);

    private static final String ENTITY_NAME = "reaction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ReactionRepository reactionRepository;
    private final EventRepository eventRepository;

    public ReactionResource(ReactionRepository reactionRepository,EventRepository eventRepository) {
        this.reactionRepository = reactionRepository;
		this.eventRepository = eventRepository;
    }

    /**
     * {@code POST  /reactions} : Create a new reaction.
     *
     * @param reaction the reaction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new reaction, or with status {@code 400 (Bad Request)} if the reaction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/reactions")
    public ResponseEntity<Reaction> createReaction(@RequestBody Reaction reaction) throws URISyntaxException {
        log.debug("REST request to save Reaction : {}", reaction);
        if (reaction.getId() != null) {
            throw new BadRequestAlertException("A new reaction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Reaction result = reactionRepository.save(reaction);
        return ResponseEntity.created(new URI("/api/reactions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /reactions} : Updates an existing reaction.
     *
     * @param reaction the reaction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated reaction,
     * or with status {@code 400 (Bad Request)} if the reaction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the reaction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/reactions")
    public ResponseEntity<Reaction> updateReaction(@RequestBody Reaction reaction) throws URISyntaxException {
        log.debug("REST request to update Reaction : {}", reaction);
        if (reaction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Reaction result = reactionRepository.save(reaction);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, reaction.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /reactions} : get all the reactions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of reactions in body.
     */
    @GetMapping("/reactions")
    public ResponseEntity<List<Reaction>> getAllReactions(Pageable pageable) {
        log.debug("REST request to get a page of Reactions");
        Page<Reaction> page = reactionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /reactions/:id} : get the "id" reaction.
     *
     * @param id the id of the reaction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the reaction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/reactions/{id}")
    public ResponseEntity<Reaction> getReaction(@PathVariable Long id) {
        log.debug("REST request to get Reaction : {}", id);
        Optional<Reaction> reaction = reactionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(reaction);
    }

    @GetMapping("/reactions/event/{id}")
    public ResponseEntity<List<Reaction>> getReactionByEvent(@PathVariable Long id) {
        log.debug("REST request to get Reaction : {}", id);
        List<Reaction> reaction = reactionRepository.findByEvent(eventRepository.findById(id).get());
        return ResponseEntity.ok().body(reaction);
    }
    /**
     * {@code DELETE  /reactions/:id} : delete the "id" reaction.
     *
     * @param id the id of the reaction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/reactions/{id}")
    public ResponseEntity<Void> deleteReaction(@PathVariable Long id) {
        log.debug("REST request to delete Reaction : {}", id);
        reactionRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
