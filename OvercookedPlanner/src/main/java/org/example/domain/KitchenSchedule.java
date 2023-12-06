package org.example.domain;

import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.util.ArrayList;
import java.util.List;

@PlanningSolution
public class KitchenSchedule {

    @PlanningEntityCollectionProperty
    private List<CharacterSchedule> schedules = new ArrayList<>();

    @ProblemFactCollectionProperty
    @ValueRangeProvider
    private List<Step> stepList;


    /*@ProblemFactCollectionProperty
    @ValueRangeProvider
    private List<Timeslot> timeslotList;
    @ProblemFactCollectionProperty
    @ValueRangeProvider
    private List<Room> roomList;
    @PlanningEntityCollectionProperty
    private List<Lesson> lessonList;*/

    @PlanningScore
    private HardSoftScore score;

    // No-arg constructor required for OptaPlanner
    public KitchenSchedule() {
    }

    public KitchenSchedule(List<Character> characters, List<Step> steps) {
        long id = 0;
        for (Character character : characters) {
            schedules.add(new CharacterSchedule(id, character));
            id++;
        }
        this.stepList = steps;
    }

    // ************************************************************************
    // Getters and setters
    // ************************************************************************

    public List<CharacterSchedule> getCharacterScheduleList() {
        return schedules;
    }

    public HardSoftScore getScore() {
        return score;
    }
}
