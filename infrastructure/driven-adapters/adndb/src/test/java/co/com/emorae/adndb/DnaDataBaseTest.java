package co.com.emorae.adndb;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.ConcurrentHashMap;

@ExtendWith(MockitoExtension.class)
class DnaDataBaseTest {

    private DnaDataBase db = new DnaDataBase(new ConcurrentHashMap<>());

    @Test
    void addSequence() {
        db.addSequence("anyHuman", false);
        Assertions.assertEquals(1, db.getAll().size());
    }

    @Test
    void findSequence() {
        db.addSequence("anyHuman", false);
        Assertions.assertEquals(false, db.findSequence("anyHuman"));
    }

    @Test
    void getAll() {
        Assertions.assertEquals(0, db.getAll().size());
    }
}