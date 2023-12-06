package org.example.constraints;

import org.example.domain.Character;
import org.example.domain.CharacterSchedule;
import org.example.domain.KitchenSchedule;
import org.example.domain.Step;
import org.example.domain.TaskAssignment;
import org.example.domain.actions.Task;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

import java.util.function.ToIntBiFunction;

public class RecipeConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                //Hard constraints
                penalizeUnfinishedStepRequirements(constraintFactory),

                //Soft constraints
                minimizeTotalAmountOfTask(constraintFactory),
        };
    }

    private Constraint penalizeUnfinishedStepRequirements(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(CharacterSchedule.class)
                .filter(schedule -> !schedule.stepsRequirementsSatisfied())
                .penalize(HardSoftScore.ONE_HARD).asConstraint("Step requirements");
    }

    private Constraint minimizeTotalAmountOfTask(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEachUniquePair(CharacterSchedule.class)
                .penalize(HardSoftScore.ONE_SOFT, (schedule1, schedule2) -> Math.abs(schedule1.getStepAmount() - schedule2.getStepAmount())).asConstraint("Minimize Step amount");
    }
}
