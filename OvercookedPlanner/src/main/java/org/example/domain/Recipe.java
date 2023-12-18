package org.example.domain;

import org.example.domain.actions.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<Task> getAllPreviousTasks(int startTime){
        Map<Character, Task> lastEndedTasks = new HashMap<>();

        for(Task task : tasks){
            if(lastEndedTasks.get(task.getCharacter()) == null ||
                (lastEndedTasks.get(task.getCharacter()).getStartTime() <= task.getStartTime() &&
                task.getStartTime() < startTime)
            ){
                lastEndedTasks.put(task.getCharacter(), task);
            }
        }

        List<Task> allPreviousTasks = new ArrayList<>();
        for(Map.Entry<Character, Task> entry : lastEndedTasks.entrySet()){
            allPreviousTasks.addAll(entry.getValue().getPreviousTasks());
            allPreviousTasks.add(entry.getValue());
        }

        return allPreviousTasks;
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
