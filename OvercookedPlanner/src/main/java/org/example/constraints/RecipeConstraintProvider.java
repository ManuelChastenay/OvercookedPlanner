package org.example.constraints;

import org.example.domain.CharacterSchedule;
import org.example.domain.actions.Task;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

public class RecipeConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                //Hard constraints
                penalizeUnfinishedTaskRequirements(constraintFactory),

                //Soft constraints
                minimizeTotalAmountOfTask(constraintFactory),
        };
    }

    private Constraint penalizeUnfinishedTaskRequirements(ConstraintFactory constraintFactory) {
        return constraintFactory
                //.forEach(Task.class)
                .forEach(CharacterSchedule.class)
                .filter(schedule -> !schedule.stepsRequirementsSatisfied())
                .penalize(HardSoftScore.ONE_HARD).asConstraint("Step requirements");
    }

    private Constraint minimizeTotalAmountOfTask(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(CharacterSchedule.class)
                //.forEach(CharacterSchedule.class)
                //.filter(s -> s.getTaskAmount() == 0)
                .penalize(HardSoftScore.ONE_SOFT, (schedule1, schedule2) -> Math.abs(schedule1.getTaskAmount() - schedule2.getTaskAmount())).asConstraint("Minimize Step amount");
    }
}
