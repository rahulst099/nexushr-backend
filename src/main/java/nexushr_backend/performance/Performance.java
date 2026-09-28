package nexushr_backend.performance;

import jakarta.persistence.*;

@Entity
@Table(name = "performance_reviews")
public class Performance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private nexushr_backend.employee.Employee employee;

    @Column(nullable = false)
    private String reviewPeriod;

    private Double rating;

    @Column(length = 2000)
    private String feedback;

    private String goals;

    private String status;

    public Performance() {
    }

    public Long getId() {
        return id;
    }


    public nexushr_backend.employee.Employee getEmployee() {
        return employee;
    }

    public void setEmployee(nexushr_backend.employee.Employee employee) {
        this.employee = employee;
    }

    public String getReviewPeriod() {
        return reviewPeriod;
    }

    public void setReviewPeriod(String reviewPeriod) {
        this.reviewPeriod = reviewPeriod;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}