package school.tesla.identity;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import school.tesla.value.Email;
import school.tesla.value.FirstName;
import school.tesla.value.LastName;

import java.util.Objects;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private FirstName firstName;

    @Embedded
    private LastName lastName;

    @Embedded
    private Email email;

    protected User() {
    }

    protected User(FirstName firstName, LastName lastName, Email email) {
        this.firstName = Objects.requireNonNull(firstName, "firstName");
        this.lastName = Objects.requireNonNull(lastName, "lastName");
        this.email = Objects.requireNonNull(email, "email");
    }

    public Long getId() {
        return id;
    }

    public FirstName getFirstName() {
        return firstName;
    }

    public LastName getLastName() {
        return lastName;
    }

    public Email getEmail() {
        return email;
    }
}
