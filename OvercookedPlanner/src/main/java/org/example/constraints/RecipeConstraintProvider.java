package org.example.constraints;

import org.example.domain.Character;
import org.example.domain.actions.Task;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.score.stream.*;

public class RecipeConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                //Hard constraints
                penalizeUnfinishedTaskRequirements(constraintFactory),
                penalizeIncorrectStartTime(constraintFactory),
                cantDoActionWithoutNeededItem(constraintFactory),
                cantOutputItemWhenAlreadyHaveItemNotUsed(constraintFactory),
                firstActionHasBaseStartTime(constraintFactory),
                firstActionCantHaveDependencies(constraintFactory),
                firstActionCantRequireItem(constraintFactory),


                //Soft constraints
                penalizeChracterDoingNothing(constraintFactory),
                minimizeDistanceFromTaskToNext(constraintFactory),

        };
    }

    //HARD CONSTRAINTS

    //TODO Le multijoueur va necessiter l'ordonnacement (Task::startTime)
    private Constraint penalizeUnfinishedTaskRequirements(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Task.class)
                .join(Task.class,
                        Joiners.lessThan(Task::getId))
                .filter((t1, t2) ->
                        (t2.getDependencies().contains(t1) && !t2.getPreviousTasks().contains(t1)) ||
                        (t1.getDependencies().contains(t2) && !t1.getPreviousTasks().contains(t2))
                )
                .penalizeLong(HardSoftLongScore.ONE_HARD,
                        (t1, t2) -> 500L).asConstraint("Task Dependencies Requires");
                        //TODO Meilleure gestion des poids
                        //(t1, t2) -> (long) t1.getUnfinishedDependencies().size()).asConstraint("Task Dependencies Requires");
    }

    private Constraint penalizeIncorrectStartTime(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Task.class)
                .join(Task.class,
                        //Joiners.equal(Task::getCharacter),
                        Joiners.equal(Task::getPreviousTaskId, Task::getId))
                .filter((task, previousTask) -> task.getStartTime() != previousTask.getStartTime() + previousTask.getDuration())
                .penalizeLong(HardSoftLongScore.ONE_HARD,
                        (t1, t2) -> 1L).asConstraint("Incorrect Start Time Set");
    }

    private Constraint cantDoActionWithoutNeededItem(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Task.class)
                .join(Task.class,
                        Joiners.equal(Task::getPreviousTaskId, Task::getId))
                .filter((task, previousTask) -> task.getInputItem() != null && task.getInputItem() != previousTask.getOutputItem())
                .penalizeLong(HardSoftLongScore.ONE_HARD,
                        (t1, t2) -> 200L).asConstraint("Starting task without needed item");
    }

    private Constraint cantOutputItemWhenAlreadyHaveItemNotUsed(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Task.class)
                .join(Task.class,
                        Joiners.equal(Task::getPreviousTaskId, Task::getId))
                .filter((task, previousTask) -> task.getOutputItem() != null && task.getInputItem() == null && previousTask.getOutputItem() != null)
                .penalizeLong(HardSoftLongScore.ONE_HARD,
                        (t1 , t2) -> 100L).asConstraint("Can't take more than one item");
    }

    private Constraint firstActionHasBaseStartTime(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Task.class)
                .filter(t -> t.getPreviousTask() == null && t.getStartTime() != 0)
                .penalizeLong(HardSoftLongScore.ONE_HARD,
                        t -> 1000L).asConstraint("First action has to start at time 0");
    }


    private Constraint minimizeDistanceFromTaskToNext(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Task.class)
                .join(Task.class, Joiners.equal(Task::getPreviousTaskId, Task::getId))
                .penalizeLong(
                        HardSoftLongScore.ONE_SOFT, // Le poids de la pénalité.
                        (currentTask, previousTask) -> Pathfinding.calculateDistance(currentTask,previousTask ) // La fonction de pénalité.
                ).asConstraint("motion penalty");

    private Constraint firstActionCantRequireItem(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Task.class)
                .filter(t -> t.getPreviousTask() == null && t.getInputItem() != null)
                .penalizeLong(HardSoftLongScore.ONE_HARD,
                        t -> 1000L).asConstraint("First action can't require an item");
    }

    private Constraint firstActionCantHaveDependencies(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Task.class)
                .filter(t -> t.getPreviousTask() == null && !t.getDependencies().isEmpty())
                .penalizeLong(HardSoftLongScore.ONE_HARD,
                        t -> 1000L).asConstraint("First action can't have dependencies");
    }

    //SOFT CONSTRAINTS

    private Constraint penalizeCharacterDoingNothing(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Character.class)
                .filter(character -> character.getNextElement() == null)
                .penalize(HardSoftLongScore.ONE_SOFT).asConstraint("All characters must work");

    }
}
