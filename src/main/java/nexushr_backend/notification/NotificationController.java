package nexushr_backend.notification;

import nexushr_backend.employee.Employee;
import nexushr_backend.employee.EmployeeService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:5173")
public class NotificationController {

    private final NotificationService notificationService;
    private final EmployeeService employeeService;

    public NotificationController(
            NotificationService notificationService,
            EmployeeService employeeService) {

        this.notificationService = notificationService;
        this.employeeService = employeeService;
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<List<Notification>> getEmployeeNotifications(
            @PathVariable Long employeeId,
            Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_ADMIN"));

        if (isAdmin) {
            return ResponseEntity.ok(
                    notificationService
                            .getEmployeeNotifications(employeeId)
            );
        }

        String username = authentication.getName();

        Employee employee =
                employeeService.getEmployeeByUsername(username);

        if (!employee.getId().equals(employeeId)) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(
                notificationService
                        .getEmployeeNotifications(employeeId)
        );
    }

    @PostMapping
    public ResponseEntity<Notification> createNotification(
            @RequestBody Notification notification,
            Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(
                notificationService
                        .createNotification(notification)
        );
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Notification> markAsRead(
            @PathVariable Long id,
            Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        Notification notification = notificationService
                .getNotificationById(id)
                .orElse(null);

        if (notification == null) {
            return ResponseEntity.notFound().build();
        }

        if (isAdmin) {
            return ResponseEntity.ok(notificationService.markAsRead(id));
        }

        String username = authentication.getName();

        Employee employee = employeeService.getEmployeeByUsername(username);

        if (!employee.getId().equals(notification.getEmployeeId())) {
            return ResponseEntity.status(403).build();
        }

        return ResponseEntity.ok(notificationService.markAsRead(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotification(
            @PathVariable Long id,
            Authentication authentication) {

        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(a ->
                        a.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            return ResponseEntity.status(403).build();
        }

        notificationService.deleteNotification(id);

        return ResponseEntity.noContent().build();
    }
}

