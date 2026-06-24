package com.nic.ipr.service;

import com.nic.ipr.exception.BadRequestException;
import com.nic.ipr.exception.ResourceNotFoundException;
import com.nic.ipr.entity.IprReturn;
import com.nic.ipr.repository.IprReturnRepository;
import com.nic.ipr.status.IprStatus;
import com.nic.ipr.entity.User;
import com.nic.ipr.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class IprReturnService {

    private final IprReturnRepository iprReturnRepository;
    private final UserRepository userRepository;// because we will implement a function of it
    private final IprNotificationService iprNotificationService;

    //Helper method
    private User getCurrentUser() {

        String username = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();

        return userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User Not Found"));
    }
    private void validateOwnership(IprReturn iprReturn) {

        User user = getCurrentUser();

        if (user.getRole().equalsIgnoreCase("EMPLOYEE")
                && !iprReturn.getEmployee().getId()
                .equals(user.getEmployee().getId())) {

            throw new BadRequestException(
                    "Access denied: You can only access your own IPR returns");
        }
    }

    public List<IprReturn> getAllIprReturns() {

        //-----------If employee try to get all ipr returns he will get only his own------------------//
        User user = getCurrentUser();
        if (user.getRole().equalsIgnoreCase("EMPLOYEE")) {
            return iprReturnRepository
                    .findByEmployeeId(user.getEmployee().getId());
        }
        //-------------------------------------------------------------------------------------------//

        return iprReturnRepository.findAll();
    }

    public IprReturn getIprReturnById(Long id) {

        IprReturn iprReturn = iprReturnRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "IPR Return not found with id: " + id));

        validateOwnership(iprReturn);
        return iprReturn;
    }

    public List<IprReturn> getIprReturnsByEmployeeId(Long employeeId) {

        User user = getCurrentUser();

        if (user.getRole().equalsIgnoreCase("EMPLOYEE")
                && !employeeId.equals(user.getEmployee().getId())) {

            throw new BadRequestException(
                    "You can only access your own IPR returns");
        }

        return iprReturnRepository.findByEmployeeId(employeeId);
    }


    public IprReturn addIprReturn(IprReturn iprReturn) {
        //checkng if IprReturn entry window is Open
        if(!iprNotificationService.isFilingWindowOpen()){
            throw new BadRequestException("IPR filing window is currently closed");
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();//Get the username of whoever is currently logged in

        //Use that username to find the User from DB-- this secures from any other login username holder to change any other ipr return
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User Not Found"));

        //Get the employee object from user data and set in iprReturn data Employee object -- to Set the logged-in user's employee into the IPR Return
        iprReturn.setEmployee(user.getEmployee());

        // Force default status to DRAFT if not provided
        // because JSON from frontend may not include status field → arrives as nullg
        if (iprReturn.getStatus() == null) {
            iprReturn.setStatus(IprStatus.DRAFT);
        }


        return iprReturnRepository.save(iprReturn);
    }

    public IprReturn updateIprReturn(Long id, IprReturn updatedIprReturn) {
        //checkng if IprReturn entry window is Open
        if (!iprNotificationService.isFilingWindowOpen()) {
            throw new BadRequestException(
                    "IPR filing window is currently closed");
        }

        IprReturn existingReturn = iprReturnRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("IPR Return not found with id: " + id));

        //-------------This code block check only whether the employeeId is present in database----------//
        validateOwnership(existingReturn);
        //-----------------------------------------------------------------------------------------------//

        existingReturn.setReportingYear(updatedIprReturn.getReportingYear());
        existingReturn.setAsOnDate(updatedIprReturn.getAsOnDate());
        existingReturn.setTotalAnnualIncome(updatedIprReturn.getTotalAnnualIncome());
        existingReturn.setIsNoProperty(updatedIprReturn.getIsNoProperty());
        //existingReturn.setStatus(updatedIprReturn.getStatus());// so that anyone insystem cant have admin priviledges to change the status of iprreturn

        return iprReturnRepository.save(existingReturn);
    }

    public IprReturn submitIprReturn(Long id) {
        //checkng if IprReturn entry window is Open
        if (!iprNotificationService.isFilingWindowOpen()) {
            throw new BadRequestException(
                    "IPR filing window is currently closed");
        }

        IprReturn iprReturn = iprReturnRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("IPR Return not found with id: " + id));

        validateOwnership(iprReturn);

        if (iprReturn.getStatus() != IprStatus.DRAFT) {
            throw new BadRequestException(
                    "Only draft IPR returns can be submitted");
        }

        iprReturn.setStatus(IprStatus.SUBMITTED);
        iprReturn.setSubmittedAt(LocalDateTime.now());

        return iprReturnRepository.save(iprReturn);
    }

    public IprReturn approveIprReturn(Long id) {

        // Get currently logged-in username from JWT
        String username = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        // Find the corresponding user
        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User Not Found"));

        // Only ADMIN can approve
        if (!user.getRole().equalsIgnoreCase("ADMIN")) {
            throw new BadRequestException(
                    "Only ADMIN can approve IPR returns");
        }

        // Find the IPR Return
        IprReturn iprReturn = iprReturnRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "IPR Return not found with id: " + id));

        // Only SUBMITTED IPRs can be approved
        if (iprReturn.getStatus() != IprStatus.SUBMITTED) {
            throw new BadRequestException(
                    "Only submitted IPR returns can be approved");
        }

        // Approve and set timestamp
        iprReturn.setStatus(IprStatus.APPROVED);
        iprReturn.setApprovedAt(LocalDateTime.now());

        return iprReturnRepository.save(iprReturn);
    }

    public void deleteIprReturn(Long id) {

        IprReturn iprReturn = iprReturnRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("IPR Return not found with id: " + id));

        validateOwnership(iprReturn);



        iprReturnRepository.delete(iprReturn);
    }
}