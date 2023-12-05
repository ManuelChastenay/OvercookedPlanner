package org.example.domain.actions;

import org.example.domain.Recipe;
import org.example.domain.TaskAssignment;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRangeFactory;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.score.director.ScoreDirector;
import org.optaplanner.core.impl.heuristic.move.AbstractMove;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@PlanningEntity
public class Task{
    private String taskName;
    private boolean incomingItem; //True si la tâche fais passer le character de main vides à main pleines, false sinon.
    private boolean outcomingItem; //True si la tâche fais passer le character de main pleines à main vides, false sinon.

    private Recipe currentRecipe;

    @PlanningVariable(valueRangeProviderRefs = {"taskRange"})
    private Task dependentTask;

    //TODO Faire fonctionner les choix du solveur sur cette variable
    //@PlanningVariable(valueRangeProviderRefs = {"isFinished"})
    private Boolean isFinished = true;

    public Task(){
    }

    public Task(String taskName, boolean incomingItem, boolean outcomingItem) {
        this.taskName = taskName;
        this.incomingItem = incomingItem;
        this.outcomingItem = outcomingItem;
    }

    public Task(String taskName, Task dependentTask, boolean incomingItem, boolean outcomingItem){
        this.taskName = taskName;
        this.dependentTask = dependentTask;
        this.incomingItem = incomingItem;
        this.outcomingItem = outcomingItem;
    }

    @ValueRangeProvider(id = "taskRange")
    public List<Task> getPossibleTaskList() {
        // get all tasks in the recipe that are not the current task
        return currentRecipe.getTasks().stream()
                .filter(task -> task != this)
                .collect(Collectors.toList());
    }

    /*@ValueRangeProvider(id = "isFinished")
    public List<Boolean> getPossibleFinishedStates() {
        List<Boolean> booleans = new ArrayList<>();
        booleans.add(true);
        booleans.add(false);
        return booleans;
    }*/

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public Recipe getCurrentRecipe() {
        return currentRecipe;
    }

    public void setCurrentRecipe(Recipe currentRecipe) {
        this.currentRecipe = currentRecipe;
    }

    public Task getDependentTask() {
        return dependentTask;
    }

    public void setDependentTask(Task dependentTask) {
        this.dependentTask = dependentTask;
    }

    public boolean isFinished(){
        return isFinished;
    }

    public void setFinished(boolean isFinished) {
        this.isFinished = isFinished;
    }

    public boolean hasIncoming(){
        return incomingItem;
    }

    public boolean hasOutcoming(){
        return outcomingItem;
    }
}
