package org.example.domain.actions;

import org.example.domain.Recipe;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@PlanningEntity
public class MultipleDependencyTask extends Task {
    @PlanningVariable(valueRangeProviderRefs = {"taskRange"})
    private List<Task> dependentTasks = new ArrayList<>();

    public MultipleDependencyTask(){
    }

    public MultipleDependencyTask(String taskName, List<Task> dependentTasks, boolean incomingItem, boolean outcomingItem){
        super(taskName, incomingItem, outcomingItem);
        this.dependentTasks = dependentTasks;
    }
}
