package com.wsfb.volunteer.web.rest;

import com.wsfb.volunteer.BackendApp;
import com.wsfb.volunteer.domain.Reaction;
import com.wsfb.volunteer.repository.ReactionRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ReactionResource} REST controller.
 */
@SpringBootTest(classes = BackendApp.class)
@AutoConfigureMockMvc
@WithMockUser
public class ReactionResourceIT {

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReactionMockMvc;

    private Reaction reaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reaction createEntity(EntityManager em) {
        Reaction reaction = new Reaction();
        return reaction;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reaction createUpdatedEntity(EntityManager em) {
        Reaction reaction = new Reaction();
        return reaction;
    }

    @BeforeEach
    public void initTest() {
        reaction = createEntity(em);
    }

    @Test
    @Transactional
    public void createReaction() throws Exception {
        int databaseSizeBeforeCreate = reactionRepository.findAll().size();
        // Create the Reaction
        restReactionMockMvc.perform(post("/api/reactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reaction)))
            .andExpect(status().isCreated());

        // Validate the Reaction in the database
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeCreate + 1);
        Reaction testReaction = reactionList.get(reactionList.size() - 1);
    }

    @Test
    @Transactional
    public void createReactionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = reactionRepository.findAll().size();

        // Create the Reaction with an existing ID
        reaction.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restReactionMockMvc.perform(post("/api/reactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reaction)))
            .andExpect(status().isBadRequest());

        // Validate the Reaction in the database
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllReactions() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        // Get all the reactionList
        restReactionMockMvc.perform(get("/api/reactions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reaction.getId().intValue())));
    }
    
    @Test
    @Transactional
    public void getReaction() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        // Get the reaction
        restReactionMockMvc.perform(get("/api/reactions/{id}", reaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reaction.getId().intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingReaction() throws Exception {
        // Get the reaction
        restReactionMockMvc.perform(get("/api/reactions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateReaction() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        int databaseSizeBeforeUpdate = reactionRepository.findAll().size();

        // Update the reaction
        Reaction updatedReaction = reactionRepository.findById(reaction.getId()).get();
        // Disconnect from session so that the updates on updatedReaction are not directly saved in db
        em.detach(updatedReaction);

        restReactionMockMvc.perform(put("/api/reactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedReaction)))
            .andExpect(status().isOk());

        // Validate the Reaction in the database
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeUpdate);
        Reaction testReaction = reactionList.get(reactionList.size() - 1);
    }

    @Test
    @Transactional
    public void updateNonExistingReaction() throws Exception {
        int databaseSizeBeforeUpdate = reactionRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReactionMockMvc.perform(put("/api/reactions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(reaction)))
            .andExpect(status().isBadRequest());

        // Validate the Reaction in the database
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteReaction() throws Exception {
        // Initialize the database
        reactionRepository.saveAndFlush(reaction);

        int databaseSizeBeforeDelete = reactionRepository.findAll().size();

        // Delete the reaction
        restReactionMockMvc.perform(delete("/api/reactions/{id}", reaction.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Reaction> reactionList = reactionRepository.findAll();
        assertThat(reactionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
