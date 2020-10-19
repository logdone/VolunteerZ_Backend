package com.wsfb.volunteer.web.rest;

import com.wsfb.volunteer.BackendApp;
import com.wsfb.volunteer.domain.Event;
import com.wsfb.volunteer.repository.EventRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.wsfb.volunteer.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link EventResource} REST controller.
 */
@SpringBootTest(classes = BackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class EventResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_EVENT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_EVENT_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_CATEGORY = "AAAAAAAAAA";
    private static final String UPDATED_CATEGORY = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATION_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATION_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_EVENT_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_EVENT_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Integer DEFAULT_MAX_NUMBER_VOLUNTEERS = 0;
    private static final Integer UPDATED_MAX_NUMBER_VOLUNTEERS = 1;

    private static final Integer DEFAULT_NBR_REPORTS = 1;
    private static final Integer UPDATED_NBR_REPORTS = 2;

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEventMockMvc;

    private Event event;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createEntity(EntityManager em) {
        Event event = new Event()
            .title(DEFAULT_TITLE)
            .eventDescription(DEFAULT_EVENT_DESCRIPTION)
            .eventImage(DEFAULT_EVENT_IMAGE)
            .category(DEFAULT_CATEGORY)
            .creationDate(DEFAULT_CREATION_DATE)
            .eventDate(DEFAULT_EVENT_DATE)
            .maxNumberVolunteers(DEFAULT_MAX_NUMBER_VOLUNTEERS)
            .nbrReports(DEFAULT_NBR_REPORTS)
            .link(DEFAULT_LINK)
            .location(DEFAULT_LOCATION);
        return event;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Event createUpdatedEntity(EntityManager em) {
        Event event = new Event()
            .title(UPDATED_TITLE)
            .eventDescription(UPDATED_EVENT_DESCRIPTION)
            .eventImage(UPDATED_EVENT_IMAGE)
            .category(UPDATED_CATEGORY)
            .creationDate(UPDATED_CREATION_DATE)
            .eventDate(UPDATED_EVENT_DATE)
            .maxNumberVolunteers(UPDATED_MAX_NUMBER_VOLUNTEERS)
            .nbrReports(UPDATED_NBR_REPORTS)
            .link(UPDATED_LINK)
            .location(UPDATED_LOCATION);
        return event;
    }

    @BeforeEach
    public void initTest() {
        event = createEntity(em);
    }

    @Test
    @Transactional
    public void createEvent() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();
        // Create the Event
        restEventMockMvc.perform(post("/api/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isCreated());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate + 1);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testEvent.getEventDescription()).isEqualTo(DEFAULT_EVENT_DESCRIPTION);
        assertThat(testEvent.getEventImage()).isEqualTo(DEFAULT_EVENT_IMAGE);
        assertThat(testEvent.getCategory()).isEqualTo(DEFAULT_CATEGORY);
        assertThat(testEvent.getCreationDate()).isEqualTo(DEFAULT_CREATION_DATE);
        assertThat(testEvent.getEventDate()).isEqualTo(DEFAULT_EVENT_DATE);
        assertThat(testEvent.getMaxNumberVolunteers()).isEqualTo(DEFAULT_MAX_NUMBER_VOLUNTEERS);
        assertThat(testEvent.getNbrReports()).isEqualTo(DEFAULT_NBR_REPORTS);
        assertThat(testEvent.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testEvent.getLocation()).isEqualTo(DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    public void createEventWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = eventRepository.findAll().size();

        // Create the Event with an existing ID
        event.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEventMockMvc.perform(post("/api/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setTitle(null);

        // Create the Event, which fails.


        restEventMockMvc.perform(post("/api/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCreationDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setCreationDate(null);

        // Create the Event, which fails.


        restEventMockMvc.perform(post("/api/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkEventDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setEventDate(null);

        // Create the Event, which fails.


        restEventMockMvc.perform(post("/api/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLocationIsRequired() throws Exception {
        int databaseSizeBeforeTest = eventRepository.findAll().size();
        // set the field null
        event.setLocation(null);

        // Create the Event, which fails.


        restEventMockMvc.perform(post("/api/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllEvents() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get all the eventList
        restEventMockMvc.perform(get("/api/events?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(event.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].eventDescription").value(hasItem(DEFAULT_EVENT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].eventImage").value(hasItem(DEFAULT_EVENT_IMAGE)))
            .andExpect(jsonPath("$.[*].category").value(hasItem(DEFAULT_CATEGORY)))
            .andExpect(jsonPath("$.[*].creationDate").value(hasItem(sameInstant(DEFAULT_CREATION_DATE))))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(sameInstant(DEFAULT_EVENT_DATE))))
            .andExpect(jsonPath("$.[*].maxNumberVolunteers").value(hasItem(DEFAULT_MAX_NUMBER_VOLUNTEERS)))
            .andExpect(jsonPath("$.[*].nbrReports").value(hasItem(DEFAULT_NBR_REPORTS)))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)));
    }
    
    @Test
    @Transactional
    public void getEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        // Get the event
        restEventMockMvc.perform(get("/api/events/{id}", event.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(event.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.eventDescription").value(DEFAULT_EVENT_DESCRIPTION))
            .andExpect(jsonPath("$.eventImage").value(DEFAULT_EVENT_IMAGE))
            .andExpect(jsonPath("$.category").value(DEFAULT_CATEGORY))
            .andExpect(jsonPath("$.creationDate").value(sameInstant(DEFAULT_CREATION_DATE)))
            .andExpect(jsonPath("$.eventDate").value(sameInstant(DEFAULT_EVENT_DATE)))
            .andExpect(jsonPath("$.maxNumberVolunteers").value(DEFAULT_MAX_NUMBER_VOLUNTEERS))
            .andExpect(jsonPath("$.nbrReports").value(DEFAULT_NBR_REPORTS))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION));
    }
    @Test
    @Transactional
    public void getNonExistingEvent() throws Exception {
        // Get the event
        restEventMockMvc.perform(get("/api/events/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // Update the event
        Event updatedEvent = eventRepository.findById(event.getId()).get();
        // Disconnect from session so that the updates on updatedEvent are not directly saved in db
        em.detach(updatedEvent);
        updatedEvent
            .title(UPDATED_TITLE)
            .eventDescription(UPDATED_EVENT_DESCRIPTION)
            .eventImage(UPDATED_EVENT_IMAGE)
            .category(UPDATED_CATEGORY)
            .creationDate(UPDATED_CREATION_DATE)
            .eventDate(UPDATED_EVENT_DATE)
            .maxNumberVolunteers(UPDATED_MAX_NUMBER_VOLUNTEERS)
            .nbrReports(UPDATED_NBR_REPORTS)
            .link(UPDATED_LINK)
            .location(UPDATED_LOCATION);

        restEventMockMvc.perform(put("/api/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedEvent)))
            .andExpect(status().isOk());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
        Event testEvent = eventList.get(eventList.size() - 1);
        assertThat(testEvent.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testEvent.getEventDescription()).isEqualTo(UPDATED_EVENT_DESCRIPTION);
        assertThat(testEvent.getEventImage()).isEqualTo(UPDATED_EVENT_IMAGE);
        assertThat(testEvent.getCategory()).isEqualTo(UPDATED_CATEGORY);
        assertThat(testEvent.getCreationDate()).isEqualTo(UPDATED_CREATION_DATE);
        assertThat(testEvent.getEventDate()).isEqualTo(UPDATED_EVENT_DATE);
        assertThat(testEvent.getMaxNumberVolunteers()).isEqualTo(UPDATED_MAX_NUMBER_VOLUNTEERS);
        assertThat(testEvent.getNbrReports()).isEqualTo(UPDATED_NBR_REPORTS);
        assertThat(testEvent.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testEvent.getLocation()).isEqualTo(UPDATED_LOCATION);
    }

    @Test
    @Transactional
    public void updateNonExistingEvent() throws Exception {
        int databaseSizeBeforeUpdate = eventRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEventMockMvc.perform(put("/api/events")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(event)))
            .andExpect(status().isBadRequest());

        // Validate the Event in the database
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteEvent() throws Exception {
        // Initialize the database
        eventRepository.saveAndFlush(event);

        int databaseSizeBeforeDelete = eventRepository.findAll().size();

        // Delete the event
        restEventMockMvc.perform(delete("/api/events/{id}", event.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Event> eventList = eventRepository.findAll();
        assertThat(eventList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
