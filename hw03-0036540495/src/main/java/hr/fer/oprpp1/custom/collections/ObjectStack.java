package hr.fer.oprpp1.custom.collections;

/**
 * A class representing and with functionality of a stack of objects.
 *
 * @param <T> type of elements stored in the stack
 *
 * @see ArrayIndexedCollection
 * @see EmptyStackException
 *
 * @version 2.0
 * @author Marko Šelendić
 */
public class ObjectStack<T> {

    /**
     * Collection used for storing elements of the stack.
     */
    private final ArrayIndexedCollection<T> collection;

    /**
     * Creates an empty stack.
     */
    public ObjectStack() {
        this.collection = new ArrayIndexedCollection<>();
    }

    /**
     * Returns true only if the stack contains no objects.
     *
     * @return true if the stack contains no objects, false otherwise
     */
    public boolean isEmpty() {
        return this.collection.isEmpty();
    }

    /**
     * Returns the number of currently stored objects in this stack.
     *
     * @return number of currently stored objects
     */
    public int size() {
        return this.collection.size();
    }

    /**
     * Pushes the given value to the top of the stack.
     *
     * @param value value to be pushed to the top of the stack
     * @throws NullPointerException if the given value is null
     */
    public void push(T value) {
        this.collection.add(value);
    }

    /**
     * Removes the value from the top of the stack and returns it.
     *
     * @return last value pushed on the stack
     * @throws EmptyStackException if the stack is empty
     */
    public T pop() {
        if (this.isEmpty()) {
            throw new EmptyStackException("The stack is empty.");
        }

        T popped = this.collection.get(this.size() - 1);
        this.collection.remove(this.size() - 1);
        return popped;
    }

    /**
     * Returns the value from the top of the stack without removing it.
     *
     * @return last element placed on the stack
     * @throws EmptyStackException if the stack is empty
     */
    public T peek() {
        if (this.isEmpty()) {
            throw new EmptyStackException("The stack is empty.");
        }

        return this.collection.get(this.size() - 1);
    }

    /**
     * Removes all elements from the stack.
     */
    public void clear() {
        this.collection.clear();
    }
}
