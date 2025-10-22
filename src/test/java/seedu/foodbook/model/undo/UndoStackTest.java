package seedu.foodbook.model.undo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static seedu.foodbook.testutil.Assert.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import seedu.foodbook.model.undo.exceptions.NoMoreUndoException;

class UndoStackTest {

    @Test
    @DisplayName("Undo on empty stack throws NoMoreUndoException")
    void undo_empty_throws() {
        UndoStack<Integer> stack = new UndoStack<>();
        assertThrows(NoMoreUndoException.class, stack::undo);
    }

    @Test
    @DisplayName("Single checkpoint then undo returns the same instance")
    void checkpoint_thenUndo_returnsSameInstance() throws Exception {
        UndoStack<String> stack = new UndoStack<>();
        String snap = "S0";
        stack.checkpoint(snap);
        assertSame(snap, stack.undo());
        assertThrows(NoMoreUndoException.class, stack::undo);
    }

    @Test
    @DisplayName("LIFO ordering: last checkpoint is undone first")
    void lifoOrdering_basic() throws Exception {
        UndoStack<Integer> stack = new UndoStack<>();
        stack.checkpoint(1);
        stack.checkpoint(2);
        stack.checkpoint(3);
        assertEquals(3, stack.undo());
        assertEquals(2, stack.undo());
        assertEquals(1, stack.undo());
        assertThrows(NoMoreUndoException.class, stack::undo);
    }

    @Test
    @DisplayName("Capacity trimming (MAX=3): oldest is dropped when exceeding capacity")
    void capacityLimit_keepsMostRecentThree() throws Exception {
        UndoStack<String> stack = new UndoStack<>();
        // Push 6 snapshots; capacity is 5 → keep only the last five
        stack.checkpoint("A");
        stack.checkpoint("B");
        stack.checkpoint("C");
        stack.checkpoint("D");
        stack.checkpoint("E");
        stack.checkpoint("F");
        stack.checkpoint("G");

        assertEquals("G", stack.undo());
        assertEquals("F", stack.undo());
        assertEquals("E", stack.undo());
        assertEquals("D", stack.undo());
        assertEquals("C", stack.undo());
        // A was trimmed; stack is empty now
        assertThrows(NoMoreUndoException.class, stack::undo);
    }

    @Test
    @DisplayName("Interleaving checkpoint/undo across capacity works correctly")
    void interleave_checkpointUndo_correctOrder() throws Exception {
        UndoStack<String> stack = new UndoStack<>();

        stack.checkpoint("S1"); // [S1]
        stack.checkpoint("S2"); // [S1, S2]
        assertEquals("S2", stack.undo()); // -> [S1]

        stack.checkpoint("S3"); // [S1, S3]
        stack.checkpoint("S4"); // [S1, S3, S4]
        stack.checkpoint("S5"); // capacity reached: drop S1 → [S3, S4, S5]

        assertEquals("S5", stack.undo()); // -> [S3, S4]
        assertEquals("S4", stack.undo()); // -> [S3]
        assertEquals("S3", stack.undo()); // -> []
    }

    @Test
    @DisplayName("Pushing far beyond capacity still yields the last three only")
    void manyCheckpoints_onlyLastThreeRemain() throws Exception {
        UndoStack<Integer> stack = new UndoStack<>();
        for (int i = 0; i < 100; i++) {
            stack.checkpoint(i);
        }
        // Only 97, 98, 99 should remain (in that LIFO undo order)
        assertEquals(99, stack.undo());
        assertEquals(98, stack.undo());
        assertEquals(97, stack.undo());
        assertEquals(96, stack.undo());
        assertEquals(95, stack.undo());
        assertThrows(NoMoreUndoException.class, stack::undo);
    }

    // Optional: show it works with complex/generic types (reference identity preserved)
    static final class Snapshot {
        final int id;
        Snapshot(int id) {
            this.id = id;
        }
    }

    @Test
    @DisplayName("Generic type: preserves reference identity for complex snapshots")
    void genericType_referenceIdentityPreserved() throws Exception {
        UndoStack<Snapshot> stack = new UndoStack<>();
        Snapshot s1 = new Snapshot(1);
        Snapshot s2 = new Snapshot(2);
        stack.checkpoint(s1);
        stack.checkpoint(s2);
        assertSame(s2, stack.undo());
        assertSame(s1, stack.undo());
        assertThrows(NoMoreUndoException.class, stack::undo);
    }
}
