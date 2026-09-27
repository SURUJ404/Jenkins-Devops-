package com.example.taskapi;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread-safe in-memory store for tasks. Kept dependency-free on purpose
 * so the whole app builds and runs with nothing but the JDK.
 */
public class TaskStore {
    private final Map<Integer, Task> tasks = new ConcurrentHashMap<>();
    private final AtomicInteger nextId = new AtomicInteger(1);

    public Task add(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("title must not be blank");
        }
        int id = nextId.getAndIncrement();
        Task task = new Task(id, title.trim());
        tasks.put(id, task);
        return task;
    }

    public Collection<Task> list() {
        return tasks.values();
    }

    public boolean complete(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            return false;
        }
        task.setDone(true);
        return true;
    }

    public boolean delete(int id) {
        return tasks.remove(id) != null;
    }

    public int size() {
        return tasks.size();
    }
}
