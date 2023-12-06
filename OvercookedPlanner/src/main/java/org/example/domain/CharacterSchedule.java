package org.example.domain;

import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.variable.PlanningListVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

@PlanningEntity
public class CharacterSchedule {
    @PlanningId
    private Long id;

    private Character character;

    @PlanningListVariable
    private List<Step> steps = new ArrayList<>();

    public CharacterSchedule() {}

    public CharacterSchedule(long id, Character character) {
        this.id = id;
        this.character = character;
    }

    public CharacterSchedule(long id, Character character, List<Step> steps) {
        this(id, character);
        this.steps = steps;
    }

    @Override
    public String toString() {
        StringBuilder returnValue = new StringBuilder("Character : " + character.getId() + ", Steps : ");
        for (Step step : steps) {
            returnValue.append(step.getName());
            if(step != steps.getLast()) {
                returnValue.append(", ");
            }
        }
        return returnValue.toString();
    }

    public boolean stepsRequirementsSatisfied() {
        for (Step step : steps) {
            if (step.getName() == "Couper un onion") {
                int onionsDispo = 0;
                for(int i = 0; i < steps.indexOf(step); i++) {
                    if(steps.get(i).getName() == "Prendre un onion") {
                        onionsDispo++;
                    }
                }
                if(onionsDispo <= 0) {
                    return false;
                }
            }
        }
        return true;
    }

    // ************************************************************************
    // Getters and setters
    // ************************************************************************

    public Character getCharacter() {
        return character;
    }
    public void setCharacter(Character character) {
        this.character = character;
    }

    public List<Step> getSteps() {
        return steps;
    }
    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public int getStepAmount() {
        return steps.size();
    }
}
