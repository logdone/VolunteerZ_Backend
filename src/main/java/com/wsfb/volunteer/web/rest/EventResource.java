package com.wsfb.volunteer.web.rest;

import com.wsfb.volunteer.domain.Comment;
import com.wsfb.volunteer.domain.Event;
import com.wsfb.volunteer.domain.User;
import com.wsfb.volunteer.repository.EventRepository;
import com.wsfb.volunteer.repository.UserRepository;
import com.wsfb.volunteer.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * REST controller for managing {@link com.wsfb.volunteer.domain.Event}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class EventResource {

    private final Logger log = LoggerFactory.getLogger(EventResource.class);
    private final int REPORTS_MAX = 15;
    private static final String ENTITY_NAME = "event";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    @Autowired
    public  EventRepository eventRepository;
    @Autowired
    public  UserRepository userRepository;



    /**
     * {@code POST  /events} : Create a new event.
     *
     * @param event the event to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new event, or with status {@code 400 (Bad Request)} if the event has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/events")
    public ResponseEntity<Event> createEvent(@Valid @RequestBody Event event) throws URISyntaxException {
        if (event.getId() != null) {
            throw new BadRequestAlertException("A new event cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Event result = eventRepository.saveAndFlush(event);
        return ResponseEntity.created(new URI("/api/events/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /events} : Updates an existing event.
     *
     * @param event the event to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated event,
     * or with status {@code 400 (Bad Request)} if the event is not valid,
     * or with status {@code 500 (Internal Server Error)} if the event couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/events")
    public ResponseEntity<Event> updateEvent(@Valid @RequestBody Event event) throws URISyntaxException {
        System.out.println("***********************************************************");

        System.out.println("--  "+event.getComments().size()+"         --");
        System.out.println("--    "+event.getParticipants().size()+"        --");
        System.out.println("--    "+event.getReactions().size()+"     --");

        System.out.println("***********************************************************");
        if (event.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        System.out.println("+++++++++++++++++++++++++++++++++++++++++++++++++++++++"+event.getComments().size());
        Event result = eventRepository.save(event);
        eventRepository.flush();
        Event res = eventRepository.getOne(event.getId());


        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, event.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /events} : get all the events.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of events in body.
     */
    @GetMapping("/events")
    public ResponseEntity<List<Event>> getAllEvents(Pageable pageable) {
        Page<Event> page = eventRepository.findAll(pageable);
        for(Event e : page) {
        	 System.out.println("--  "+e.getComments().size()+"         --");
             System.out.println("--    "+e.getParticipants().size()+"        --");
             System.out.println("--    "+e.getReactions().size()+"     --");
             System.out.println("--    "+e.getEventReports().size()+"     --");
             for(Comment c :e.getComments()) {
                 System.out.println("--    "+c.getCommentReports().size()+"     --");
             }

        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /events/:id} : get the "id" event.
     *
     * @param id the id of the event to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the event, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/events/{id}")
    public ResponseEntity<Event> getEvent(@PathVariable Long id) {
        eventRepository.flush();
        Optional<Event> event = eventRepository.findById(id);
        
        System.out.println("----------------------------------------------------");
        System.out.println("--                                                --");
        System.out.println("--                                                --");
        System.out.println("--                  "+event.get().getComments().size()+"                             --");
        System.out.println("--                  "+event.get().getParticipants().size()+"                             --");
        System.out.println("--                  "+event.get().getReactions().size()+"                             --");

 
        System.out.println("--                                                --");
        System.out.println("----------------------------------------------------");

        return ResponseUtil.wrapOrNotFound(event);
    }
    

    
    @GetMapping("/events/participate/{id}/{userId}")
    public ResponseEntity<Event> participate(@PathVariable Long id,@PathVariable String userId) {
    	log.debug("Participating to event "+id+" The user is "+userId);
        Optional<Event> event = eventRepository.findById(id);
       User user = userRepository.findOneByLogin(userId).get();
       System.out.println("This is the user first name "+user.getFirstName());
       Set<User> participants = event.get().getParticipants();
       participants.add(user);
       event.get().setParticipants(participants);
       System.out.println("Event participants "+event.get().getParticipants());
       return ResponseUtil.wrapOrNotFound(event);

    }
    
    
    @GetMapping("/events/report/{id}/{userId}")
    public ResponseEntity<Event> report(@PathVariable Long id,@PathVariable String userId) {
    	log.debug("Participating to event "+id+" The user is "+userId);
    	
        Optional<Event> event = eventRepository.findById(id);
       System.out.println("Reports "+event.get().getEventReports().size());

       User user = userRepository.findOneByLogin(userId).get();

       System.out.println("This is the user first name "+user.getFirstName());
       Set<User> reports = event.get().getEventReports();
       if(!reports.contains(user)) {

       reports.add(user);
       event.get().setEventReports(reports);
       System.out.println("Event reprots "+event.get().getParticipants());
       if(reports.size()>REPORTS_MAX) {
    	   eventRepository.delete(event.get());
       }}
       return ResponseUtil.wrapOrNotFound(event);

    }
    /**
     * {@code DELETE  /events/:id} : delete the "id" event.
     *
     * @param id the id of the event to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/events/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable Long id) {
        log.debug("REST request to delete Event : {}", id);
        eventRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
