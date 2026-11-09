package school.tesla.service;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import school.tesla.identity.Technician;
import school.tesla.persistence.AppointmentStatusConverter;
import school.tesla.vehicle.Vehicle;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "service_appointments")
public class ServiceAppointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "scheduled_at", nullable = false)
    private LocalDateTime scheduledAt;

    @Convert(converter = AppointmentStatusConverter.class)
    @Column(name = "status_code", nullable = false, length = 4)
    private AppointmentStatus status;

    @ElementCollection
    @CollectionTable(name = "appointment_tasks", joinColumns = @JoinColumn(name = "appointment_id"))
    @OrderColumn(name = "task_order", nullable = false)
    private List<ServiceTask> tasks = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "service_center_id", nullable = false)
    private ServiceCenter serviceCenter;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "technician_id")
    private Technician technician;

    protected ServiceAppointment() {
    }

    public ServiceAppointment(
            LocalDateTime scheduledAt,
            AppointmentStatus status,
            List<ServiceTask> tasks,
            ServiceCenter serviceCenter,
            Vehicle vehicle,
            Technician technician
    ) {
        this.scheduledAt = Objects.requireNonNull(scheduledAt, "scheduledAt");
        this.status = Objects.requireNonNull(status, "status");
        this.tasks = new ArrayList<>(List.copyOf(tasks));
        this.serviceCenter = Objects.requireNonNull(serviceCenter, "serviceCenter");
        this.vehicle = Objects.requireNonNull(vehicle, "vehicle");
        this.technician = technician;
        if (this.tasks.isEmpty()) {
            throw new IllegalArgumentException("tasks must contain at least one item");
        }
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public AppointmentStatus getStatus() {
        return status;
    }

    public List<ServiceTask> getTasks() {
        return List.copyOf(tasks);
    }

    public ServiceCenter getServiceCenter() {
        return serviceCenter;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public Technician getTechnician() {
        return technician;
    }
}
