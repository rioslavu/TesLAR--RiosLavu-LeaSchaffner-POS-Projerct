package school.tesla.lesson;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "photos")
public class Photo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "photo_name", nullable = false)
    private String name;

    @Convert(converter = OrientationConverter.class)
    @Column(name = "orientation", length = 1)
    private Orientation orientation;

    protected Photo() {
    }

    public Photo(String name, Orientation orientation) {
        this.name = Objects.requireNonNull(name, "name");
        this.orientation = orientation;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Orientation getOrientation() {
        return orientation;
    }
}
