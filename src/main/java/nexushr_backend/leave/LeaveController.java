package nexushr_backend.leave;

import nexushr_backend.employee.Employee;
import nexushr_backend.employee.EmployeeService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/leaves")
public class LeaveController {

    private final LeaveService leaveService;
    private final EmployeeService employeeService;

    public LeaveController(
            LeaveService leaveService,
            EmployeeService employeeService) {

        this.leaveService = leaveService;
        this.employeeService = employeeService;
    }

    // Apply Leave
    @PostMapping
    public ResponseEntity<Leave> applyLeave(
            @RequestBody Leave leave,
            Authentication authentication) {

        String username = authentication.getName();

        Employee employee =
                employeeService.getEmployeeByUsername(username);

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN")
                );

        // Employee can apply leave only for himself
        if (!isAdmin &&
                !employee.getId().equals(leave.getEmployeeId())) {

            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(
                leaveService.applyLeave(leave)
        );
    }

    // Get All Leaves - ADMIN
    @GetMapping
    public ResponseEntity<List<Leave>> getAllLeaves(
            Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN")
                );

        if (!isAdmin) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(
                leaveService.getAllLeaves()
        );
    }

    // Get Leave By ID
    @GetMapping("/{id}")
    public ResponseEntity<Leave> getLeaveById(
            @PathVariable Long id,
            Authentication authentication) {

        return leaveService.getLeaveById(id)
                .map(leave -> {

                    boolean isAdmin = authentication.getAuthorities()
                            .stream()
                            .anyMatch(authority ->
                                    authority.getAuthority()
                                            .equals("ROLE_ADMIN")
                            );

                    if (isAdmin) {
                        return ResponseEntity.ok(leave);
                    }

                    Employee employee =
                            employeeService.getEmployeeByUsername(
                                    authentication.getName()
                            );

                    if (!employee.getId()
                            .equals(leave.getEmployeeId())) {

                        return ResponseEntity
                                .status(403)
                                .<Leave>build();
                    }

                    return ResponseEntity.ok(leave);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Get Employee Leaves
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Leave>> getLeavesByEmployee(
            @PathVariable Long employeeId,
            Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN")
                );

        if (!isAdmin) {

            Employee employee =
                    employeeService.getEmployeeByUsername(
                            authentication.getName()
                    );

            if (!employee.getId().equals(employeeId)) {
                return ResponseEntity.status(403).build();
            }
        }

        return ResponseEntity.ok(
                leaveService.getLeavesByEmployee(employeeId)
        );
    }

    // Get Leaves By Status - ADMIN
    @GetMapping("/status/{status}")
    public ResponseEntity<List<Leave>> getLeavesByStatus(
            @PathVariable String status,
            Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN")
                );

        if (!isAdmin) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(
                leaveService.getLeavesByStatus(status)
        );
    }

    // Approve Leave - ADMIN
    @PutMapping("/{id}/approve")
    public ResponseEntity<Leave> approveLeave(
            @PathVariable Long id,
            Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN")
                );

        if (!isAdmin) {
            return ResponseEntity.status(403).build();
        }

        return leaveService.approveLeave(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Reject Leave - ADMIN
    @PutMapping("/{id}/reject")
    public ResponseEntity<Leave> rejectLeave(
            @PathVariable Long id,
            Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN")
                );

        if (!isAdmin) {
            return ResponseEntity.status(403).build();
        }

        return leaveService.rejectLeave(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete Leave
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLeave(
            @PathVariable Long id,
            Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN")
                );

        if (!isAdmin) {
            return ResponseEntity.status(403).build();
        }

        if (leaveService.deleteLeave(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}