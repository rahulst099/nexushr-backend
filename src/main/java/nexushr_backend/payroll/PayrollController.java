package nexushr_backend.payroll;

import nexushr_backend.employee.Employee;
import nexushr_backend.employee.EmployeeService;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {

    private final PayrollService payrollService;
    private final EmployeeService employeeService;

    public PayrollController(
            PayrollService payrollService,
            EmployeeService employeeService) {

        this.payrollService = payrollService;
        this.employeeService = employeeService;
    }

    // Create Payroll - ADMIN only
    @PostMapping
    public ResponseEntity<Payroll> createPayroll(
            @RequestBody Payroll payroll,
            Authentication authentication) {

        if (!isAdmin(authentication)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(
                payrollService.createPayroll(payroll)
        );
    }

    // Get All Payroll - ADMIN only
    @GetMapping
    public ResponseEntity<List<Payroll>> getAllPayroll(
            Authentication authentication) {

        if (!isAdmin(authentication)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(
                payrollService.getAllPayroll()
        );
    }

    // Get Payroll By ID
    // ADMIN -> any payroll
    // EMPLOYEE -> own payroll only
    @GetMapping("/{id}")
    public ResponseEntity<Payroll> getPayrollById(
            @PathVariable Long id,
            Authentication authentication) {

        return payrollService.getPayrollById(id)
                .map(payroll -> {

                    if (isAdmin(authentication)) {
                        return ResponseEntity.ok(payroll);
                    }

                    Employee employee =
                            employeeService.getEmployeeByUsername(
                                    authentication.getName()
                            );

                    if (!employee.getId()
                            .equals(payroll.getEmployeeId())) {

                        return ResponseEntity
                                .status(403)
                                .<Payroll>build();
                    }

                    return ResponseEntity.ok(payroll);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Get Employee Payroll
    // ADMIN -> any employee
    // EMPLOYEE -> own payroll only
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Payroll>> getPayrollByEmployee(
            @PathVariable Long employeeId,
            Authentication authentication) {

        if (!isAdmin(authentication)) {

            Employee employee =
                    employeeService.getEmployeeByUsername(
                            authentication.getName()
                    );

            if (!employee.getId().equals(employeeId)) {
                return ResponseEntity.status(403).build();
            }
        }

        return ResponseEntity.ok(
                payrollService.getPayrollByEmployee(employeeId)
        );
    }

    // Get Employee Payroll By Month
    // ADMIN -> any employee
    // EMPLOYEE -> own payroll only
    @GetMapping("/employee/{employeeId}/month/{month}")
    public ResponseEntity<Payroll> getPayrollByEmployeeAndMonth(
            @PathVariable Long employeeId,
            @PathVariable String month,
            Authentication authentication) {

        if (!isAdmin(authentication)) {

            Employee employee =
                    employeeService.getEmployeeByUsername(
                            authentication.getName()
                    );

            if (!employee.getId().equals(employeeId)) {
                return ResponseEntity.status(403).build();
            }
        }

        return payrollService
                .getPayrollByEmployeeAndMonth(employeeId, month)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get Payroll By Payment Status - ADMIN only
    @GetMapping("/status/{paymentStatus}")
    public ResponseEntity<List<Payroll>> getPayrollByStatus(
            @PathVariable String paymentStatus,
            Authentication authentication) {

        if (!isAdmin(authentication)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(
                payrollService.getPayrollByStatus(paymentStatus)
        );
    }

    // Mark Payroll As Paid - ADMIN only
    @PutMapping("/{id}/pay")
    public ResponseEntity<Payroll> markAsPaid(
            @PathVariable Long id,
            Authentication authentication) {

        if (!isAdmin(authentication)) {
            return ResponseEntity.status(403).build();
        }

        return payrollService.markAsPaid(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Download Payslip PDF
    // ADMIN -> any payslip
    // EMPLOYEE -> own payslip only
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadPayslip(
            @PathVariable Long id,
            Authentication authentication) {

        Payroll payroll =
                payrollService.getPayrollById(id)
                        .orElse(null);

        if (payroll == null) {
            return ResponseEntity.notFound().build();
        }

        if (!isAdmin(authentication)) {

            Employee employee =
                    employeeService.getEmployeeByUsername(
                            authentication.getName()
                    );

            if (!employee.getId()
                    .equals(payroll.getEmployeeId())) {

                return ResponseEntity.status(403).build();
            }
        }

        byte[] pdf =
                payrollService.generatePayslipPdf(id);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=payslip-" + id + ".pdf"
                )
                .contentType(
                        MediaType.APPLICATION_PDF
                )
                .body(pdf);
    }

    // Delete Payroll - ADMIN only
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayroll(
            @PathVariable Long id,
            Authentication authentication) {

        if (!isAdmin(authentication)) {
            return ResponseEntity.status(403).build();
        }

        if (payrollService.deletePayroll(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    // Check ADMIN role
    private boolean isAdmin(Authentication authentication) {

        return authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority()
                                .equals("ROLE_ADMIN")
                );
    }
}