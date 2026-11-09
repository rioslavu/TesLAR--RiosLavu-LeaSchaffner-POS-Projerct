package school.tesla;

import jakarta.persistence.EntityManager;
import school.tesla.charging.ChargingSession;
import school.tesla.charging.ChargingStall;
import school.tesla.charging.ConnectorType;
import school.tesla.charging.SuperchargerStation;
import school.tesla.identity.Owner;
import school.tesla.identity.Technician;
import school.tesla.persistence.Jpa;
import school.tesla.service.AppointmentStatus;
import school.tesla.service.ServiceAppointment;
import school.tesla.service.ServiceCenter;
import school.tesla.service.ServiceTask;
import school.tesla.service.ServiceType;
import school.tesla.value.Address;
import school.tesla.value.Amount;
import school.tesla.value.CenterName;
import school.tesla.value.CountryCode;
import school.tesla.value.Currency;
import school.tesla.value.Description;
import school.tesla.value.Email;
import school.tesla.value.EmployeeNo;
import school.tesla.value.EnergyKwh;
import school.tesla.value.FirstName;
import school.tesla.value.GeoCoordinate;
import school.tesla.value.LastName;
import school.tesla.value.Latitude;
import school.tesla.value.LicenseNumber;
import school.tesla.value.LicensePlate;
import school.tesla.value.Longitude;
import school.tesla.value.Mileage;
import school.tesla.value.Money;
import school.tesla.value.Percentage;
import school.tesla.value.PhoneNumber;
import school.tesla.value.PowerKw;
import school.tesla.value.StallNo;
import school.tesla.value.StationName;
import school.tesla.value.SwVersion;
import school.tesla.value.Vin;
import school.tesla.vehicle.BatteryChemistry;
import school.tesla.vehicle.BatteryPack;
import school.tesla.vehicle.TeslaModel;
import school.tesla.vehicle.Vehicle;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public final class Demo {
    private Demo() {
    }

    public static void main(String[] args) {
        try (var factory = Jpa.open("tesla"); var em = factory.createEntityManager()) {
            var transaction = em.getTransaction();
            transaction.begin();
            SampleIds sample;
            try {
                sample = persistSample(em);
                transaction.commit();
            } catch (RuntimeException e) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw e;
            }

            em.clear();
            var appointment = em.find(ServiceAppointment.class, sample.appointmentId());
            var session = em.find(ChargingSession.class, sample.sessionId());
            System.out.printf("Vehicle %s (%s), appointment %s, %d service tasks%n",
                    appointment.getVehicle().getVin().value(), appointment.getVehicle().getModel(),
                    appointment.getStatus(), appointment.getTasks().size());
            System.out.printf("Charging at %s, stall %s, %.1f kWh, %s %s%n",
                    session.getStation().getName().value(), session.getStallNo().value(),
                    session.getEnergyCharged().value(), session.getTotalCost().amount().value(),
                    session.getTotalCost().currency());
        }
    }

    static SampleIds persistSample(EntityManager em) {
        var address = new Address("Example Street 12", "1011", "Budapest", new CountryCode("HU"));
        var owner = new Owner(new FirstName("Alex"), new LastName("Kovacs"),
                new Email("alex@example.com"), new LicenseNumber("DL-123456"),
                new PhoneNumber("+3612345678"), address);
        var center = new ServiceCenter(new CenterName("Budapest Service"), address);
        var technician = new Technician(new FirstName("Sam"), new LastName("Nagy"),
                new Email("sam@example.com"), new EmployeeNo("EMP00001"),
                Set.of(TeslaModel.MODEL_S, TeslaModel.MODEL_Lpromax), center);
        var vehicle = new Vehicle(new Vin("5YJSA1E26JF000001"), new LicensePlate("ABCD-123"),
                TeslaModel.MODEL_Lpromax, LocalDate.of(2024, 4, 12), new Mileage(18_450),
                new SwVersion("2026.44.2"),
                new BatteryPack(BatteryChemistry.NMC, new EnergyKwh(100), new Percentage(94)), owner);

        var start = LocalDateTime.of(2026, 10, 24, 9, 0);
        var tasks = List.of(
                new ServiceTask(ServiceType.TIRE_CHANGE, new Description("Change winter tires"),
                        Duration.ofMinutes(40).plusNanos(123), eur("40.00")),
                new ServiceTask(ServiceType.INSPECTION, null, null, eur("15.00")));
        var appointment = new ServiceAppointment(start, AppointmentStatus.CONFIRMED, tasks,
                center, vehicle, technician);

        var stalls = List.of(
                new ChargingStall(new StallNo("2B"), ConnectorType.NACS, new PowerKw(250)),
                new ChargingStall(new StallNo("1A"), ConnectorType.CCS2, new PowerKw(150)));
        var station = new SuperchargerStation(new StationName("Budapest Supercharger"), address,
                new GeoCoordinate(new Latitude(47.4979), new Longitude(19.0402)), eur("0.4200"), stalls);
        var session = new ChargingSession(new StallNo("2B"), start.plusHours(2),
                start.plusHours(2).plusMinutes(20), new EnergyKwh(20.5), station, vehicle, eur("8.61"));

        em.persist(owner);
        em.persist(center);
        em.persist(technician);
        em.persist(vehicle);
        em.persist(appointment);
        em.persist(station);
        em.persist(session);

        return new SampleIds(owner.getId(), technician.getId(), vehicle.getId(), center.getId(),
                appointment.getId(), station.getId(), session.getId());
    }

    private static Money eur(String amount) {
        return new Money(new Amount(new BigDecimal(amount)), Currency.EUR);
    }

    record SampleIds(Long ownerId, Long technicianId, Long vehicleId, Long centerId,
                     Long appointmentId, Long stationId, Long sessionId) {
    }
}
