package org.example.domain;

import org.example.domain.actions.Task;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String recipeName;

    private List<Task> tasks = new ArrayList<>();

    public Recipe() {
    }
    public Recipe(String recipeName) {
        this.recipeName = recipeName;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        int id = 0;
        for (Task task : tasks) {
            task.setCurrentRecipe(this);
            task.setId(id++);
        }
    }
}
