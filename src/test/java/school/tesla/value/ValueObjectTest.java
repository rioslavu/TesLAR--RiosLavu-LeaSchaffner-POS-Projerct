package school.tesla.value;

import org.junit.jupiter.api.Test;
import school.tesla.charging.SuperchargerStation;
import school.tesla.identity.Owner;
import school.tesla.service.AppointmentStatus;
import school.tesla.service.ServiceAppointment;
import school.tesla.service.ServiceCenter;
import school.tesla.vehicle.BatteryChemistry;
import school.tesla.vehicle.BatteryPack;
import school.tesla.vehicle.TeslaModel;
import school.tesla.vehicle.Vehicle;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ValueObjectTest {
    @Test
    void validatesTheUmlLengthsPatternsAndRanges() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> new FirstName("x".repeat(65))),
                () -> assertThrows(IllegalArgumentException.class, () -> new Email("missing-at.example")),
                () -> assertThrows(IllegalArgumentException.class, () -> new PhoneNumber("003612345678")),
                () -> assertThrows(IllegalArgumentException.class, () -> new Vin("5YJSA1E26JF00000O")),
                () -> assertThrows(IllegalArgumentException.class, () -> new SwVersion("2026.54.1")),
                () -> assertThrows(IllegalArgumentException.class, () -> new StallNo("3E")),
                () -> assertThrows(IllegalArgumentException.class, () -> new CountryCode("ZZ")),
                () -> assertThrows(IllegalArgumentException.class, () -> new Mileage(-1)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Percentage(101)),
                () -> assertThrows(IllegalArgumentException.class, () -> new PowerKw(0)),
                () -> assertThrows(IllegalArgumentException.class, () -> new EnergyKwh(Double.NaN)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Latitude(-90.01)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Longitude(180.01)),
                () -> assertThrows(IllegalArgumentException.class, () -> new Amount(new BigDecimal("-0.01"))),
                () -> assertDoesNotThrow(() -> new Latitude(-90)),
                () -> assertDoesNotThrow(() -> new PowerKw(500)),
                () -> assertDoesNotThrow(() -> new Percentage(100))
        );
    }

    @Test
    void requiresAtLeastOneTaskAndStall() {
        var address = new Address("Example Street 12", "1011", "Budapest", new CountryCode("HU"));
        var center = new ServiceCenter(new CenterName("Budapest Service"), address);
        var owner = new Owner(new FirstName("Alex"), new LastName("Kovacs"),
                new Email("alex@example.com"), new LicenseNumber("DL-123456"), null, address);
        var battery = new BatteryPack(BatteryChemistry.NMC, new EnergyKwh(100), new Percentage(94));
        var vehicle = new Vehicle(new Vin("5YJSA1E26JF000001"), null, TeslaModel.MODEL_S,
                null, new Mileage(0), null, battery, owner);
        var location = new GeoCoordinate(new Latitude(47.4979), new Longitude(19.0402));
        var price = new Money(new Amount(new BigDecimal("0.42")), Currency.EUR);

        assertThrows(IllegalArgumentException.class,
                () -> new ServiceAppointment(LocalDateTime.of(2026, 10, 24, 9, 0), AppointmentStatus.REQUESTED,
                        List.of(), center, vehicle, null));
        assertThrows(IllegalArgumentException.class,
                () -> new SuperchargerStation(new StationName("Budapest Supercharger"), address,
                        location, price, List.of()));
    }
}
