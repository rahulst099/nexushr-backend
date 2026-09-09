package nexushr_backend.leave;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    // Apply Leave
    @PostMapping
    public ResponseEntity<Leave> applyLeave(@RequestBody Leave leave) {
        return ResponseEntity.ok(leaveService.applyLeave(leave));
    }

    // Get All Leaves
    @GetMapping
    public ResponseEntity<List<Leave>> getAllLeaves() {
        return ResponseEntity.ok(leaveService.getAllLeaves());
    }

    // Get Leave By ID
    @GetMapping("/{id}")
    public ResponseEntity<Leave> getLeaveById(@PathVariable Long id) {
        return leaveService.getLeaveById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get Employee Leaves
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Leave>> getLeavesByEmployee(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                leaveService.getLeavesByEmployee(employeeId)
        );
    }

    // Get Leaves By Status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Leave>> getLeavesByStatus(
            @PathVariable String status) {

        return ResponseEntity.ok(
                leaveService.getLeavesByStatus(status)
        );
    }

    // Approve Leave
    @PutMapping("/{id}/approve")
    public ResponseEntity<Leave> approveLeave(@PathVariable Long id) {

        return leaveService.approveLeave(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Reject Leave
    @PutMapping("/{id}/reject")
    public ResponseEntity<Leave> rejectLeave(@PathVariable Long id) {

        return leaveService.rejectLeave(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete Leave
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeave(@PathVariable Long id) {

        if (leaveService.deleteLeave(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}