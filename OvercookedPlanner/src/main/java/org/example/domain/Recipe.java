package org.example.domain;

import org.example.domain.actions.Task;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.ArrayList;
import java.util.List;

//@PlanningSolution
public class Recipe {
    private String recipeName;

    private List<Task> tasks = new ArrayList<>();


    public Recipe() {
    }
    public Recipe(String recipeName) {
        this.recipeName = recipeName;
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
}
