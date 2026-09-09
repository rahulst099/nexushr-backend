package nexushr_backend.attendance;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping
    public Attendance createAttendance(@RequestBody Attendance attendance) {
        return attendanceService.saveAttendance(attendance);
    }

    @GetMapping("/employee/{employeeId}")
    public List<Attendance> getAttendanceByEmployee(
            @PathVariable Long employeeId) {

        return attendanceService.getAttendanceByEmployee(employeeId);
    }
}