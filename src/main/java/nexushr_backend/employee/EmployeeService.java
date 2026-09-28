package nexushr_backend.employee;

import nexushr_backend.user.User;
import nexushr_backend.user.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.cache.annotation.CacheEvict;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public EmployeeService(
            EmployeeRepository employeeRepository,
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // =========================
    // GET ALL EMPLOYEES
    // =========================
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // =========================
    // GET EMPLOYEE BY ID
    // =========================
    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Employee not found"));
    }

    // =========================
    // CREATE EMPLOYEE
    // =========================
    @CacheEvict(value = "dashboardSummary", allEntries = true)
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    // =========================
    // DELETE EMPLOYEE
    // =========================
    @CacheEvict(value = "dashboardSummary", allEntries = true)
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    // =========================
    // UPDATE EMPLOYEE
    // =========================
    @CacheEvict(value = "dashboardSummary", allEntries = true)
    public Employee updateEmployee(Long id, Employee employee) {

        Employee existingEmployee =
                employeeRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Employee not found"));

        existingEmployee.setEmployeeCode(
                employee.getEmployeeCode());

        existingEmployee.setFirstName(
                employee.getFirstName());

        existingEmployee.setLastName(
                employee.getLastName());

        existingEmployee.setEmail(
                employee.getEmail());

        existingEmployee.setPhone(
                employee.getPhone());

        existingEmployee.setDepartment(
                employee.getDepartment());

        existingEmployee.setDesignation(
                employee.getDesignation());

        existingEmployee.setSalary(
                employee.getSalary());

        existingEmployee.setActive(
                employee.getActive());

        return employeeRepository.save(existingEmployee);
    }

    // =========================
    // EMPLOYEE SIGNUP
    // =========================
    @CacheEvict(value = "dashboardSummary", allEntries = true)
    public Employee signup(EmployeeSignupRequest request) {

        // Check username
        if (userRepository.findByUsername(request.getUsername())
                .isPresent()) {

            throw new RuntimeException("Username already exists");
        }

        // Create User account
        User user = new User();

        user.setUsername(request.getUsername());

        // Encrypt password using BCrypt
        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        // Automatically assign EMPLOYEE role
        user.setRole("EMPLOYEE");

        // Save User first
        User savedUser = userRepository.save(user);

        // Create Employee record
        Employee employee = new Employee();

        employee.setEmployeeCode(request.getEmployeeCode());
        employee.setFirstName(request.getFirstName());
        employee.setLastName(request.getLastName());
        employee.setEmail(request.getEmail());
        employee.setPhone(request.getPhone());
        employee.setDepartment(request.getDepartment());
        employee.setDesignation(request.getDesignation());

        // Default values
        employee.setSalary(0.0);
        employee.setActive(true);

        // Link Employee with User
        employee.setUser(savedUser);

        return employeeRepository.save(employee);
    }

    public Employee getEmployeeByUsername(String username) {

        return employeeRepository.findByUserUsername(username)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee not found for username: " + username
                        )
                );
    }

    public void changePassword(
            String username,
            ChangePasswordRequest request) {

        User user = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        // Verify old password
        if (!passwordEncoder.matches(
                request.getOldPassword(),
                user.getPassword())) {

            throw new RuntimeException("Old password is incorrect");
        }

        // Encode and save new password
        user.setPassword(
                passwordEncoder.encode(request.getNewPassword())
        );

        userRepository.save(user);
    }

    public Employee updateMyProfile(
            String username,
            Employee updatedEmployee) {

        Employee existingEmployee =
                employeeRepository.findByUserUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Employee not found for username: " + username
                                ));

        existingEmployee.setFirstName(
                updatedEmployee.getFirstName());

        existingEmployee.setLastName(
                updatedEmployee.getLastName());

        existingEmployee.setEmail(
                updatedEmployee.getEmail());

        existingEmployee.setPhone(
                updatedEmployee.getPhone());

        existingEmployee.setDepartment(
                updatedEmployee.getDepartment());

        existingEmployee.setDesignation(
                updatedEmployee.getDesignation());

        return employeeRepository.save(existingEmployee);
    }

}

