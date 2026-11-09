package school.tesla;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import school.tesla.charging.ChargingSession;
import school.tesla.charging.ChargingStall;
import school.tesla.charging.SuperchargerStation;
import school.tesla.identity.Owner;
import school.tesla.identity.Technician;
import school.tesla.identity.User;
import school.tesla.persistence.DataQualityException;
import school.tesla.persistence.Jpa;
import school.tesla.service.AppointmentStatus;
import school.tesla.service.ServiceAppointment;
import school.tesla.service.ServiceTask;
import school.tesla.service.ServiceType;
import school.tesla.value.Currency;
import school.tesla.value.EnergyKwh;
import school.tesla.value.Mileage;
import school.tesla.value.StallNo;
import school.tesla.value.Vin;
import school.tesla.vehicle.BatteryChemistry;
import school.tesla.vehicle.TeslaModel;
import school.tesla.vehicle.Vehicle;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PersistenceTest {
    private static EntityManagerFactory factory;

    @BeforeAll
    static void openDatabase() {
        factory = Jpa.open("tesla");
    }

    @AfterAll
    static void closeDatabase() {
        if (factory != null) {
            factory.close();
        }
    }

    @Test
    void savesAndReloadsTheDomainGraph() {
        var sample = seed();

        try (var em = factory.createEntityManager()) {
            var owner = assertInstanceOf(Owner.class, em.find(User.class, sample.ownerId()));
            assertEquals("HU", owner.getHomeAddress().country().value());
            assertEquals("+3612345678", owner.getPhone().value());

            var technician = assertInstanceOf(Technician.class, em.find(User.class, sample.technicianId()));
            assertEquals(sample.centerId(), technician.getWorksAt().getId());
            assertEquals(Set.of(TeslaModel.MODEL_S, TeslaModel.MODEL_Lpromax), technician.getCertifiedFor());

            var vehicle = em.find(Vehicle.class, sample.vehicleId());
            assertEquals(sample.ownerId(), vehicle.getOwner().getId());
            assertEquals(TeslaModel.MODEL_Lpromax, vehicle.getModel());
            assertEquals(BatteryChemistry.NMC, vehicle.getBattery().chemistry());
            assertEquals(18_450, vehicle.getMileage().value());

            var appointment = em.find(ServiceAppointment.class, sample.appointmentId());
            assertEquals(sample.vehicleId(), appointment.getVehicle().getId());
            assertEquals(sample.centerId(), appointment.getServiceCenter().getId());
            assertEquals(sample.technicianId(), appointment.getTechnician().getId());
            assertEquals(AppointmentStatus.CONFIRMED, appointment.getStatus());
            assertEquals(List.of(ServiceType.TIRE_CHANGE, ServiceType.INSPECTION),
                    appointment.getTasks().stream().map(ServiceTask::type).toList());
            assertEquals(Duration.ofMinutes(40).plusNanos(123), appointment.getTasks().getFirst().laborTime());
            assertNull(appointment.getTasks().getLast().description());

            var station = em.find(SuperchargerStation.class, sample.stationId());
            assertEquals(List.of(new StallNo("2B"), new StallNo("1A")),
                    station.getStalls().stream().map(ChargingStall::stallNo).toList());
            assertEquals(new BigDecimal("0.4200"), station.getPricePerKwh().amount().value());
            assertEquals(47.4979, station.getLocation().latitude().value());

            var session = em.find(ChargingSession.class, sample.sessionId());
            assertEquals(sample.vehicleId(), session.getVehicle().getId());
            assertEquals(sample.stationId(), session.getStation().getId());
            assertEquals(20.5, session.getEnergyCharged().value());
            assertEquals(Currency.EUR, session.getTotalCost().currency());
            assertEquals(new BigDecimal("8.6100"), session.getTotalCost().amount().value());
        }
    }

    @Test
    void storesEnumCodesInScalarEmbeddedAndCollectionColumns() {
        var sample = seed();

        try (var em = factory.createEntityManager()) {
            assertEquals(List.of("LP", "NMC"), List.of((Object[]) selectOne(em,
                    "select model_code, chemistry_code from vehicles where id = ?1", sample.vehicleId())));
            assertEquals(List.of("LP", "S"), em.createNativeQuery(
                            "select model_code from technician_certifications where technician_id = ?1 order by model_code")
                    .setParameter(1, sample.technicianId()).getResultList());
            assertEquals("CONF", selectOne(em,
                    "select status_code from service_appointments where id = ?1", sample.appointmentId()));
            assertEquals("TIRE", selectOne(em,
                    "select type_code from appointment_tasks where appointment_id = ?1 and task_order = 0",
                    sample.appointmentId()));
            assertEquals("NACS", selectOne(em,
                    "select connector_code from station_stalls where station_id = ?1 and stall_order = 0",
                    sample.stationId()));
            assertEquals("EUR", selectOne(em,
                    "select total_cost_currency from charging_sessions where id = ?1", sample.sessionId()));
        }
    }

    @Test
    void reloadsOptionalFieldsAsNull() {
        var sample = seed();
        var start = LocalDateTime.of(2026, 10, 24, 9, 0);

        try (var em = factory.createEntityManager()) {
            em.getTransaction().begin();
            var original = em.find(ServiceAppointment.class, sample.appointmentId());
            var station = em.getReference(SuperchargerStation.class, sample.stationId());
            var originalOwner = original.getVehicle().getOwner();
            var owner = new Owner(originalOwner.getFirstName(), originalOwner.getLastName(),
                    originalOwner.getEmail(), originalOwner.getDriversLicense(), null, originalOwner.getHomeAddress());
            var vehicle = new Vehicle(new Vin("5YJSA1E26JF000002"), null, TeslaModel.MODEL_S,
                    null, new Mileage(0), null, original.getVehicle().getBattery(), owner);
            var openSession = new ChargingSession(new StallNo("2B"), start, null,
                    new EnergyKwh(0), station, vehicle, null);
            var unassigned = new ServiceAppointment(start, AppointmentStatus.REQUESTED, original.getTasks(),
                    original.getServiceCenter(), vehicle, null);
            em.persist(owner);
            em.persist(vehicle);
            em.persist(openSession);
            em.persist(unassigned);
            em.getTransaction().commit();
            em.clear();

            var session = em.find(ChargingSession.class, openSession.getId());
            assertNull(session.getEndTS());
            assertNull(session.getTotalCost());
            assertNull(session.getVehicle().getLicensePlate());
            assertNull(session.getVehicle().getFirstRegistration());
            assertNull(session.getVehicle().getSoftwareVersion());
            assertNull(session.getVehicle().getOwner().getPhone());
            assertNull(em.find(ServiceAppointment.class, unassigned.getId()).getTechnician());
        }
    }

    @Test
    void rejectsAnUnknownDatabaseCodeOnRead() {
        var sample = seed();

        try (var em = factory.createEntityManager()) {
            em.getTransaction().begin();
            em.createNativeQuery("update vehicles set model_code = 'X' where id = ?1")
                    .setParameter(1, sample.vehicleId()).executeUpdate();
            em.getTransaction().commit();

            var error = assertThrows(PersistenceException.class,
                    () -> em.find(Vehicle.class, sample.vehicleId()));
            var cause = assertInstanceOf(DataQualityException.class, error.getCause());
            assertEquals("Unknown value 'X' for enum of type TeslaModel", cause.getMessage());
        }
    }

    private static Demo.SampleIds seed() {
        try (var em = factory.createEntityManager()) {
            em.getTransaction().begin();
            var sample = Demo.persistSample(em);
            em.getTransaction().commit();
            return sample;
        }
    }

    private static Object selectOne(EntityManager em, String sql, Long id) {
        return em.createNativeQuery(sql).setParameter(1, id).getSingleResult();
    }
}
