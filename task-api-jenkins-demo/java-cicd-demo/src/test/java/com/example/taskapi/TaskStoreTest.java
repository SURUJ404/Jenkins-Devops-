package com.example.taskapi;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskStoreTest {

    private TaskStore store;

    @BeforeEach
    void setUp() {
        store = new TaskStore();
    }

    @Test
    void addAssignsIncrementingIds() {
        Task first = store.add("write pipeline");
        Task second = store.add("build docker image");

        assertEquals(1, first.getId());
        assertEquals(2, second.getId());
        assertEquals(2, store.size());
    }

    @Test
    void addRejectsBlankTitle() {
        assertThrows(IllegalArgumentException.class, () -> store.add("  "));
    }

    @Test
    void completeMarksExistingTaskDone() {
        Task task = store.add("run tests");

        assertTrue(store.complete(task.getId()));
        assertTrue(task.isDone());
    }

    @Test
    void completeReturnsFalseForUnknownId() {
        assertFalse(store.complete(999));
    }

    @Test
    void deleteRemovesTask() {
        Task task = store.add("deploy");

        assertTrue(store.delete(task.getId()));
        assertEquals(0, store.size());
        assertFalse(store.delete(task.getId()));
    }
}
