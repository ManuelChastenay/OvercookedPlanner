package org.example.domain;

import org.example.domain.actions.Task;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.CountableValueRange;
import org.optaplanner.core.api.domain.valuerange.ValueRange;
import org.optaplanner.core.api.domain.valuerange.ValueRangeFactory;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.ArrayList;
import java.util.List;

@PlanningSolution
public class Recipe {
    private String recipeName;

    @ValueRangeProvider
    @ProblemFactCollectionProperty
    private List<Character> characters;

    @ValueRangeProvider
    @PlanningEntityCollectionProperty
    private List<Task> tasks;

    @ValueRangeProvider
    @PlanningEntityCollectionProperty
    private List<TaskAssignment> taskAssignments;

    @PlanningScore
    private HardSoftScore score;


    @ValueRangeProvider(id = "finishedOrder")
    public CountableValueRange<Integer> getFinishedOrder() {
        return ValueRangeFactory.createIntValueRange(0, tasks.size());
    }

    public Recipe() {
        taskAssignments = new ArrayList<>();
    }
    public Recipe(String recipeName) {
        this.recipeName = recipeName;
        taskAssignments = new ArrayList<>();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        for (Task task : tasks) {
            task.setCurrentRecipe(this);
        }
    }

    public List<Character> getCharacters() {
        return characters;
    }

    public void setCharactersAndTaskAssignments(List<Character> characters) {
        this.characters = characters;

        int id = 0;
        for (Task task : tasks) {
            task.setId(id++);
            for (Character character : characters) {
                //TODO Revoir le gestion des ids
                TaskAssignment newTA = new TaskAssignment(id, task, character);
                task.setTaskAssignment(newTA);
                taskAssignments.add(newTA);
            }
        }
    }

    public List<TaskAssignment> getTaskAssignments() {
        return taskAssignments;
    }
}
