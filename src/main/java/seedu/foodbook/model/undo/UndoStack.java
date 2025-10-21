package seedu.foodbook.model.undo;

import java.util.ArrayDeque;
import java.util.Deque;

import seedu.foodbook.model.undo.exceptions.NoMoreUndoException;

public class UndoStack<T> {

    private final Integer MAX_CAPACITY = 3;

    private final Deque<T> stack;

    public UndoStack() {
        this.stack = new ArrayDeque<>();
    }

    public void checkpoint(T snapshot) {
        if (stack.size() == MAX_CAPACITY) {
            stack.removeFirst();
        }
        stack.addLast(snapshot);
    }

    public T undo() throws NoMoreUndoException {
        if (stack.isEmpty()) {
            throw new NoMoreUndoException();
        }
        return stack.pollLast();
    }
}
