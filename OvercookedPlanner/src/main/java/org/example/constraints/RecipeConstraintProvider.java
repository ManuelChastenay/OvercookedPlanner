package org.example.constraints;

import org.example.domain.Character;
import org.example.domain.actions.Task;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
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
                //penalizeDependenciesOrderTaskDependency(constraintFactory),
                cantDoActionWithoutNeededItem(constraintFactory),
                cantOutputItemWhenAlreadyHaveItemNotUsed(constraintFactory),
                firstActionCantHaveDependencies(constraintFactory),
                firstActionCantRequireItem(constraintFactory),


                //Soft constraints
                penalizeCharacterDoingNothing(constraintFactory)
                //minimizeTotalTime(constraintFactory)
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
                        (t1, t2) -> 50L).asConstraint("Task Dependencies Requires");
                        //TODO Meilleure gestion des poids
                        //(t1, t2) -> (long) t1.getUnfinishedDependencies().size()).asConstraint("Task Dependencies Requires");
    }

    //Poids très faible pour le moment de début (débutr tâche précédente + temps d'éxécution tâche précédente)
    private Constraint penalizeDependenciesOrderTaskDependency(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Task.class)
                .join(Task.class,
                        Joiners.equal(Task::getCharacter))
                .filter((t1, t2) -> t1.getPreviousTasks().contains(t2) && t1.getStartTime() <= t2.getStartTime())
                .penalizeLong(HardSoftLongScore.ONE_HARD,
                        (t1, t2) -> 1L).asConstraint("Dependencies Have Lower Task Order");
    }

    private Constraint cantDoActionWithoutNeededItem(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Task.class)
                .join(Task.class,
                        Joiners.equal(Task::getPreviousTaskId, Task::getId))
                .filter((task, previousTask) -> task.getInputItem() != null && task.getInputItem() != previousTask.getOutputItem())
                .penalizeLong(HardSoftLongScore.ONE_HARD,
                        (t1 , t2) -> 100L).asConstraint("Starting task without needed item");
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
