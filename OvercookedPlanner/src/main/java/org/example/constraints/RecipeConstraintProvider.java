package org.example.constraints;

import org.example.domain.TaskAssignment;
import org.example.domain.actions.Task;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.stream.Constraint;
import org.optaplanner.core.api.score.stream.ConstraintFactory;
import org.optaplanner.core.api.score.stream.ConstraintProvider;
import org.optaplanner.core.api.score.stream.Joiners;

import java.util.Objects;

public class RecipeConstraintProvider implements ConstraintProvider {
    @Override
    public Constraint[] defineConstraints(ConstraintFactory constraintFactory) {
        return new Constraint[]{
                //Hard constraints
                //penalizeUnfinishedDependenciesTask(constraintFactory),
                penalizeDependenciesOrderTaskEquality(constraintFactory),
                penalizeDependenciesOrderTaskDependency(constraintFactory),
                //cantHoldMoreThanOneItem(constraintFactory),

                //Soft constraints
                //minimizeDistanceFromPreviousStep(constraintFactory),
        };
    }

    //TODO: NON FONCTIONNEL
    private Constraint penalizeUnfinishedDependenciesTask(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Task.class)
                .filter(t -> t.getDependencies() != null && !t.areDependenciesFinished())
                .penalize(HardSoftScore.ONE_HARD).asConstraint("Task Dependency");
    }

    //TODO: Accélérer en joinant seulement les tâches à leurs dépendences
    private Constraint penalizeDependenciesOrderTaskEquality(ConstraintFactory constraintFactory) {
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
    }

    //TODO: Corriger la contrainte, elle n'est pas complètement fonctionnelle.
    private Constraint cantHoldMoreThanOneItem(ConstraintFactory constraintFactory) {
        return constraintFactory
                .forEach(Task.class)
                .join(Task.class,
                        Joiners.equal(t -> t.getTaskAssignment().getCharacter()),
                        Joiners.lessThan(Task::getId))
                .filter((t1, t2) ->
                        t1.hasIncoming() &&
                        !t1.hasOutcoming() &&
                        t2.hasIncoming()
                        //taskAssignment1.getTask().getDependencies() != null &&
                        //!taskAssignment1.getTask().getDependencies().contains(taskAssignment2.getTask())
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
