package org.example.domain;

import org.example.domain.actions.Task;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;
import org.optaplanner.core.api.score.buildin.hardsoftlong.HardSoftLongScore;

import java.util.ArrayList;
import java.util.List;

@PlanningSolution
public class KitchenSchedule {
    @ValueRangeProvider
    @ProblemFactCollectionProperty
    private List<Character> characterList;

    @ValueRangeProvider
    @PlanningEntityCollectionProperty
    private List<Task> taskList;

    @PlanningScore
    private HardSoftLongScore score;

    public KitchenSchedule() {
        // Marshalling constructor
    }

    public KitchenSchedule(List<Character> characterList, List<Task> taskList) {
        this.characterList = characterList;
        this.taskList = taskList;
    }

    public List<Character> getCharacterList() {
        return characterList;
    }

    public void setCharacterList(List<Character> characterList) {
        this.characterList = characterList;
    }

    public List<Task> getTaskList() {
        return taskList;
    }

    public void setTaskList(List<Task> taskList) {
        this.taskList = taskList;
    }

    public HardSoftLongScore getScore() {
        return score;
    }

    public void setScore(HardSoftLongScore score) {
        this.score = score;
    }
}
