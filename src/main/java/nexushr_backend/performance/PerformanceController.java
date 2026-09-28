package nexushr_backend.performance;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import nexushr_backend.employee.Employee;
import nexushr_backend.employee.EmployeeService;
import org.springframework.security.core.Authentication;


import java.util.List;

@RestController
@RequestMapping("/api/performance")
@CrossOrigin(origins = "http://localhost:5173")
public class PerformanceController {

    private final PerformanceService performanceService;
    private final EmployeeService employeeService;

    public PerformanceController(
            PerformanceService performanceService,
            EmployeeService employeeService) {

        this.performanceService = performanceService;
        this.employeeService = employeeService;
    }


    @GetMapping
    public ResponseEntity<List<Performance>> getAllPerformance(
            Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(
                performanceService.getAllPerformance()
        );
    }





    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Performance>> getPerformanceByEmployeeId(
            @PathVariable Long employeeId,
            Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return ResponseEntity.ok(
                    performanceService.getPerformanceByEmployeeId(employeeId)
            );
        }

        String username = authentication.getName();

        Employee employee =
                employeeService.getEmployeeByUsername(username);

        if (!employee.getId().equals(employeeId)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(
                performanceService.getPerformanceByEmployeeId(employeeId)
        );
    }








    @PostMapping
    public ResponseEntity<Performance> createPerformance(
            @RequestBody Performance performance,
            Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(
                performanceService.createPerformance(performance)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Performance> updatePerformance(
            @PathVariable Long id,
            @RequestBody Performance performance,
            Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(
                performanceService.updatePerformance(
                        id,
                        performance
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerformance(
            @PathVariable Long id,
            Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(403).build();
        }

        performanceService.deletePerformance(id);

        return ResponseEntity.noContent().build();
    }

}