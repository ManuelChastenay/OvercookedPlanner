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

    public List<Task> getOtherPreviousTasks(Character character, int startTime){
        Map<Character, Task> otherEndedTasks = new HashMap<>();
        for(Task task : tasks){
            if(task.getCharacter() != character && (
                    otherEndedTasks.get(task.getCharacter()) == null ||
                            //TODO à vérifier
                            (otherEndedTasks.get(task.getCharacter()).getStartTime() < task.getStartTime() &&
                            task.getStartTime() + task.getDuration() < startTime)
            )){
                otherEndedTasks.put(task.getCharacter(), task);
            }
        }

        List<Task> otherPreviousTasks = new ArrayList<>();
        for(Map.Entry<Character, Task> entry : otherEndedTasks.entrySet()){
            otherPreviousTasks.addAll(entry.getValue().getPreviousTasks());
            otherPreviousTasks.add(entry.getValue());
        }

        return otherPreviousTasks;
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
