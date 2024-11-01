package com.c1se22.publiclaundsmartsystem.service.impl;

import com.c1se22.publiclaundsmartsystem.entity.Feedback;
import com.c1se22.publiclaundsmartsystem.entity.Machine;
import com.c1se22.publiclaundsmartsystem.entity.User;
import com.c1se22.publiclaundsmartsystem.payload.FeedbackDto;
import com.c1se22.publiclaundsmartsystem.repository.FeedbackRepository;
import com.c1se22.publiclaundsmartsystem.repository.MachineRepository;
import com.c1se22.publiclaundsmartsystem.repository.UserRepository;
import com.c1se22.publiclaundsmartsystem.service.FeedbackService;
import com.c1se22.publiclaundsmartsystem.exception.ResourceNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FeedbackServiceImpl implements FeedbackService {
    FeedbackRepository feedbackRepository;
    UserRepository userRepository;
    MachineRepository machineRepository;

    @Override
    public List<FeedbackDto> getAllFeedbacks() {
        return feedbackRepository.findAll().stream().map(this::mapToFeedbackDto).collect(Collectors.toList());
    }

    @Override
    public List<FeedbackDto> getFeedbackByIdMachine(Integer machineId) {
        return feedbackRepository.findFeedbackByMachineId(machineId).stream().map(this::mapToFeedbackDto).collect(Collectors.toList());
    }

    @Override
    public FeedbackDto getFeedbackById(Integer id) {
        Feedback feedback = feedbackRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Feedback", "id", id)
        );
        return mapToFeedbackDto(feedback);
    }

    @Override
    public FeedbackDto addFeedback(FeedbackDto feedbackDto) {
        Feedback feedback = new Feedback();
        feedback.setComment(feedbackDto.getComment());
        feedback.setRating(feedbackDto.getRating());
        feedback.setCreatedAt(feedbackDto.getCreatedAt());
        User user = userRepository.findById(feedbackDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", feedbackDto.getUserId()));
        Machine machine = machineRepository.findById(feedbackDto.getMachineId())
                .orElseThrow(() -> new ResourceNotFoundException("Machine", "id", feedbackDto.getMachineId()));
        feedback.setUser(user);
        feedback.setMachine(machine);
        return mapToFeedbackDto(feedbackRepository.save(feedback));
    }

    @Override
    public FeedbackDto updateFeedback(Integer id, FeedbackDto feedbackDto) {
        Feedback feedback = feedbackRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Feedback", "id", id)
        );
        feedback.setComment(feedbackDto.getComment());
        feedback.setRating(feedbackDto.getRating());
        feedback.setCreatedAt(feedbackDto.getCreatedAt());
        User user = userRepository.findById(feedbackDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", feedbackDto.getUserId()));

        Machine machine = machineRepository.findById(feedbackDto.getMachineId())
                .orElseThrow(() -> new ResourceNotFoundException("Machine", "id", feedbackDto.getMachineId()));
        feedback.setUser(user);
        feedback.setMachine(machine);
        return mapToFeedbackDto(feedbackRepository.save(feedback));
    }

    @Override
    public void deleteFeedback(Integer id) {
        Feedback feedback = feedbackRepository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Feedback", "id", id));
        feedbackRepository.delete(feedback);
    }

    private FeedbackDto mapToFeedbackDto(Feedback feedback){
        return FeedbackDto.builder()
                .id(feedback.getId())
                .rating(feedback.getRating())
                .comment(feedback.getComment())
                .createdAt(feedback.getCreatedAt())
                .userId(feedback.getUser() != null ? feedback.getUser().getId() : null)
                .machineId(feedback.getMachine() != null ? feedback.getMachine().getId() : null)
                .build();
    }

}
