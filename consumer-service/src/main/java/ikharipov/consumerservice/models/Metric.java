package ikharipov.consumerservice.models;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "metrics", schema = "consumer")
public class Metric extends CurrentEntity {

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "base_unit")
    private String baseUnit;

    @OneToMany(mappedBy = "metric", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Measurement> measurements;

    @OneToMany(mappedBy = "metric", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<AvailableTag> availableTags;

    @Enumerated(value = EnumType.STRING)
    @Column(name = "type")
    private MetricType type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public MetricType getType() {
        return type;
    }

    public void setType(MetricType type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBaseUnit() {
        return baseUnit;
    }

    public void setBaseUnit(String baseUnit) {
        this.baseUnit = baseUnit;
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }

    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public List<AvailableTag> getAvailableTags() {
        return availableTags;
    }

    public void setAvailableTags(List<AvailableTag> availableAvailableTags) {
        this.availableTags = availableAvailableTags;
    }
}
