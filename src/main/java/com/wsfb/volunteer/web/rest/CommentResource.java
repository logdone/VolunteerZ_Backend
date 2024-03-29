package com.wsfb.volunteer.web.rest;

import com.wsfb.volunteer.domain.Comment;
import com.wsfb.volunteer.domain.Event;
import com.wsfb.volunteer.domain.TimeLine;
import com.wsfb.volunteer.domain.User;
import com.wsfb.volunteer.repository.CommentRepository;
import com.wsfb.volunteer.repository.TimeLineRepository;
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
 * REST controller for managing {@link com.wsfb.volunteer.domain.Comment}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CommentResource {

    private final Logger log = LoggerFactory.getLogger(CommentResource.class);

    private static final String ENTITY_NAME = "comment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;
    private final int REPORTS_MAX = 5;

    private final CommentRepository commentRepository;
    
    private final  TimeLineRepository timeLineRepository;
    
    private final  UserRepository userRepository;
    
    public CommentResource(CommentRepository commentRepository,UserRepository userRepository,TimeLineRepository timeLineRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.timeLineRepository = timeLineRepository;
    }

    /**
     * {@code POST  /comments} : Create a new comment.
     *
     * @param comment the comment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new comment, or with status {@code 400 (Bad Request)} if the comment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comments")
    public ResponseEntity<Comment> createComment(@Valid @RequestBody Comment comment) throws URISyntaxException {
        log.debug("REST request to save Comment : {}", comment);
        if (comment.getId() != null) {
            throw new BadRequestAlertException("A new comment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Comment result = commentRepository.save(comment);
        TimeLine t = new TimeLine("Commented ", comment.getCommentBody(), comment.getUser());
        timeLineRepository.saveAndFlush(t);
        return ResponseEntity.created(new URI("/api/comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /comments} : Updates an existing comment.
     *
     * @param comment the comment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated comment,
     * or with status {@code 400 (Bad Request)} if the comment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the comment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comments")
    public ResponseEntity<Comment> updateComment(@Valid @RequestBody Comment comment) throws URISyntaxException {
        log.debug("REST request to update Comment : {}", comment);
        if (comment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Comment result = commentRepository.save(comment);
        TimeLine t = new TimeLine("Edited comment ", comment.getCommentBody(), comment.getUser());
        timeLineRepository.saveAndFlush(t);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, comment.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /comments} : get all the comments.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of comments in body.
     */
    @GetMapping("/comments")
    public ResponseEntity<List<Comment>> getAllComments(Pageable pageable) {
        log.debug("REST request to get a page of Comments");
        Page<Comment> page = commentRepository.findAll(pageable);
        for(Comment e : page) {
       	 System.out.println("------------"+e.getCommentReports().size()+"------------");
       }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /comments/:id} : get the "id" comment.
     *
     * @param id the id of the comment to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the comment, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/comments/{id}")
    public ResponseEntity<Comment> getComment(@PathVariable Long id) {
        log.debug("REST request to get Comment : {}", id);
        Optional<Comment> comment = commentRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(comment);
    }

    
    @GetMapping("/comments/report/{id}/{userId}")
    public ResponseEntity<Comment> report(@PathVariable Long id,@PathVariable String userId) {
    	log.debug("Participating to event "+id+" The user is "+userId);
    	
        Optional<Comment> comment = commentRepository.findById(id);

       User user = userRepository.findOneByLogin(userId).get();

       System.out.println("This is the user first name "+user.getFirstName());
       Set<User> reports = comment.get().getCommentReports();
       if(!reports.contains(user)) {

       reports.add(user);
       comment.get().setCommentReports(reports);
       TimeLine t = new TimeLine("Reported comment ", comment.get().getCommentBody(), comment.get().getUser());
       timeLineRepository.saveAndFlush(t);
       if(reports.size()>=REPORTS_MAX) {
    	   commentRepository.delete(comment.get());
       }}
       return ResponseUtil.wrapOrNotFound(comment);

    }
    
    
    
    /**
     * {@code DELETE  /comments/:id} : delete the "id" comment.
     *
     * @param id the id of the comment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/comments/{id}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long id) {
        log.debug("REST request to delete Comment : {}", id);
        Comment comment = commentRepository.getOne(id);
        TimeLine t = new TimeLine("Deleted comment ", comment.getCommentBody(), comment.getUser());
        timeLineRepository.saveAndFlush(t);
        commentRepository.deleteById(id);

        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
