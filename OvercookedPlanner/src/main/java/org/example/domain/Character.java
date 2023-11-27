package org.example.domain;

public class Character extends CharacterOrCharacterStep{
    private String id;

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
