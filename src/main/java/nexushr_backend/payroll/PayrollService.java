package nexushr_backend.payroll;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PayrollService {

    private final PayrollRepository payrollRepository;

    public PayrollService(PayrollRepository payrollRepository) {
        this.payrollRepository = payrollRepository;
    }

    // Create Payroll
    public Payroll createPayroll(Payroll payroll) {

        double basicSalary = payroll.getBasicSalary() != null
                ? payroll.getBasicSalary()
                : 0;

        double allowances = payroll.getAllowances() != null
                ? payroll.getAllowances()
                : 0;

        double deductions = payroll.getDeductions() != null
                ? payroll.getDeductions()
                : 0;

        double netSalary = basicSalary + allowances - deductions;

        payroll.setNetSalary(netSalary);

        if (payroll.getPaymentStatus() == null) {
            payroll.setPaymentStatus("PENDING");
        }

        return payrollRepository.save(payroll);
    }

    // Get All Payroll
    public List<Payroll> getAllPayroll() {
        return payrollRepository.findAll();
    }

    // Get Payroll By ID
    public Optional<Payroll> getPayrollById(Long id) {
        return payrollRepository.findById(id);
    }

    // Get Employee Payroll
    public List<Payroll> getPayrollByEmployee(Long employeeId) {
        return payrollRepository.findByEmployeeId(employeeId);
    }

    // Get Employee Payroll By Month
    public Optional<Payroll> getPayrollByEmployeeAndMonth(
            Long employeeId, String month) {

        return payrollRepository
                .findByEmployeeIdAndMonth(employeeId, month);
    }

    // Get Payroll By Payment Status
    public List<Payroll> getPayrollByStatus(String paymentStatus) {
        return payrollRepository.findByPaymentStatus(paymentStatus);
    }

    // Mark Payroll as Paid
    public Optional<Payroll> markAsPaid(Long id) {

        return payrollRepository.findById(id)
                .map(payroll -> {
                    payroll.setPaymentStatus("PAID");
                    return payrollRepository.save(payroll);
                });
    }

    // Delete Payroll
    public boolean deletePayroll(Long id) {

        if (payrollRepository.existsById(id)) {
            payrollRepository.deleteById(id);
            return true;
        }

        return false;
    }
}