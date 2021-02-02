package com.wsfb.volunteer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import com.wsfb.volunteer.domain.TimeLine;
import com.wsfb.volunteer.domain.User;

public interface TimeLineRepository extends JpaRepository<TimeLine, Long>{
	List<TimeLine> findByOwnerOrderByTime(User user);
}
