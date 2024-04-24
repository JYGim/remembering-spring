package kr.co.felici.remembering.domain;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.Test;

/**
 * author: felici
 */
public class LetterTest {

    @Test
    public void whenPersistEntitiesWithOneToManyAssociation_thenSuccess() {

        Letter letter = new Letter();
        letter.addContents("hello");
        BoardImage image = BoardImage.builder()
                .originalFilename("test.jpg")
                .path("/images/33ee.jpg")
                .build();

        image.setLetter(letter);

//
//        Session session = sessionFactory.openSession();
//        session.beginTransaction();
//        session.persist(image);
//        session.getTransaction().commit();
//        session.close();

    }


}
