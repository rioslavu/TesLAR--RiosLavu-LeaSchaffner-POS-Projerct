package school.tesla.persistence;

import jakarta.persistence.AttributeConverter;
import org.junit.jupiter.api.Test;
import school.tesla.charging.ConnectorType;
import school.tesla.lesson.Orientation;
import school.tesla.lesson.OrientationConverter;
import school.tesla.service.AppointmentStatus;
import school.tesla.service.ServiceType;
import school.tesla.value.Currency;
import school.tesla.vehicle.BatteryChemistry;
import school.tesla.vehicle.TeslaModel;

import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EnumConverterTest {
    @Test
    void teslaModelUsesStableCodes() {
        assertMapping(TeslaModel.class, new TeslaModelConverter(), Map.of(
                TeslaModel.MODEL_S, "S",
                TeslaModel.MODEL_L, "L",
                TeslaModel.MODEL_R, "R",
                TeslaModel.MODEL_Lpromax, "LP"));
    }

    @Test
    void batteryChemistryUsesStableCodes() {
        assertMapping(BatteryChemistry.class, new BatteryChemistryConverter(), Map.of(
                BatteryChemistry.NCA, "NCA",
                BatteryChemistry.NMC, "NMC",
                BatteryChemistry.LFP, "LFP"));
    }

    @Test
    void appointmentStatusUsesStableCodes() {
        assertMapping(AppointmentStatus.class, new AppointmentStatusConverter(), Map.of(
                AppointmentStatus.REQUESTED, "REQ",
                AppointmentStatus.CONFIRMED, "CONF",
                AppointmentStatus.IN_PROGRESS, "PROG",
                AppointmentStatus.COMPLETED, "DONE",
                AppointmentStatus.CANCELLED, "CANC"));
    }

    @Test
    void serviceTypeUsesStableCodes() {
        assertMapping(ServiceType.class, new ServiceTypeConverter(), Map.of(
                ServiceType.INSPECTION, "INSP",
                ServiceType.TIRE_CHANGE, "TIRE",
                ServiceType.BRAKE_SERVICE, "BRAKE",
                ServiceType.BATTERY_CHECK, "BATT",
                ServiceType.SOFTWARE_UPDATE, "SW",
                ServiceType.REPAIR, "REPAIR"));
    }

    @Test
    void connectorTypeUsesStableCodes() {
        assertMapping(ConnectorType.class, new ConnectorTypeConverter(), Map.of(
                ConnectorType.CCS2, "CCS2",
                ConnectorType.NACS, "NACS",
                ConnectorType.TYPE2, "TYPE2"));
    }

    @Test
    void currencyUsesStableCodes() {
        assertMapping(Currency.class, new CurrencyConverter(), Map.of(
                Currency.EUR, "EUR",
                Currency.USD, "USD",
                Currency.CHF, "CHF"));
    }

    @Test
    void orientationUsesStableCodes() {
        assertMapping(Orientation.class, new OrientationConverter(), Map.of(
                Orientation.PORTRAIT, "P",
                Orientation.LANDSCAPE, "L",
                Orientation.SQUARE, "S"));
    }

    private static <E extends Enum<E>> void assertMapping(
            Class<E> type, AttributeConverter<E, String> converter, Map<E, String> codes) {
        assertEquals(Set.of(type.getEnumConstants()), codes.keySet());
        codes.forEach((value, code) -> {
            assertEquals(code, converter.convertToDatabaseColumn(value));
            assertEquals(value, converter.convertToEntityAttribute(code));
        });

        assertNull(converter.convertToDatabaseColumn(null));
        assertNull(converter.convertToEntityAttribute(null));
        var exception = assertThrows(DataQualityException.class,
                () -> converter.convertToEntityAttribute("?"));
        assertEquals("Unknown value '?' for enum of type " + type.getSimpleName(), exception.getMessage());
    }
}
