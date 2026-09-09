package nexushr_backend.payroll;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PayrollRepository extends JpaRepository<Payroll, Long> {

    List<Payroll> findByEmployeeId(Long employeeId);

    Optional<Payroll> findByEmployeeIdAndMonth(Long employeeId, String month);

    List<Payroll> findByPaymentStatus(String paymentStatus);
}