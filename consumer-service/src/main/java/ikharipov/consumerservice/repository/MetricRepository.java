package ikharipov.consumerservice.repository;

import ikharipov.consumerservice.models.Metric;
import ikharipov.consumerservice.models.MetricType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface MetricRepository extends JpaRepository<Metric, UUID> {

    List<Metric> findAllByName(String name);

    List<Metric> findAllByType(MetricType type);

    List<Metric> findMetricsByCreatedBefore(Date dateBefore);

    List<Metric> findMetricsByCreatedAfter(Date dateAfter);

    List<Metric> findMetricsByCreatedBetween(Date startDate, Date endDate);
}
