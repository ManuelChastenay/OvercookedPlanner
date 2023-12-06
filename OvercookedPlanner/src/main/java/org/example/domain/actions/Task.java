package org.example.domain.actions;

import org.example.domain.Recipe;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRange;
import org.optaplanner.core.api.domain.valuerange.ValueRangeFactory;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.util.ArrayList;
import java.util.List;

@PlanningEntity
public class Task{
    private String taskName;
    private Recipe currentRecipe;
    private Task dependentTask;

    private Boolean incomingItem; //True si la tâche fais passer le character de main vides à main pleines, false sinon.
    private Boolean outcomingItem; //True si la tâche fais passer le character de main pleines à main vides, false sinon.

    @PlanningVariable(valueRangeProviderRefs = {"finishedRange"})
    private Boolean isFinished;

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

    @ValueRangeProvider(id = "finishedRange")
    public ValueRange<Boolean> getFinishedRange() {
        return ValueRangeFactory.createBooleanValueRange();
    }

    public String getTaskName() {
        return taskName;
    }

    public Recipe getCurrentRecipe() {
        return currentRecipe;
    }

    public void setCurrentRecipe(Recipe currentRecipe) {
        this.currentRecipe = currentRecipe;
    }

    public List<Task> getDependencies() {
        if(dependentTask == null) return null;

        List<Task> dependencies = new ArrayList<>();
        dependencies.add(dependentTask);
        return dependencies;
    }

    public Boolean areDependenciesFinished(){
        if(dependentTask == null) return true;
        if(isFinished == null) return false;
        return dependentTask.isFinished();
    }

    public Boolean isFinished(){
        if(isFinished == null) return false;
        return isFinished;
    }

    public boolean hasIncoming(){
        return incomingItem;
    }

    public boolean hasOutcoming(){
        return outcomingItem;
    }
}
