package org.example.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;

/**
 * Base class for implementing the chained graph modelling strategy.
 *
 * @see Character
 * @see TaskAssignment
 */
@PlanningEntity
public abstract class CharacterOrTaskAssignment {
    public static final String PREVIOUS_TASK = "previousTask";

    /**
     * Shadow variable: Is automatically set by the Solver and facilitates that all the elements in the chain, the
     * Character and the TaskAssignment, can have a reference to the next element in that chain.
     */
    @InverseRelationShadowVariable(sourceVariableName = PREVIOUS_TASK)
    protected TaskAssignment nextStep;

    public TaskAssignment getNextStep() {
        return nextStep;
    }
    public void setNextStep(TaskAssignment nextStep) {
        this.nextStep = nextStep;
    }
}
