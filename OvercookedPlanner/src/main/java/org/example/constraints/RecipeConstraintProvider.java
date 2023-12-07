package org.example.constraints;

import org.example.domain.Character;
import org.example.domain.actions.Task;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

import java.util.Objects;

public class RecipeConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                //Hard constraints
                penalizeUnfinishedTaskRequirements(constraintFactory),
                //cantHoldMoreThanOneItem(constraintFactory),

                //Soft constraints
                penalizeChracterDoingNothing(constraintFactory)
        };
    }

    private Constraint penalizeUnfinishedTaskRequirements(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Task.class)
                .filter(task -> !task.areDependenciesFinished())
                //.forEach(CharacterSchedule.class)
                //.filter(schedule -> !schedule.stepsRequirementsSatisfied())
                .penalize(HardSoftLongScore.ONE_HARD).asConstraint("Task dependencies required");
    }

    //TODO: Minimiser le temps total (Non fonctionnel)
    private Constraint minimizeTotalAmountOfTask(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(Character.class)
                //.forEach(CharacterSchedule.class)
                //.filter(s -> s.getTaskAmount() == 0)
                .penalize(HardSoftScore.ONE_SOFT/*, (schedule1, schedule2) -> Math.abs(schedule1.getTaskAmount() - schedule2.getTaskAmount())*/).asConstraint("Minimize Step amount");
    }

    private Constraint penalizeChracterDoingNothing(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Character.class)
                .filter(character -> character.getNextElement() == null)
                .penalize(HardSoftLongScore.ONE_SOFT).asConstraint("All characters must work");

    //TODO: Accélérer en joinant seulement les tâches à leurs dépendences
    /*private Constraint penalizeDependenciesOrderTaskEquality(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Task.class)
                .join(Task.class,
                        Joiners.equal(t -> t.getTaskAssignment().getCharacter()),
                        Joiners.lessThan(Task::getId),
                        Joiners.equal(Task::getFinishedOrder))
                .penalize(HardSoftScore.ONE_HARD).asConstraint("All Different Task Order");
    }

    private Constraint penalizeDependenciesOrderTaskDependency(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Task.class)
                .join(Task.class,
                        Joiners.equal(t -> t.getTaskAssignment().getCharacter()),
                        Joiners.lessThan(Task::getId))
                //In this case, t2.id < t1.id
                .filter((t1, t2) -> t1.getDependencies() != null && t1.getDependencies().contains(t2) && t1.getFinishedOrder() < t2.getFinishedOrder())
                .penalize(HardSoftScore.ONE_HARD).asConstraint("Dependencies Have Lower Task Order");
    }*/

    //TODO: Corriger la contrainte, elle n'est pas complètement fonctionnelle.
    private Constraint cantHoldMoreThanOneItem(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Task.class)
                .filter(task -> !task.isItemInHandValid())
                .penalize(HardSoftLongScore.ONE_HARD).asConstraint("Holding More Than One Item");
    }
}
