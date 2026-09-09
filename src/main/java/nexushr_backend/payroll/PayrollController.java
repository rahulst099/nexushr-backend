package nexushr_backend.payroll;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payroll")
public class PayrollController {

    private final PayrollService payrollService;

    public PayrollController(PayrollService payrollService) {
        this.payrollService = payrollService;
    }

    // Create Payroll
    @PostMapping
    public ResponseEntity<Payroll> createPayroll(
            @RequestBody Payroll payroll) {

        return ResponseEntity.ok(
                payrollService.createPayroll(payroll)
        );
    }

    // Get All Payroll
    @GetMapping
    public ResponseEntity<List<Payroll>> getAllPayroll() {

        return ResponseEntity.ok(
                payrollService.getAllPayroll()
        );
    }

    // Get Payroll By ID
    @GetMapping("/{id}")
    public ResponseEntity<Payroll> getPayrollById(
            @PathVariable Long id) {

        return payrollService.getPayrollById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get Employee Payroll
    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Payroll>> getPayrollByEmployee(
            @PathVariable Long employeeId) {

        return ResponseEntity.ok(
                payrollService.getPayrollByEmployee(employeeId)
        );
    }

    // Get Employee Payroll By Month
    @GetMapping("/employee/{employeeId}/month/{month}")
    public ResponseEntity<Payroll> getPayrollByEmployeeAndMonth(
            @PathVariable Long employeeId,
            @PathVariable String month) {

        return payrollService
                .getPayrollByEmployeeAndMonth(employeeId, month)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Get Payroll By Payment Status
    @GetMapping("/status/{paymentStatus}")
    public ResponseEntity<List<Payroll>> getPayrollByStatus(
            @PathVariable String paymentStatus) {

        return ResponseEntity.ok(
                payrollService.getPayrollByStatus(paymentStatus)
        );
    }

    // Mark Payroll As Paid
    @PutMapping("/{id}/pay")
    public ResponseEntity<Payroll> markAsPaid(
            @PathVariable Long id) {

        return payrollService.markAsPaid(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Download Payslip PDF
    @GetMapping("/{id}/download")
    public ResponseEntity<byte[]> downloadPayslip(
            @PathVariable Long id) {

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

    // Delete Payroll
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayroll(
            @PathVariable Long id) {

        if (payrollService.deletePayroll(id)) {
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }
}