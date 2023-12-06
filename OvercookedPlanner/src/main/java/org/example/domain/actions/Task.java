package org.example.domain.actions;

import org.example.domain.Recipe;
import org.example.domain.TaskAssignment;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRange;
import org.optaplanner.core.api.domain.valuerange.ValueRangeFactory;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.util.ArrayList;
import java.util.List;

@PlanningEntity
public class Task{
    //Double chaînage, pratique pour les contraintes.
    private TaskAssignment taskAssignment;

    private int id;
    private String taskName;
    private Recipe currentRecipe;
    private Task dependentTask;

    private Boolean incomingItem; //True si la tâche fais passer le character de main vides à main pleines, false sinon.
    private Boolean outcomingItem; //True si la tâche fais passer le character de main pleines à main vides, false sinon.


    //Finished et Order pourraient se trouver dans TaskAssignment?
    //À vérifier, c'est étrange si on a plusieurs Characters, car on crée des duplicats de tâches
    @PlanningVariable(valueRangeProviderRefs = {"finishedRange"})
    private Boolean isFinished;

    //ValueRangeProvider se trouve dans la classe Recipe, pour avoir accès au nombre de tasks.
    //TODO: Modifier pour pouvoir gêrer cette valeur dynamiquement pour plusieurs personnages et un nombre de tâches inconnu.
    @PlanningVariable(valueRangeProviderRefs = {"finishedOrder"})
    private Integer finishedOrder;

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

    public void setTaskAssignment(TaskAssignment taskAssignment){
        this.taskAssignment = taskAssignment;
    }

    public TaskAssignment getTaskAssignment(){
        return taskAssignment;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
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

    public Integer getFinishedOrder(){
        return finishedOrder == null ? 0 : finishedOrder;
    }

    public boolean hasIncoming(){
        return incomingItem;
    }

    public boolean hasOutcoming(){
        return outcomingItem;
    }
}
