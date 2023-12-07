package org.example.constraints;

import org.example.domain.Character;
import org.example.domain.actions.Task;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

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
                //.filter(s -> s.getPreviousTask() == null)
                //.reward(HardSoftScore.ONE_SOFT).asConstraint("Minimize Ste amount");
                .penalize(HardSoftLongScore.ONE_SOFT).asConstraint("All characters must work");
    }

    //TODO: Corriger la contrainte, elle n'est pas complètement fonctionnelle.
    private Constraint cantHoldMoreThanOneItem(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Task.class)
                .filter(task -> !task.isItemInHandValid())
                .penalize(HardSoftLongScore.ONE_HARD).asConstraint("Holding More Than One Item");
    }
}
