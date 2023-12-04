package org.example.constraints;

import org.example.domain.CharacterStep;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

public class OvercookedConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                //Hard constraints

                //Soft constraints
                minimizeDistanceFromPreviousStep(constraintFactory),
        };
    }

    Constraint minimizeDistanceFromPreviousStep(ConstraintFactory constraintFactory) {
        // TODO remove dumb constraint
        return constraintFactory
                .forEachUniquePair(CharacterStep.class,
                    Joiners.equal(CharacterStep::getCharacter)
                )
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Yeah");
    }
}
