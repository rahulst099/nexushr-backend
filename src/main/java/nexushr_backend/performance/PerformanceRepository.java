package nexushr_backend.performance;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PerformanceRepository
        extends JpaRepository<Performance, Long> {

    List<Performance> findByEmployee_Id(Long employeeId);
}