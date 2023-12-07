package org.example.domain.actions;
import java.util.ArrayList;
import java.util.List;

public class MultipleDependencyTask extends Task {
    private List<Task> dependentTasks = new ArrayList<>();

    public MultipleDependencyTask(){
    }

    public MultipleDependencyTask(String taskName, List<Task> dependentTasks, boolean incomingItem, boolean outcomingItem){
        super(taskName, incomingItem, outcomingItem);
        this.dependentTasks = dependentTasks;
    }

    public List<Task> getDependencies() {
        if(dependentTasks.isEmpty()) return null;
        return dependentTasks;
    }

    /*public Boolean areDependenciesFinished(){
        for (Task dependentTask : dependentTasks) {
            if (!dependentTask.isFinished()) return false;
        }
        return true;
    }*/
}
