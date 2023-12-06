package org.example.domain;

import org.example.domain.actions.Task;
import org.optaplanner.core.api.domain.entity.PlanningEntity;
import org.optaplanner.core.api.domain.variable.AnchorShadowVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariable;
import org.optaplanner.core.api.domain.variable.PlanningVariableGraphType;

/**
 * Revoir l'implémentation qui sera différente avec le pathfinding. (Est-ce qu'on considère une liste de déplacements entre chaque action?)

 * Represents a "stop" in a character's path where an action is to be done.
 * The CharacterStep is the only PlanningEntity entity that will be changed during the problem-solving, and the
 *  {@link TaskAssignment#previousStep} is the only PlanningVariable defined.
 * By using the CHAINED graph modelling strategy, combined with shadow variables {@link TaskAssignment#character} and
 *  {@link CharacterOrTaskAssignment#nextStep}, the Solver will create a structure like the following:
 * Character1 <-> CharacterStep1 <-> CharacterStep2 <-> CharacterStep3 -> end
 * Character2 <-> ...

 * Where the initial element of each chain, the Character, is known as the "anchor" and will always have a reference
 * to the next element, a CharacterStep.

 * The intermediary elements, the CharacterStep, when assigned will always have a reference to "anchor", the previous
 * element and the next element. (a null value on the next element, indicates that current step is currently the last
 * element).
 */
@PlanningEntity
public class TaskAssignment extends CharacterOrTaskAssignment {
    //TODO: Change to startTime
    //@PlanningId
    private int id;

    private Task task;

    /**
     * Shadow variable: Is automatically set by the Solver and facilitates that all the character steps can have a
     * reference to the chain "anchor", the Character.
     */
    @AnchorShadowVariable(sourceVariableName = PREVIOUS_TASK)
    private Character character;

    @PlanningVariable(graphType = PlanningVariableGraphType.CHAINED)
    private CharacterOrTaskAssignment previousTask;

    public TaskAssignment() {
    }

    public TaskAssignment(int id, Task task, Character character) {
        this.id = id;
        this.task = task;
        this.character = character;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Character getCharacter() {
        return character;
    }

    public void setCharacter(Character character) {
        this.character = character;
    }

    public int getId(){
        return id;
    }

    public CharacterOrTaskAssignment getPreviousTask() {
        return previousTask;
    }

    public void setPreviousTask(CharacterOrTaskAssignment previousTask) {
        this.previousTask = previousTask;
    }
}
