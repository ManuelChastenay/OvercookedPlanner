package org.example.constraints;

import org.example.domain.TaskAssignment;
import org.example.domain.actions.Task;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

public class RecipeConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                //Hard constraints
                penalizeUnfinishedDependenciesTaskAssignment(constraintFactory),
                //cantHoldMoreThanOneItem(constraintFactory),

                //Soft constraints
                //minimizeDistanceFromPreviousStep(constraintFactory),
        };
    }

    private Constraint penalizeUnfinishedDependenciesTaskAssignment(ConstraintFactory constraintFactory) {
        return constraintFactory
            .forEach(TaskAssignment.class)
            .filter(taskAssignment -> taskAssignment.getTask().getDependencies() != null && !taskAssignment.getTask().areDependenciesFinished())
            .penalize(HardSoftScore.ONE_HARD).asConstraint("Task Dependency");
    }

    private Constraint cantHoldMoreThanOneItem(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(TaskAssignment.class)
                .join(TaskAssignment.class,
                        Joiners.equal(TaskAssignment::getCharacter),
                        Joiners.lessThan(TaskAssignment::getId))
                .filter((taskAssignment1, taskAssignment2) ->
                        taskAssignment1.getTask().hasIncoming() &&
                        !taskAssignment1.getTask().hasOutcoming() &&
                        taskAssignment2.getTask().hasIncoming()
                )
                .penalize(HardSoftScore.ONE_HARD).asConstraint("Holding More Than One Item");
    }

    Constraint minimizeDistanceFromPreviousStep(ConstraintFactory constraintFactory) {
        // TODO remove dumb constraint
        return constraintFactory
                .forEachUniquePair(TaskAssignment.class,
                    Joiners.equal(TaskAssignment::getCharacter)
                )
                .penalize(HardSoftScore.ONE_HARD)
                .asConstraint("Yeah");
    }
}
