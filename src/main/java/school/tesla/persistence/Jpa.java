package school.tesla.persistence;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Map;

public final class Jpa {
    private Jpa() {
    }

    public static EntityManagerFactory open(String unitName) {
        return Persistence.createEntityManagerFactory(
                unitName, Map.of("hibernate.show_sql", Boolean.getBoolean("tesla.sql")));
    }
}
