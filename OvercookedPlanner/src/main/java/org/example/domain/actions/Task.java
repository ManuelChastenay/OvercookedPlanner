package org.example.domain.actions;

import org.example.domain.Character;
import org.example.domain.Recipe;
import org.example.utils.Pathfinding;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.AnchorShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableGraphType;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@PlanningEntity
public class Task extends TaskOrCharacter {
    private String name;

    private int id;
    private int duration = 1;
    private Recipe currentRecipe;
    private List<Task> dependentTasks;

    private  List<Point> relatedPositions;

    @PlanningVariable(valueRangeProviderRefs = "positionRange")
    private Integer positionIndex;

    private Item inputItem;

    private Item outputItem;

    @PlanningVariable(graphType = PlanningVariableGraphType.CHAINED)
    private TaskOrCharacter previousElement;

    @AnchorShadowVariable(sourceVariableName = PREVIOUS_ELEMENT)
    private Character character;

    @PlanningVariable(valueRangeProviderRefs = {"startTime"})
    private Long startTime;

    @ValueRangeProvider(id = "positionRange")
    public List<Integer> getPositionIndexRange() {
        return IntStream.range(0, this.relatedPositions.size()).boxed().collect(Collectors.toList());
    }

    @ValueRangeProvider(id = "startTime")
    public List<Long> getStartTimeValueRange() {
        List<Long> possibleValue = new ArrayList<>();

        if(getPreviousTask() != null) possibleValue.add(getPreviousTask().startTime + getPreviousTask().duration + Pathfinding.calculateDistance(this, getPreviousTask()));
        else possibleValue.add(0L);

        return possibleValue;
    }

    public Task(){

    }

    public Task(String name, Item inputItem, Item outputItem, int duration, List<Point> position) {
        this.name = name;
        this.inputItem = inputItem;
        this.outputItem = outputItem;
        this.duration = duration;
        this.relatedPositions = position;
    }

    public Task(String name, Task dependentTask, Item inputItem, Item outputItem, int duration, List<Point> position){
        this.name = name;
        this.dependentTasks = new ArrayList<>();
        dependentTasks.add(dependentTask);
        this.inputItem = inputItem;
        this.outputItem = outputItem;
        this.duration = duration;
        this.relatedPositions = position;
    }

    public Task(String name, List<Task> dependentTasks, Item inputItem, Item outputItem, int duration, List<Point> position){
        this.name = name;
        this.dependentTasks = dependentTasks;
        this.inputItem = inputItem;
        this.outputItem = outputItem;
        this.duration = duration;
        this.relatedPositions = position;
    }
    public Point getPosition() { return relatedPositions.get(positionIndex); }

    public Point getLastPosition() {
        if(getPreviousTask() != null){
            return getPreviousTask().getPosition();
        } else{
            return getCharacter().getLocation();
        }
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

    public Recipe getCurrentRecipe(){
        return currentRecipe;
    }

    public List<Task> getDependencies() {
        List<Task> dependencies = dependentTasks;
        if(dependentTasks == null) {
            dependencies = new ArrayList<>();
        }
        return dependencies;
    }

    public Long getStartTime(){
        return startTime == null ? 0 : startTime;
    }

    public int getDuration(){
        return duration;
    }

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

    public List<Task> getRecipePreviousTasks(){
        return currentRecipe.getAllPreviousTasks(startTime);
    }

    public boolean taskInDependenciesAndNotCompleted() {
        List<Task> previousTasks = getRecipePreviousTasks();
        for (Task task : getDependencies()){
            if(previousTasks.contains(task)) return true;
        }
        return false;
    }
}
