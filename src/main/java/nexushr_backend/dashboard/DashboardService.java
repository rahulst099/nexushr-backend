package nexushr_backend.dashboard;

import nexushr_backend.employee.EmployeeRepository;
import nexushr_backend.leave.LeaveRepository;
import nexushr_backend.payroll.PayrollRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {

    private final EmployeeRepository employeeRepository;
    private final LeaveRepository leaveRepository;
    private final PayrollRepository payrollRepository;

    public DashboardService(
            EmployeeRepository employeeRepository,
            LeaveRepository leaveRepository,
            PayrollRepository payrollRepository) {

        this.employeeRepository = employeeRepository;
        this.leaveRepository = leaveRepository;
        this.payrollRepository = payrollRepository;
    }

    @Cacheable("dashboardSummary")
    public DashboardSummary getSummary() {

        DashboardSummary summary = new DashboardSummary();

        // Employees
        summary.setTotalEmployees(
                employeeRepository.count()
        );

        summary.setActiveEmployees(
                employeeRepository.findAll()
                        .stream()
                        .filter(employee -> Boolean.TRUE.equals(employee.getActive()))
                        .count()
        );

        // Leaves
        summary.setTotalLeaves(
                leaveRepository.count()
        );

        summary.setPendingLeaves(
                leaveRepository.findByStatus("PENDING").size()
        );

        summary.setApprovedLeaves(
                leaveRepository.findByStatus("APPROVED").size()
        );

        summary.setRejectedLeaves(
                leaveRepository.findByStatus("REJECTED").size()
        );

        // Payroll
        summary.setTotalPayroll(
                payrollRepository.count()
        );

        summary.setPendingPayroll(
                payrollRepository.findByPaymentStatus("PENDING").size()
        );

        summary.setPaidPayroll(
                payrollRepository.findByPaymentStatus("PAID").size()
        );

        return summary;
    }
}