package nexushr_backend.attendance;

import nexushr_backend.employee.Employee;
import nexushr_backend.employee.EmployeeService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final EmployeeService employeeService;

    public AttendanceController(
            AttendanceService attendanceService,
            EmployeeService employeeService) {

        this.attendanceService = attendanceService;
        this.employeeService = employeeService;
    }

    // ADMIN will create attendance
    @PostMapping
    public ResponseEntity<Attendance> createAttendance(
            @RequestBody Attendance attendance,
            Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(
                attendanceService.saveAttendance(attendance)
        );
    }



    // ADMIN can view any employee attendance
    // EMPLOYEE can view only own attendance
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Attendance>> getAttendanceByEmployee(
            @PathVariable Long employeeId,
            Authentication authentication) {

        String username = authentication.getName();

        Employee employee =
                employeeService.getEmployeeByUsername(username);

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN")
                );

        if (!isAdmin && !employee.getId().equals(employeeId)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(
                attendanceService.getAttendanceByEmployee(employeeId)
        );
    }
}