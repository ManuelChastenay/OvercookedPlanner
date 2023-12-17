package org.example.domain.actions;

import com.github.javaparser.utils.Pair;
import org.example.domain.Character;
import org.example.domain.Recipe;
import org.optaplanner.core.api.domain.entity.PlanningEntity;

import org.optaplanner.core.api.domain.valuerange.ValueRange;
import org.optaplanner.core.api.domain.valuerange.ValueRangeFactory;

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
    private Integer startTime;

    @ValueRangeProvider(id = "positionRange")
    public List<Integer> getPositionIndexRange() {
        return IntStream.range(0, this.relatedPositions.size()).boxed().collect(Collectors.toList());
    }

    private Boolean incomingItem; //True si la tâche fais passer le character de main vides à main pleines, false sinon.
    private Boolean outcomingItem; //True si la tâche fais passer le character de main pleines à main vides, false sinon.

    //ValueRangeProvider se trouve dans la classe Recipe, pour avoir accès au nombre de tasks.
    //TODO: Modifier lors de l'implémentation du temps pour l'ordonnancement


    @ValueRangeProvider(id = "startTime")
    public List<Integer> getStartTimeValueRange() {
        List<Integer> possibleValue = new ArrayList<>();


        if(getPreviousTask() != null) possibleValue.add(getPreviousTask().startTime + getPreviousTask().duration);
        else possibleValue.add(0);
        //Having more than 1 variable helps the planner not crashing.
        //possibleValue.add(0);

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
}
