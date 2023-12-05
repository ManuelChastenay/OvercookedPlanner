package org.example.constraints;

import org.example.domain.TaskAssignment;
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
                taskDependency(constraintFactory),
                //cantHoldMoreThanOneItem(constraintFactory),
                //characterMultiTasking(constraintFactory)

                //Soft constraints
                //minimizeDistanceFromPreviousStep(constraintFactory),
        };
    }

    private Constraint taskDependency(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(TaskAssignment.class)
                .filter(taskAssignment -> taskAssignment.getTask().getDependentTask() != null &&
                        !taskAssignment.getTask().getDependentTask().isFinished())
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

    /*private Constraint characterMultiTasking(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(TaskAssignment.class)
                .join(TaskAssignment.class,
                        equal(TaskAssignment::getCharacter),
                        lessThan(TaskAssignment::getEndTime, TaskAssignment::getStartTime))
                .penalize("Character multitasking", HardSoftScore.ONE_HARD);
    }*/

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
