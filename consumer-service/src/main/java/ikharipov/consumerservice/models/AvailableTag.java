package ikharipov.consumerservice.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "tags", schema = "consumer")
public class AvailableTag extends CurrentEntity {

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "metric", referencedColumnName = "id")
    @JsonIgnore
    private Metric metric;

    @Column(name = "tag")
    private String tag;

    @ElementCollection
    @CollectionTable(name = "tag_values", schema = "consumer", joinColumns = @JoinColumn(name = "tag_id"))
    private List<String> values;

    public Metric getMetric() {
        return metric;
    }

    public void setMetric(Metric metric) {
        this.metric = metric;
    }

    public List<String> getValues() {
        return values;
    }
    public void setValues(List<String> values) {
        this.values = values;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
