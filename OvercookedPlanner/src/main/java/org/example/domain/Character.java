package org.example.domain;

import org.example.domain.actions.Task;

public class Character extends CharacterOrTaskAssignment {
    private String id;
    private Task currentTask;

    public Character(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    public void setCurrentTask(Task currentTask) {
        this.currentTask = currentTask;
    }
}
