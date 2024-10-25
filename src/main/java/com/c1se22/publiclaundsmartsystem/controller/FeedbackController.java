package com.c1se22.publiclaundsmartsystem.controller;

import com.c1se22.publiclaundsmartsystem.payload.FeedbackDto;
import com.c1se22.publiclaundsmartsystem.payload.MaintenanceLogsDto;
import com.c1se22.publiclaundsmartsystem.service.FeedbackService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import org.springframework.http.ResponseEntity;

import java.util.List;


@RestController
@RequestMapping("/api/feedbacks")
@AllArgsConstructor

public class FeedbackController {
    FeedbackService feedbackService;

    @GetMapping
    public  ResponseEntity<List<FeedbackDto>> getFeedbacks(){
        return ResponseEntity.ok(feedbackService.getAllFeedbacks());
    }
    @GetMapping ("/{id}")
    public  ResponseEntity<FeedbackDto> getMaintenanceLogById(@PathVariable Integer id){
        return ResponseEntity.ok(feedbackService.getFeedbackById(id));
    }

    @GetMapping ("/machine/{machineId}")
    public  ResponseEntity<List<FeedbackDto>> getFeedbackbyMachineId(@PathVariable Integer machineId){
        return ResponseEntity.ok(feedbackService.getFeedbackByIdMachine(machineId));
    }

    @PostMapping
    public ResponseEntity<FeedbackDto> addFeedback(@RequestBody FeedbackDto feedbackDto){
        return ResponseEntity.ok(feedbackService.addFeedback(feedbackDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FeedbackDto> updateFeedback(@PathVariable Integer id, @RequestBody FeedbackDto feedbackDto){
        return ResponseEntity.ok(feedbackService.updateFeedback(id, feedbackDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteFeedback(@PathVariable Integer id){
        feedbackService.deleteFeedback(id);
        return ResponseEntity.ok("Feedback deleted successfully");
    }

}
