package org.example.domain.actions;

import org.example.domain.Character;
import org.example.domain.Recipe;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.AnchorShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableGraphType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@PlanningEntity
public class Task extends TaskOrCharacter {
    private String name;
    private int id;
    private Recipe currentRecipe;
    private List<Task> dependentTasks;

    private Item inputItem;

    private Item outputItem;

    @PlanningVariable(graphType = PlanningVariableGraphType.CHAINED)
    private TaskOrCharacter previousElement;

    @AnchorShadowVariable(sourceVariableName = PREVIOUS_ELEMENT)
    private Character character;
    
    //ValueRangeProvider se trouve dans la classe Recipe, pour avoir accès au nombre de tasks.
    //TODO: Modifier lors de l'implémentation du temps pour l'ordonnancement
    @PlanningVariable(valueRangeProviderRefs = {"startTime"})
    private Integer startTime;

    public Task(){

    }

    public Task(String name, Item inputItem, Item outputItem) {
        this.name = name;
        this.inputItem = inputItem;
        this.outputItem = outputItem;
    }

    public Task(String name, Task dependentTask, Item inputItem, Item outputItem){
        this.name = name;
        this.dependentTasks = new ArrayList<>();
        dependentTasks.add(dependentTask);
        this.inputItem = inputItem;
        this.outputItem = outputItem;
    }

    public Task(String name, List<Task> dependentTasks, Item inputItem, Item outputItem){
        this.name = name;
        this.dependentTasks = dependentTasks;
        this.inputItem = inputItem;
        this.outputItem = outputItem;
    }

    public String getName() {
        return name;
    }

    public TaskOrCharacter getPreviousElement() {
        return previousElement;
    }

    public Task getPreviousTask() {
        if(getPreviousElement() instanceof Task) {
            return (Task) getPreviousElement();
        }
        return null;
    }

    public Integer getPreviousTaskId() {
        return getPreviousTask() == null ? null : getPreviousTask().id;
    }

    public Character getCharacter() {
        return character;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public void setCurrentRecipe(Recipe currentRecipe) {
        this.currentRecipe = currentRecipe;
    }

    public List<Task> getDependencies() {
        List<Task> dependencies = dependentTasks;
        if(dependentTasks == null) {
            dependencies = new ArrayList<>();
        }
        return dependencies;
    }

    public Integer getStartTime(){
        return startTime == null ? 0 : startTime;
    }

    //TODO Degueu mais fonctionnel
    public List<Task> getPreviousTasks(){
        List<Task> previousTasks = new ArrayList<>();
        if(previousElement instanceof Character) return previousTasks;

        Task previousTask = (Task) previousElement;
        while(previousTask != null){
            previousTasks.add(previousTask);
            if(previousTask.previousElement instanceof Character) return previousTasks;
            previousTask = (Task) previousTask.previousElement;
        }
        return previousTasks;
    }

    public Item getInputItem() {
        return inputItem;
    }

    public Item getOutputItem() {
        return outputItem;
    }
}
