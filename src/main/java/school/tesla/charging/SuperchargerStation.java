package school.tesla.charging;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import org.hibernate.annotations.Check;
import school.tesla.value.Address;
import school.tesla.value.GeoCoordinate;
import school.tesla.value.Money;
import school.tesla.value.StationName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "supercharger_stations")
@Check(constraints = "latitude between -90 and 90 and longitude between -180 and 180 and price_per_kwh_amount >= 0")
public class SuperchargerStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private StationName name;

    @Embedded
    private Address address;

    @Embedded
    private GeoCoordinate location;

    @Embedded
    @AttributeOverride(name = "amount.value",
            column = @Column(name = "price_per_kwh_amount", nullable = false, precision = 19, scale = 4))
    @AttributeOverride(name = "currency",
            column = @Column(name = "price_per_kwh_currency", nullable = false, length = 3))
    private Money pricePerKwh;

    @ElementCollection
    @CollectionTable(name = "station_stalls", joinColumns = @JoinColumn(name = "station_id"))
    @OrderColumn(name = "stall_order", nullable = false)
    private List<ChargingStall> stalls = new ArrayList<>();

    protected SuperchargerStation() {
    }

    public SuperchargerStation(
            StationName name,
            Address address,
            GeoCoordinate location,
            Money pricePerKwh,
            List<ChargingStall> stalls
    ) {
        this.name = Objects.requireNonNull(name, "name");
        this.address = Objects.requireNonNull(address, "address");
        this.location = Objects.requireNonNull(location, "location");
        this.pricePerKwh = Objects.requireNonNull(pricePerKwh, "pricePerKwh");
        this.stalls = new ArrayList<>(List.copyOf(stalls));
        if (this.stalls.isEmpty()) {
            throw new IllegalArgumentException("stalls must contain at least one item");
        }
    }

    public Long getId() {
        return id;
    }

    public StationName getName() {
        return name;
    }

    public Address getAddress() {
        return address;
    }

    public GeoCoordinate getLocation() {
        return location;
    }

    public Money getPricePerKwh() {
        return pricePerKwh;
    }

    public List<ChargingStall> getStalls() {
        return List.copyOf(stalls);
    }
}
