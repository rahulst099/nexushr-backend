package nexushr_backend.performance;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PerformanceService {

    private final PerformanceRepository performanceRepository;

    public PerformanceService(PerformanceRepository performanceRepository) {
        this.performanceRepository = performanceRepository;
    }

    public List<Performance> getAllPerformance() {
        return performanceRepository.findAll();
    }

    public Optional<Performance> getPerformanceById(Long id) {
        return performanceRepository.findById(id);
    }

    public List<Performance> getPerformanceByEmployeeId(Long employeeId) {
        return performanceRepository.findByEmployee_Id(employeeId);
    }

    public Performance createPerformance(Performance performance) {
        return performanceRepository.save(performance);
    }

    public Performance updatePerformance(Long id, Performance updatedPerformance) {

        return performanceRepository.findById(id)
                .map(existingPerformance -> {

                    existingPerformance.setEmployee(
                            updatedPerformance.getEmployee()
                    );

                    existingPerformance.setReviewPeriod(
                            updatedPerformance.getReviewPeriod()
                    );

                    existingPerformance.setRating(
                            updatedPerformance.getRating()
                    );

                    existingPerformance.setFeedback(
                            updatedPerformance.getFeedback()
                    );

                    existingPerformance.setGoals(
                            updatedPerformance.getGoals()
                    );

                    existingPerformance.setStatus(
                            updatedPerformance.getStatus()
                    );

                    return performanceRepository.save(
                            existingPerformance
                    );
                })
                .orElseThrow(() ->
                        new RuntimeException(
                                "Performance record not found"
                        )
                );
    }

    public void deletePerformance(Long id) {
        performanceRepository.deleteById(id);
    }
}