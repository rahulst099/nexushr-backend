package nexushr_backend.payroll;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.lowagie.text.Document;
import org.springframework.cache.annotation.CacheEvict;

import nexushr_backend.employee.Employee;
import nexushr_backend.employee.EmployeeRepository;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Optional;

@Service
public class PayrollService {

    private final PayrollRepository payrollRepository;
    private final EmployeeRepository employeeRepository;

    public PayrollService(
            PayrollRepository payrollRepository,
            EmployeeRepository employeeRepository) {

        this.payrollRepository = payrollRepository;
        this.employeeRepository = employeeRepository;
    }

    // Create Payroll
    @CacheEvict(value = "dashboardSummary", allEntries = true)
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
    @CacheEvict(value = "dashboardSummary", allEntries = true)
    public Optional<Payroll> markAsPaid(Long id) {

        return payrollRepository.findById(id)
                .map(payroll -> {
                    payroll.setPaymentStatus("PAID");
                    return payrollRepository.save(payroll);
                });
    }

    // Delete Payroll
    @CacheEvict(value = "dashboardSummary", allEntries = true)
    public boolean deletePayroll(Long id) {

        if (payrollRepository.existsById(id)) {
            payrollRepository.deleteById(id);
            return true;
        }

        return false;
    }

    // Generate Payslip PDF
    public byte[] generatePayslipPdf(Long payrollId) {

        Payroll payroll = payrollRepository.findById(payrollId)
                .orElseThrow(() ->
                        new RuntimeException("Payroll not found"));

        Employee employee = employeeRepository
                .findById(payroll.getEmployeeId())
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));

        try {

            ByteArrayOutputStream outputStream =
                    new ByteArrayOutputStream();

            Document document = new Document();

            PdfWriter.getInstance(document, outputStream);

            document.open();

            // Title
            Font titleFont = new Font(
                    Font.HELVETICA,
                    20,
                    Font.BOLD
            );

            Paragraph title =
                    new Paragraph("NexusHR", titleFont);

            title.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(title);

            // Subtitle
            Font subtitleFont = new Font(
                    Font.HELVETICA,
                    14,
                    Font.BOLD
            );

            Paragraph subtitle =
                    new Paragraph("EMPLOYEE PAYSLIP", subtitleFont);

            subtitle.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(subtitle);

            document.add(new Paragraph(" "));

            // Employee Information
            document.add(
                    new Paragraph("Employee Information")
            );

            PdfPTable employeeTable =
                    new PdfPTable(2);

            employeeTable.setWidthPercentage(100);

            addRow(
                    employeeTable,
                    "Employee ID",
                    employee.getEmployeeCode()
            );

            addRow(
                    employeeTable,
                    "Employee Name",
                    employee.getFirstName()
                            + " "
                            + employee.getLastName()
            );

            addRow(
                    employeeTable,
                    "Department",
                    employee.getDepartment()
            );

            addRow(
                    employeeTable,
                    "Designation",
                    employee.getDesignation()
            );

            document.add(employeeTable);

            document.add(new Paragraph(" "));

            // Payroll Information
            document.add(
                    new Paragraph("Payroll Information")
            );

            PdfPTable payrollTable =
                    new PdfPTable(2);

            payrollTable.setWidthPercentage(100);

            addRow(
                    payrollTable,
                    "Payslip ID",
                    String.valueOf(payroll.getId())
            );

            addRow(
                    payrollTable,
                    "Month",
                    payroll.getMonth()
            );

            addRow(
                    payrollTable,
                    "Basic Salary",
                    formatAmount(payroll.getBasicSalary())
            );

            addRow(
                    payrollTable,
                    "Allowances",
                    formatAmount(payroll.getAllowances())
            );

            addRow(
                    payrollTable,
                    "Deductions",
                    formatAmount(payroll.getDeductions())
            );

            addRow(
                    payrollTable,
                    "Net Salary",
                    formatAmount(payroll.getNetSalary())
            );

            addRow(
                    payrollTable,
                    "Payment Status",
                    payroll.getPaymentStatus()
            );

            document.add(payrollTable);

            document.add(new Paragraph(" "));

            Paragraph footer =
                    new Paragraph(
                            "This is a computer-generated payslip."
                    );

            footer.setAlignment(Paragraph.ALIGN_CENTER);

            document.add(footer);

            document.close();

            return outputStream.toByteArray();

        } catch (DocumentException e) {

            throw new RuntimeException(
                    "Failed to generate payslip PDF",
                    e
            );
        }
    }

    // Helper method for PDF table rows
    private void addRow(
            PdfPTable table,
            String label,
            String value) {

        PdfPCell labelCell =
                new PdfPCell(
                        new Phrase(label)
                );

        PdfPCell valueCell =
                new PdfPCell(
                        new Phrase(
                                value != null
                                        ? value
                                        : "-"
                        )
                );

        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    // Format salary amount
    private String formatAmount(Double amount) {

        if (amount == null) {
            return "₹0.00";
        }

        return String.format(
                "₹%.2f",
                amount
        );
    }
}