package ikharipov.consumerservice.repository;

import ikharipov.consumerservice.models.Measurement;
import ikharipov.consumerservice.models.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MeasurementRepository extends JpaRepository<Measurement, UUID> {

    List<Measurement> findAllByMetric(Metric metric);
}