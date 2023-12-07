package org.example.domain;

import org.example.domain.actions.Task;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.variable.PlanningListVariable;

import java.util.ArrayList;
import java.util.List;

@PlanningEntity
public class CharacterSchedule {
    @PlanningId
    private Long id;

    private Character character;

    @PlanningListVariable
    private List<Task> tasks = new ArrayList<>();

    public CharacterSchedule() {}

    public CharacterSchedule(long id, Character character) {
        this.id = id;
        this.character = character;
    }

    public CharacterSchedule(long id, Character character, List<Task> tasks) {
        this(id, character);
        this.tasks = tasks;
    }

    @Override
    public String toString() {
        StringBuilder returnValue = new StringBuilder("Character : " + character.getId() + ", Tasks : ");
        for (Task task : tasks) {
            returnValue.append(task.getTaskName());
            if(task != tasks.getLast()) {
                returnValue.append(", ");
            }
        }
        return returnValue.toString();
    }

    public boolean stepsRequirementsSatisfied() {
        for (Task task : tasks) {
            for (Task dependency : task.getDependencies()) {
                if (!tasks.contains(dependency) || tasks.indexOf(dependency) > tasks.indexOf(task)) {
                    return false;
                }
            }
        }
        return true;
    }

    // ************************************************************************
    // Getters and setters
    // ************************************************************************

    public Character getCharacter() {
        return character;
    }
    public void setCharacter(Character character) {
        this.character = character;
    }

    public List<Task> getTasks() {
        return tasks;
    }
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }

    public int getTaskAmount() {
        return tasks.size();
    }
}
