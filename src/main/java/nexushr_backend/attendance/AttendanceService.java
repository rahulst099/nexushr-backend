package nexushr_backend.attendance;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;

    public AttendanceService(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    public Attendance saveAttendance(Attendance attendance) {

        boolean exists = attendanceRepository
                .existsByEmployeeIdAndDate(
                        attendance.getEmployeeId(),
                        attendance.getDate()
                );

        if (exists) {
            throw new RuntimeException(
                    "Attendance already exists for this employee on this date"
            );
        }

        return attendanceRepository.save(attendance);
    }

    public List<Attendance> getAllAttendance() {
        return attendanceRepository.findAll();
    }
    public List<Attendance> getAttendanceByEmployee(Long employeeId) {
        return attendanceRepository.findByEmployeeId(employeeId);
    }
}