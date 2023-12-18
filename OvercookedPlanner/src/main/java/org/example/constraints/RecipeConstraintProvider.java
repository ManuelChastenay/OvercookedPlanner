package org.example.constraints;

import org.example.domain.actions.Task;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

public class RecipeConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
            //Hard constraints
            penalizeUnfinishedTaskRequirements(constraintFactory),
            penalizeIncorrectTaskStartTime(constraintFactory),
            penalizeIncorrectRecipeStartTime(constraintFactory),

            cantDoActionWithoutNeededItem(constraintFactory),
            cantOutputItemWhenAlreadyHaveItemNotUsed(constraintFactory),

            firstActionHasBaseStartTime(constraintFactory),
            firstActionCantHaveDependencies(constraintFactory),
            firstActionCantRequireItem(constraintFactory),


            //Soft constraints
            penalizeLenghtOfSchedule(constraintFactory)
        };
    }


    //HARD CONSTRAINTS

    private Constraint penalizeUnfinishedTaskRequirements(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Task.class)
            .filter(Task::taskInDependenciesAndNotCompleted)
            .penalizeLong(HardSoftLongScore.ONE_HARD, t1 -> 500L).asConstraint("Task Dependencies Requires");
    }

    private Constraint penalizeIncorrectTaskStartTime(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Task.class)
            .join(Task.class,
                Joiners.equal(Task::getPreviousTaskId, Task::getId))
            .filter((task, previousTask) -> task.getStartTime() != previousTask.getStartTime() + previousTask.getDuration())
            .penalizeLong(HardSoftLongScore.ONE_HARD, (t1, t2) -> 1L).asConstraint("Incorrect Task Start Time Set");
    }

    private Constraint penalizeIncorrectRecipeStartTime(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Task.class)
            .filter(task -> task.getStartTime() < task.getCurrentRecipe().getStartTime())
            .penalizeLong(HardSoftLongScore.ONE_HARD, task -> 1000L).asConstraint("Incorrect Recipe Start Time Set");
    }

    private Constraint cantDoActionWithoutNeededItem(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Task.class)
            .join(Task.class,
                Joiners.equal(Task::getPreviousTaskId, Task::getId))
            .filter((task, previousTask) -> task.getInputItem() != null && task.getInputItem() != previousTask.getOutputItem())
            .penalizeLong(HardSoftLongScore.ONE_HARD, (t1, t2) -> 2000L).asConstraint("Starting task without needed item");
    }

    private Constraint cantOutputItemWhenAlreadyHaveItemNotUsed(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Task.class)
            .join(Task.class,
                Joiners.equal(Task::getPreviousTaskId, Task::getId))
            .filter((task, previousTask) -> task.getOutputItem() != null && task.getInputItem() == null && previousTask.getOutputItem() != null)
            .penalizeLong(HardSoftLongScore.ONE_HARD, (t1 , t2) -> 1000L).asConstraint("Can't take more than one item");
    }

    private Constraint firstActionHasBaseStartTime(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Task.class)
            .filter(t -> t.getPreviousTask() == null && t.getStartTime() != 0)
            .penalizeLong(HardSoftLongScore.ONE_HARD, t -> 1000L).asConstraint("First action has to start at time 0");
    }

    private Constraint firstActionCantRequireItem(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Task.class)
            .filter(t -> t.getPreviousTask() == null && t.getInputItem() != null)
            .penalizeLong(HardSoftLongScore.ONE_HARD, t -> 1000L).asConstraint("First action can't require an item");
    }

    private Constraint firstActionCantHaveDependencies(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Task.class)
            .filter(t -> t.getPreviousTask() == null && !t.getDependencies().isEmpty())
            .penalizeLong(HardSoftLongScore.ONE_HARD, t -> 1000L).asConstraint("First action can't have dependencies");
    }


    //SOFT CONSTRAINTS

    private Constraint penalizeLenghtOfSchedule(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(Task.class)
            .penalizeLong(HardSoftLongScore.ONE_SOFT, c -> c.getStartTime() + c.getDuration()).asConstraint("Total Sum of Task Times");
    }
}
