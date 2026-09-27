package com.example.taskapi;

public class Task {
    private final int id;
    private final String title;
    private boolean done;

    public Task(int id, String title) {
        this.id = id;
        this.title = title;
        this.done = false;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String toJson() {
        return String.format(
            "{\"id\":%d,\"title\":\"%s\",\"done\":%b}",
            id, title.replace("\"", "\\\""), done
        );
    }
}
