package nexushr_backend.leave;

import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;
import java.util.Optional;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepository;

    public LeaveService(LeaveRepository leaveRepository) {
        this.leaveRepository = leaveRepository;
    }

    // Apply Leave
    @CacheEvict(value = "dashboardSummary", allEntries = true)
    public Leave applyLeave(Leave leave) {
        leave.setStatus("PENDING");
        return leaveRepository.save(leave);
    }

    // Get All Leaves
    public List<Leave> getAllLeaves() {
        return leaveRepository.findAll();
    }

    // Get Leave By ID
    public Optional<Leave> getLeaveById(Long id) {
        return leaveRepository.findById(id);
    }

    // Get Employee Leaves
    public List<Leave> getLeavesByEmployee(Long employeeId) {
        return leaveRepository.findByEmployeeId(employeeId);
    }

    // Get Leaves By Status
    public List<Leave> getLeavesByStatus(String status) {
        return leaveRepository.findByStatus(status);
    }

    // Approve Leave
    @CacheEvict(value = "dashboardSummary", allEntries = true)
    public Optional<Leave> approveLeave(Long id) {
        return leaveRepository.findById(id).map(leave -> {
            leave.setStatus("APPROVED");
            return leaveRepository.save(leave);
        });
    }

    // Reject Leave
    @CacheEvict(value = "dashboardSummary", allEntries = true)
    public Optional<Leave> rejectLeave(Long id) {
        return leaveRepository.findById(id).map(leave -> {
            leave.setStatus("REJECTED");
            return leaveRepository.save(leave);
        });
    }

    // Delete Leave
    @CacheEvict(value = "dashboardSummary", allEntries = true)
    public boolean deleteLeave(Long id) {
        if (leaveRepository.existsById(id)) {
            leaveRepository.deleteById(id);
            return true;
        }

        return false;

    }
}