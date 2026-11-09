package school.tesla.charging;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.hibernate.annotations.Check;
import school.tesla.value.EnergyKwh;
import school.tesla.value.Money;
import school.tesla.value.StallNo;
import school.tesla.vehicle.Vehicle;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "charging_sessions")
@Check(constraints = """
        energy_charged_kwh >= 0 and
        ((total_cost_amount is null and total_cost_currency is null) or
         (total_cost_amount is not null and total_cost_amount >= 0 and total_cost_currency is not null))
        """)
public class ChargingSession {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private StallNo stallNo;

    @Column(name = "start_ts", nullable = false)
    private LocalDateTime startTS;

    @Column(name = "end_ts")
    private LocalDateTime endTS;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "energy_charged_kwh", nullable = false))
    private EnergyKwh energyCharged;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "station_id", nullable = false)
    private SuperchargerStation station;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Embedded
    @AttributeOverride(name = "amount.value",
            column = @Column(name = "total_cost_amount", precision = 19, scale = 4))
    @AttributeOverride(name = "currency", column = @Column(name = "total_cost_currency", length = 3))
    private Money totalCost;

    protected ChargingSession() {
    }

    public ChargingSession(
            StallNo stallNo,
            LocalDateTime startTS,
            LocalDateTime endTS,
            EnergyKwh energyCharged,
            SuperchargerStation station,
            Vehicle vehicle,
            Money totalCost
    ) {
        this.stallNo = Objects.requireNonNull(stallNo, "stallNo");
        this.startTS = Objects.requireNonNull(startTS, "startTS");
        this.endTS = endTS;
        this.energyCharged = Objects.requireNonNull(energyCharged, "energyCharged");
        this.station = Objects.requireNonNull(station, "station");
        this.vehicle = Objects.requireNonNull(vehicle, "vehicle");
        this.totalCost = totalCost;
    }

    public Long getId() {
        return id;
    }

    public StallNo getStallNo() {
        return stallNo;
    }

    public LocalDateTime getStartTS() {
        return startTS;
    }

    public LocalDateTime getEndTS() {
        return endTS;
    }

    public EnergyKwh getEnergyCharged() {
        return energyCharged;
    }

    public SuperchargerStation getStation() {
        return station;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Money getTotalCost() {
        return totalCost;
    }
}
