package hr.fer.zemris.java.gui.prim;

import org.junit.jupiter.api.Test;

import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrimListModelTest {
    @Test
    public void testNextAndGetPrimes() {
        List<Integer> primes = List.of(1, 2, 3, 5, 7, 11, 13, 17, 19);
        PrimListModel model = new PrimListModel();
        for (int i = 0; i < primes.size() - 1; i++) {
            model.next();
        }
        assertEquals(primes, model.getPrimes());
    }

    @Test
    public void testGetElementAt() {
        List<Integer> primes = List.of(1, 2, 3, 5, 7, 11, 13, 17, 19);
        PrimListModel model = new PrimListModel();
        for (int i = 0; i < primes.size() - 1; i++) {
            model.next();
        }
        for (int i = 0; i < primes.size(); i++) {
            assertEquals(primes.get(i), model.getElementAt(i));
        }
    }

    @Test
    public void testGetSize() {
        List<Integer> primes = List.of(1, 2, 3, 5, 7, 11, 13, 17, 19);
        PrimListModel model = new PrimListModel();
        for (int i = 0; i < primes.size() - 1; i++) {
            model.next();
        }
        assertEquals(primes.size(), model.getSize());
    }

    @Test
    public void testAddListDataListener() {
        PrimListModel model = new PrimListModel();
        model.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                assertEquals(1, e.getIndex0());
                assertEquals(1, e.getIndex1());
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {
                fail();
            }

            @Override
            public void contentsChanged(ListDataEvent e) {
                fail();
            }
        });
        model.next();
    }
}
