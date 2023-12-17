package org.example.domain;

import org.example.domain.actions.Task;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String recipeName;
    private Integer recipeStartTime;

    private List<Task> tasks = new ArrayList<>();

    public Recipe() {
    }
    public Recipe(String recipeName, Integer recipeStartTime) {
        this.recipeName = recipeName;
        this.recipeStartTime = recipeStartTime;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public List<Task> getEndedTasks(int startTime){
        List<Task> endedTasks = new ArrayList<>();
        for(Task task : tasks){
            if(task.getStartTime() + task.getDuration() < startTime) endedTasks.add(task);
        }
        return endedTasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        int id = 0;
        for (Task task : tasks) {
            task.setCurrentRecipe(this);
            task.setId(id++);
        }
    }

    public Integer getStartTime(){
        return recipeStartTime;
    }
}
