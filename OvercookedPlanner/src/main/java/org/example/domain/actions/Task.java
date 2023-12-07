package org.example.domain.actions;

import org.example.domain.Character;
import org.example.domain.Recipe;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.AnchorShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableGraphType;

import java.util.ArrayList;
import java.util.List;

@PlanningEntity
public class Task extends TaskOrCharacter {
    private String name;
    private int id;
    private Recipe currentRecipe;
    private List<Task> dependentTasks;

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

    public Task(String name, boolean incomingItem, boolean outcomingItem) {
        this.name = name;
        this.incomingItem = incomingItem;
        this.outcomingItem = outcomingItem;
    }

    public Task(String name, Task dependentTask, boolean incomingItem, boolean outcomingItem){
        this.name = name;
        this.dependentTasks = new ArrayList<>();
        dependentTasks.add(dependentTask);
        this.incomingItem = incomingItem;
        this.outcomingItem = outcomingItem;
    }

    public Task(String name, List<Task> dependentTasks, boolean incomingItem, boolean outcomingItem){
        this.name = name;
        this.dependentTasks = dependentTasks;
        this.incomingItem = incomingItem;
        this.outcomingItem = outcomingItem;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setPreviousElement(TaskOrCharacter previousElement) {
        this.previousElement = previousElement;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public boolean isLast() {
        return nextElement == null;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public Recipe getCurrentRecipe() {
        return currentRecipe;
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

    public boolean hasIncoming(){
        return incomingItem;
    }

    public boolean hasOutcoming(){
        return outcomingItem;
    }

    public Boolean areDependenciesFinished(){
        //if(dependentTasks == null) return true;
        //List<Task> dependenciesToValidate = new ArrayList<>(getDependencies());
        //TODO inverser la recherche pour partir du task courant et redescendre
        /*Task task = getCharacter().getNextElement();
        while(task != null && task != this && !dependenciesToValidate.isEmpty()) {
            dependenciesToValidate.remove(task);
            task = task.getNextElement();
        }*/
        /*Task task = getPreviousTask();
        while(task != null && !dependenciesToValidate.isEmpty()) {
            dependenciesToValidate.remove(task);
            task = task.getPreviousTask();
        }*/
        /*boolean good = true;
        for (Task task : getDependencies()) {
            if(!isTaskInList(getPreviousTask(), task)) good = false;
            break;
        }
        return good;*/
        //return dependenciesToValidate.isEmpty();
        if(dependentTasks != null) {
            List<Task> dependenciesToValidate = new ArrayList<>(getDependencies());
            Task task = getPreviousTask();
            while(task != null && !dependenciesToValidate.isEmpty()) {
                dependenciesToValidate.remove(task);
                task = task.getPreviousTask();
            }
            return dependenciesToValidate.isEmpty();
        }
        return new ArrayList<>().isEmpty();
    }

    private boolean isTaskInList(Task current, Task target) {
        if (current == null) return false;
        if (current == target) return true;
        return isTaskInList(current.getPreviousTask(), target);
    }

    public List<Task> getUnfinishedDependencies() {
        if(dependentTasks != null) {
            List<Task> dependenciesToValidate = new ArrayList<>(getDependencies());
            Task task = getPreviousTask();
            while(task != null && !dependenciesToValidate.isEmpty()) {
                dependenciesToValidate.remove(task);
                task = task.getPreviousTask();
            }
            return dependenciesToValidate;
        }
        return new ArrayList<>();
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
