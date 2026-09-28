package nexushr_backend.employee;

import nexushr_backend.security.JwtService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public EmployeeController(
            EmployeeService employeeService,
            AuthenticationManager authenticationManager,
            JwtService jwtService) {

        this.employeeService = employeeService;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    // =========================
    // EMPLOYEE SIGNUP
    // =========================
    @PostMapping("/signup")
    public Employee signup(
            @RequestBody EmployeeSignupRequest request) {

        return employeeService.signup(request);
    }

    // =========================
    // EMPLOYEE LOGIN
    // =========================
    @PostMapping("/login")
    public String login(
            @RequestBody EmployeeLoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getUsername(),
                                request.getPassword()
                        )
                );

        return jwtService.generateToken(
                (org.springframework.security.core.userdetails.User)
                        authentication.getPrincipal()
        );
    }

    // =========================
    // GET ALL EMPLOYEES
    // ADMIN ONLY
    // =========================
    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees(
            Authentication authentication) {

        if (!isAdmin(authentication)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(
                employeeService.getAllEmployees()
        );
    }

    // =========================
    // GET EMPLOYEE BY ID
    // ADMIN ONLY
    // =========================
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(
            @PathVariable Long id,
            Authentication authentication) {

        if (!isAdmin(authentication)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(
                employeeService.getEmployeeById(id)
        );
    }

    // =========================
    // CREATE EMPLOYEE
    // ADMIN ONLY
    // =========================
    @PostMapping
    public ResponseEntity<Employee> createEmployee(
            @RequestBody Employee employee,
            Authentication authentication) {

        if (!isAdmin(authentication)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(
                employeeService.saveEmployee(employee)
        );
    }

    // =========================
    // DELETE EMPLOYEE
    // ADMIN ONLY
    // =========================
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(
            @PathVariable Long id,
            Authentication authentication) {

        if (!isAdmin(authentication)) {
            return ResponseEntity.status(403).build();
        }

        employeeService.deleteEmployee(id);

        return ResponseEntity.ok(
                "Employee deleted successfully"
        );
    }

    // =========================
    // UPDATE EMPLOYEE
    // ADMIN ONLY
    // =========================
    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(
            @PathVariable Long id,
            @RequestBody Employee employee,
            Authentication authentication) {

        if (!isAdmin(authentication)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(
                employeeService.updateEmployee(id, employee)
        );
    }
    @GetMapping("/me")
    public ResponseEntity<Employee> getMyProfile(
            Authentication authentication) {

        String username = authentication.getName();

        Employee employee =
                employeeService.getEmployeeByUsername(username);

        return ResponseEntity.ok(employee);
    }

    @PutMapping("/me")
    public ResponseEntity<Employee> updateMyProfile(
            @RequestBody Employee employee,
            Authentication authentication) {

        String username = authentication.getName();

        Employee updatedEmployee =
                employeeService.updateMyProfile(
                        username,
                        employee
                );

        return ResponseEntity.ok(updatedEmployee);
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @RequestBody ChangePasswordRequest request,
            Authentication authentication) {

        String username = authentication.getName();

        employeeService.changePassword(
                username,
                request
        );

        return ResponseEntity.ok(
                "Password changed successfully"
        );
    }

    // =========================
    // ADMIN CHECK
    // =========================
    private boolean isAdmin(Authentication authentication) {

        return authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority()
                                .equals("ROLE_ADMIN")
                );
    }
}

