package school.tesla.lesson;

import org.junit.jupiter.api.Test;
import school.tesla.persistence.Jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class PhotoPersistenceTest {
    @Test
    void savesTheLessonCodesAndPreservesNull() {
        try (var factory = Jpa.open("photo-lesson"); var em = factory.createEntityManager()) {
            var photo = new Photo("Holiday.jpg", Orientation.LANDSCAPE);
            var unknownOrientation = new Photo("Unsorted.jpg", null);

            em.getTransaction().begin();
            em.persist(photo);
            em.persist(unknownOrientation);
            em.getTransaction().commit();
            em.clear();

            var code = em.createNativeQuery("select orientation from photos where id = ?1")
                    .setParameter(1, photo.getId()).getSingleResult();
            assertEquals("L", code.toString());
            assertEquals(Orientation.LANDSCAPE, em.find(Photo.class, photo.getId()).getOrientation());
            assertNull(em.find(Photo.class, unknownOrientation.getId()).getOrientation());
        }
    }
}
