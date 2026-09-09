package nexushr_backend.leave;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LeaveRepository extends JpaRepository<Leave, Long> {

    List<Leave> findByEmployeeId(Long employeeId);

    List<Leave> findByStatus(String status);
}