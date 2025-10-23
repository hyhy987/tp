package seedu.foodbook.model.undo;

import java.util.ArrayDeque;
import java.util.Deque;

import seedu.foodbook.model.undo.exceptions.NoMoreUndoException;

/**
 * Data Structure that controls record management for undo
 * @param <T> The type of record to be stored
 */
public class UndoStack<T> {

    /**
     * The Maximum capacity of the stack
     */
    private static final Integer MAX_CAPACITY = 5;

    /**
     * The deq object used to store state
     */
    private final Deque<T> stack;

    public UndoStack() {
        this.stack = new ArrayDeque<>();
    }

    /**
     * Adds a new record to the stack
     * If the stack is at max capcity, removes the oldest record
     * @param snapshot The new record to add
     */
    public void checkpoint(T snapshot) {
        if (stack.size() == MAX_CAPACITY) {
            stack.removeFirst();
        }
        stack.addLast(snapshot);
    }

    /**
     * Returns the most recent state added
     * @return The most recent state
     * @throws NoMoreUndoException If no more undo states are left
     */
    public T undo() throws NoMoreUndoException {
        if (stack.isEmpty()) {
            throw new NoMoreUndoException();
        }
        return stack.pollLast();
    }
}
