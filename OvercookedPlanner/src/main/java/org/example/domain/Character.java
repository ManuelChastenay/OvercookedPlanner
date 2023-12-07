package org.example.domain;

import org.example.domain.actions.Task;
import org.example.domain.actions.TaskOrCharacter;
import org.optaplanner.core.api.domain.entity.PlanningEntity;

public class Character extends TaskOrCharacter {
    private String id;

    public Character() {
        //marshalling constructor
    }

    public Character(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
