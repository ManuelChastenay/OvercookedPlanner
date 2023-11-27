package org.example.constraints;

import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;

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
        return null;
    }
}
