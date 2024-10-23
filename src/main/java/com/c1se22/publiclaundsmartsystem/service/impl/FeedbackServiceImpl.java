package com.c1se22.publiclaundsmartsystem.service.impl;

import com.c1se22.publiclaundsmartsystem.entity.Feedback;
import com.c1se22.publiclaundsmartsystem.entity.Machine;
import com.c1se22.publiclaundsmartsystem.entity.User;
import com.c1se22.publiclaundsmartsystem.payload.FeedbackDto;
import com.c1se22.publiclaundsmartsystem.payload.MachineDto;

import com.c1se22.publiclaundsmartsystem.payload.UserDto;
import com.c1se22.publiclaundsmartsystem.repository.FeedbackRespository;
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
    FeedbackRespository feedbackRespository;
    UserRepository userRepository;
    MachineRepository machineRepository;

    @Override
    public List<FeedbackDto> getAllFeedbacks() {
        return feedbackRespository.findAll().stream().map(this::mapToFeedbackDto).collect(Collectors.toList());
    }

    @Override
    public List<FeedbackDto> getFeedbackByIdMachine(Integer machineId) {
        return feedbackRespository.findFeedbackByMachineId(machineId).stream().map(this::mapToFeedbackDto).collect(Collectors.toList());
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
        return mapToFeedbackDto(feedbackRespository.save(feedback));
    }

    @Override
    public FeedbackDto updateFeedback(Integer id, FeedbackDto feedbackDto) {
        return null;
    }

    @Override
    public void deleteFeedback(Integer id) {
        Feedback feedback = feedbackRespository.findById(id).orElseThrow(
                ()-> new ResourceNotFoundException("Feedback", "id", id));
        feedbackRespository.delete(feedback);
    }

    private FeedbackDto mapToFeedbackDto(Feedback feedback){

        FeedbackDto feedbackDto = new FeedbackDto();
        feedbackDto.setId(feedback.getId());
        feedbackDto.setComment(feedback.getComment());
        feedbackDto.setRating(feedback.getRating());
        feedbackDto.setCreatedAt(feedback.getCreatedAt());
        feedbackDto.setUserId(feedback.getUser().getId());
        feedbackDto.setMachineId(feedback.getMachine().getId());
        return feedbackDto;
    }

}