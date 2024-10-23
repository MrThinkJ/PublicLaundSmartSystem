package com.c1se22.publiclaundsmartsystem.repository;

import com.c1se22.publiclaundsmartsystem.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;


@Repository
public interface FeedbackRespository extends JpaRepository<Feedback, Integer> {
        List<Feedback> findFeedbackByMachineId(@Param("machineId") Integer machineId);
}
