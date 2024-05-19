package ikharipov.consumerservice.repository;

import ikharipov.consumerservice.models.AvailableTag;
import ikharipov.consumerservice.models.Metric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface AvailableTagRepository extends JpaRepository<AvailableTag, UUID> {

    List<AvailableTag> findAllByMetric(Metric metric);
}
