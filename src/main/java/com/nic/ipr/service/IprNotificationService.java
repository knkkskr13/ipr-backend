package com.nic.ipr.service;

import com.nic.ipr.exception.BadRequestException;
import com.nic.ipr.exception.ResourceNotFoundException;
import com.nic.ipr.entity.IprNotification;
import com.nic.ipr.repository.IprNotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class IprNotificationService {

    private final IprNotificationRepository repository;

    public List<IprNotification> getAllNotifications() {
        return repository.findAll();
    }

    public IprNotification createNotification(IprNotification notification) {
        if (notification.getActive() == null) {
            notification.setActive(true);

        }

        //---------this code restricts admin to create another IPR notification untill no active IPR notification found in database------------//
        Optional<IprNotification> existingNotification =
                repository.findByActiveTrue();

        if(existingNotification.isPresent()) {
            throw new BadRequestException(
                    "An active filing window already exists");
        }
        //-------------------------------------------------------------------------------------------------------------------------------------//

        return repository.save(notification);
    }

    public IprNotification getActiveNotification() {

        return repository.findByActiveTrue()
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "No active notification found"));
    }
    public boolean isFilingWindowOpen() {

        Optional<IprNotification> currentNotification =
                repository.findByActiveTrue();

        if (!currentNotification.isPresent()) {
            return false;
        }

        //storing the entity object for later use
        IprNotification notification =
                currentNotification.get();

        //get latest date
        LocalDate today = LocalDate.now();

        //checks if current date is before startdate
        if (today.isBefore(notification.getStartDate())) {
            return false;
        }
        //checks if current date is after endDate
        if (today.isAfter(notification.getEndDate())) {
            return false;
        }

        //return true if above checks have been passed
        return true;
    }

    public IprNotification updateNotification(
            Long id,
            IprNotification updatedNotification) {

        IprNotification existingNotification =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Notification not found with id: " + id));

        existingNotification.setTitle(
                updatedNotification.getTitle());

        existingNotification.setMessage(
                updatedNotification.getMessage());

        existingNotification.setStartDate(
                updatedNotification.getStartDate());

        existingNotification.setEndDate(
                updatedNotification.getEndDate());

        existingNotification.setActive(
                updatedNotification.getActive());

        return repository.save(existingNotification);
    }

    public void deleteNotification(Long id) {
        repository.deleteById(id);
    }
}