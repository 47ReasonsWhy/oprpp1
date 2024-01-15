package hr.fer.zemris.java.gui.prim;

import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a model for a list of prime numbers.
 *
 * @see ListModel
 * @see PrimDemo
 *
 * @version 1.0
 * @author Marko Šelendić
 */
public class PrimListModel implements ListModel<Integer> {
    /**
     * List of prime numbers.
     */
    private final List<Integer> primes;

    /**
     * List of listeners.
     */
    private final List<ListDataListener> listeners;

    /**
     * Constructs a new {@link PrimListModel} object.
     */
    public PrimListModel() {
        primes = new ArrayList<>();
        primes.add(1);
        listeners = new ArrayList<>();
    }

    /**
     * Returns the list of prime numbers.
     *
     * @return list of prime numbers
     */
    public List<Integer> getPrimes() {
        return primes;
    }

    /**
     * Returns the next prime number.
     */
    public void next() {
        int current = primes.get(primes.size() - 1);
        int next = current + 1;

        while (!isPrime(next)) {
            next++;
        }

        primes.add(next);

        listeners.forEach(l -> l.intervalAdded(
                new ListDataEvent(this, ListDataEvent.INTERVAL_ADDED, primes.size() - 1, primes.size() - 1)
        ));
    }

    /**
     * Checks if the given number is prime.
     *
     * @param n number to be checked
     * @return true if the number is prime, false otherwise
     */
    private boolean isPrime(int n) {
        if (n <= 1) {
            return false;
        }
        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int getSize() {
        return primes.size();
    }

    @Override
    public Integer getElementAt(int index) {
        return primes.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {
        listeners.add(l);
    }

    @Override
    public void removeListDataListener(ListDataListener l) {
        listeners.remove(l);
    }
}
