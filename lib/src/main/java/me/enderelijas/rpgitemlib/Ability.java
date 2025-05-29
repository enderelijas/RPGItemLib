package me.enderelijas.rpgitemlib;

import org.bukkit.event.Event;

public interface Ability {
    void execute(Event event);
    String getDescription();
}
