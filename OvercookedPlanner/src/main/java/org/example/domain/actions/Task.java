package org.example.domain.actions;

import org.example.domain.Character;
import org.example.domain.Recipe;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.AnchorShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableGraphType;

import javax.swing.text.Position;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@PlanningEntity
public class Task extends TaskOrCharacter {
    private String name;
    private int id;
    private Recipe currentRecipe;
    private List<Task> dependentTasks;

    private String locationType;

    private Point finalPosition;

    private Point[] track;

    @PlanningVariable(graphType = PlanningVariableGraphType.CHAINED)
    private TaskOrCharacter previousElement;

    @AnchorShadowVariable(sourceVariableName = PREVIOUS_ELEMENT)
    private Character character;

    private Boolean incomingItem; //True si la tâche fais passer le character de main vides à main pleines, false sinon.
    private Boolean outcomingItem; //True si la tâche fais passer le character de main pleines à main vides, false sinon.

    //ValueRangeProvider se trouve dans la classe Recipe, pour avoir accès au nombre de tasks.
    //TODO: Modifier lors de l'implémentation du temps pour l'ordonnancement
    @PlanningVariable(valueRangeProviderRefs = {"startTime"})
    private Integer startTime;

    public Task(){

    }

    public Task(String name, boolean incomingItem, boolean outcomingItem, Point position) {
        this.name = name;
        this.incomingItem = incomingItem;
        this.outcomingItem = outcomingItem;
        this.finalPosition = position;
    }

    public Task(String name, Task dependentTask, boolean incomingItem, boolean outcomingItem, Point position){
        this.name = name;
        this.dependentTasks = new ArrayList<>();
        dependentTasks.add(dependentTask);
        this.incomingItem = incomingItem;
        this.outcomingItem = outcomingItem;
        this.finalPosition = position;
    }

    public Task(String name, List<Task> dependentTasks, boolean incomingItem, boolean outcomingItem, Point position){
        this.name = name;
        this.dependentTasks = dependentTasks;
        this.incomingItem = incomingItem;
        this.outcomingItem = outcomingItem;
        this.finalPosition = position;
    }

    public Point getLastPosition() {
        if(getPreviousTask() != null){
            return getPreviousTask().getPosition();
        } else{
            return getCharacter().getLocation();
        }
    }
    public Point getPosition() { return finalPosition; }

    public void setPosition(Point p){ this.finalPosition = p; }
    public String getLocationType(){return locationType; }

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

    public Boolean isHandEmpty(){
        // Action prend objet
        if(incomingItem) return false;
        // Action prend pas objet, mais action précédente oui
        if(getPreviousTask() != null && !getPreviousTask().isHandEmpty() && !outcomingItem) return false;

        return true;
    }

    public Boolean isItemInHandValid(){
        if(getPreviousTask() == null) {
            return !outcomingItem;
        }
        if(getPreviousTask().isHandEmpty()) {
            return !outcomingItem;
        }
        return !incomingItem;
    }
}
