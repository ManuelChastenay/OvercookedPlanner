package org.example.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableGraphType;

//@PlanningEntity
public class Step {
    private String name;

    //@PlanningVariable(graphType = PlanningVariableGraphType.CHAINED)
    //private Step nextStep;

    public Step() {}

    public Step(String name) {
        this.name = name;
    }

    /*public Step(String name, Step nextStep) {
        this(name);
        this.nextStep = nextStep;
    }*/

    @Override
    public String toString() {
        return name /*+ (nextStep == null ? "" : ", " + nextStep)*/;
    }

    // ************************************************************************
    // Getters and setters
    // ************************************************************************

    public String getName() {
        return name;
    }

    /*public Step getNextStep() {
        return nextStep;
    }*/

    /*public boolean areDependenciesFinished() {
        // TODO Ajouter abstraction
        if (name == "Couper un onion") {
            boolean isOnionTaken = false;
            if(this.getName() == "Couper un onion") {
                isOnionTaken = true;
            }
            //Step next = this.getNextStep();
            //while(next != null && !isOnionTaken) {
                /*if(next.getName() == "Prendre un onion") {
                    isOnionTaken = true;
                }
                //next = null;//next.getNextStep();
            //}
            return isOnionTaken;
        }
        else {
            return true;
        }
    }*/
}
