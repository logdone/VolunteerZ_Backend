package com.wsfb.volunteer.web.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.wsfb.volunteer.domain.Reaction;
import com.wsfb.volunteer.domain.TimeLine;
import com.wsfb.volunteer.repository.EventRepository;
import com.wsfb.volunteer.repository.ReactionRepository;
import com.wsfb.volunteer.repository.TimeLineRepository;
import com.wsfb.volunteer.repository.UserRepository;

import io.github.jhipster.web.util.PaginationUtil;

@RestController
@RequestMapping("/api")
@Transactional
public class TimeLineResource {
	 private final Logger log = LoggerFactory.getLogger(ReactionResource.class);

	    private static final String ENTITY_NAME = "timeline";

	    @Value("${jhipster.clientApp.name}")
	    private String applicationName;

	    @Autowired
	    private  TimeLineRepository timeLineRepository ;
	    @Autowired
	    public  UserRepository userRepository;

	    @GetMapping("/timeline/{id}")
	    public List<TimeLine> getAllReactions(@PathVariable String id) {
	        log.debug("REST request to get a page of Reactions");
	        List<TimeLine> list = timeLineRepository.findByOwnerOrderByTime(userRepository.findOneByLogin(id).get());
	        return list;
	    }
}
