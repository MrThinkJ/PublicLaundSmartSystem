package com.c1se22.publiclaundsmartsystem.service;

import com.c1se22.publiclaundsmartsystem.payload.FeedbackDto;

import java.util.List;

public interface FeedbackService {
    List<FeedbackDto> getAllFeedbacks();
    List<FeedbackDto> getFeedbackByIdMachine(Integer id);
    FeedbackDto getFeedbackById(Integer id);
    FeedbackDto addFeedback(FeedbackDto feedbackDto);
    FeedbackDto updateFeedback(Integer id, FeedbackDto feedbackDto);
    void deleteFeedback(Integer id);
}
