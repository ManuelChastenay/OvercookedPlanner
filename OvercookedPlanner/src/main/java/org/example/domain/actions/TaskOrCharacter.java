package org.example.domain.actions;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.InverseRelationShadowVariable;

@PlanningEntity
public abstract class TaskOrCharacter {
    public static final String PREVIOUS_ELEMENT = "previousElement";

    @InverseRelationShadowVariable(sourceVariableName = PREVIOUS_ELEMENT)
    protected Task nextElement;

    protected TaskOrCharacter() {
        //marshalling constructor
    }

    public Task getNextElement() {
        return nextElement;
    }

    public void setNextElement(Task nextElement) {
        this.nextElement = nextElement;
    }
}
