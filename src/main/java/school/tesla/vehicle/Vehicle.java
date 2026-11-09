package school.tesla.vehicle;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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
import school.tesla.identity.Owner;
import school.tesla.persistence.TeslaModelConverter;
import school.tesla.value.LicensePlate;
import school.tesla.value.Mileage;
import school.tesla.value.SwVersion;
import school.tesla.value.Vin;

import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "vehicles")
@Check(constraints = "mileage >= 0 and capacity_kwh >= 0 and state_of_health between 0 and 100")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Vin vin;

    @Embedded
    private LicensePlate licensePlate;

    @Convert(converter = TeslaModelConverter.class)
    @Column(name = "model_code", nullable = false, length = 2)
    private TeslaModel model;

    @Column(name = "first_registration")
    private LocalDate firstRegistration;

    @Embedded
    private Mileage mileage;

    @Embedded
    private SwVersion softwareVersion;

    @Embedded
    private BatteryPack battery;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "owner_id", nullable = false)
    private Owner owner;

    protected Vehicle() {
    }

    public Vehicle(
            Vin vin,
            LicensePlate licensePlate,
            TeslaModel model,
            LocalDate firstRegistration,
            Mileage mileage,
            SwVersion softwareVersion,
            BatteryPack battery,
            Owner owner
    ) {
        this.vin = Objects.requireNonNull(vin, "vin");
        this.licensePlate = licensePlate;
        this.model = Objects.requireNonNull(model, "model");
        this.firstRegistration = firstRegistration;
        this.mileage = Objects.requireNonNull(mileage, "mileage");
        this.softwareVersion = softwareVersion;
        this.battery = Objects.requireNonNull(battery, "battery");
        this.owner = Objects.requireNonNull(owner, "owner");
    }

    public Long getId() {
        return id;
    }

    public Vin getVin() {
        return vin;
    }

    public LicensePlate getLicensePlate() {
        return licensePlate;
    }

    public TeslaModel getModel() {
        return model;
    }

    public LocalDate getFirstRegistration() {
        return firstRegistration;
    }

    public Mileage getMileage() {
        return mileage;
    }

    public SwVersion getSoftwareVersion() {
        return softwareVersion;
    }

    public BatteryPack getBattery() {
        return battery;
    }

    public Owner getOwner() {
        return owner;
    }
}
