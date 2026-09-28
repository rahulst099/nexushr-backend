package nexushr_backend.notification;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(
            NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification createNotification(Notification notification) {

        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now());

        return notificationRepository.save(notification);
    }

    public List<Notification> getEmployeeNotifications(
            Long employeeId) {

        return notificationRepository
                .findByEmployeeIdOrderByCreatedAtDesc(employeeId);
    }

    public Notification markAsRead(Long id) {

        Notification notification =
                notificationRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Notification not found"
                                )
                        );

        notification.setRead(true);

        return notificationRepository.save(notification);
    }
    public java.util.Optional<Notification> getNotificationById(Long id) {

        return notificationRepository.findById(id);
    }
    public void deleteNotification(Long id) {
        notificationRepository.deleteById(id);
    }
}

