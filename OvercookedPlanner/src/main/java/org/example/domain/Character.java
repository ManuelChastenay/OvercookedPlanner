package org.example.domain;

import org.example.domain.actions.Task;
import org.example.domain.actions.TaskOrCharacter;
import org.optaplanner.core.api.domain.entity.PlanningEntity;

import java.awt.*;

public class Character extends TaskOrCharacter {
    private String id;
    private Point location = new Point(2,2);

    public Character() {
        //marshalling constructor
    }

    public Point getLocation() { return location; }

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
