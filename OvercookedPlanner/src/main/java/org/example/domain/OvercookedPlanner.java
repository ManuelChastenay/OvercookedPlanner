package org.example.domain;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.List;

@PlanningSolution
public class OvercookedPlanner {
    /*@ProblemFactCollectionProperty
    @ValueRangeProvider
    private List<Timeslot> timeslotList;
    @ProblemFactCollectionProperty
    @ValueRangeProvider
    private List<Room> roomList;
    @PlanningEntityCollectionProperty
    private List<Lesson> lessonList;*/
    @ProblemFactCollectionProperty
    @ValueRangeProvider
    private List<Character> characters;

    @PlanningEntityCollectionProperty
    private List<CharacterStep> characterSteps;

    @PlanningScore
    private HardSoftScore score;

    // No-arg constructor required for OptaPlanner
    public OvercookedPlanner() {}

    public OvercookedPlanner(List<Character> characters, List<CharacterStep> characterSteps) {
        this.characters = characters;
        this.characterSteps = characterSteps;
    }

    /*public OvercookedPlanner(List<Timeslot> timeslotList, List<Room> roomList, List<Lesson> lessonList) {
        this.timeslotList = timeslotList;
        this.roomList = roomList;
        this.lessonList = lessonList;
    }*/

    public List<Character> getCharacterList() {
        return characters;
    }

    public List<CharacterStep> getCharacterStepList() {
        return characterSteps;
    }
}
