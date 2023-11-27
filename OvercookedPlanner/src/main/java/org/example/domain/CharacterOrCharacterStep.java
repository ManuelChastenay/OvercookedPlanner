package org.example.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;

/**
 * Base class for implementing the chained graph modelling strategy.
 *
 * @see Character
 * @see CharacterStep
 */
@PlanningEntity
public abstract class CharacterOrCharacterStep {
    public static final String PREVIOUS_STEP = "previousStep";

    /**
     * Shadow variable: Is automatically set by the Solver and facilitates that all the elements in the chain, the
     * Trolley and the TrolleyStep, can have a reference to the next element in that chain.
     */
    @InverseRelationShadowVariable(sourceVariableName = PREVIOUS_STEP)
    protected CharacterStep nextStep;

    public CharacterStep getNextStep() {
        return nextStep;
    }
    public void setNextStep(CharacterStep nextStep) {
        this.nextStep = nextStep;
    }
}
