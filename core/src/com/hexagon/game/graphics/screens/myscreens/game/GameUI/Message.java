package com.hexagon.game.graphics.screens.myscreens.game.GameUI;

import com.hexagon.game.graphics.ui.UILabel;

/**
 * Created by Sven on 06.03.2018.
 */

public class Message {

    private String  string;
    private long    timeCreated;
    private long    displayTime;
    private UILabel label;

    public Message(String string, UILabel label) {
        this(string, 5000, label);
    }

    public Message(String string, long displayTime, UILabel label) {
        this.string = string;
        this.displayTime = displayTime;
        this.timeCreated = System.currentTimeMillis();
        this.label = label;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public long getDisplayTime() {
        return displayTime;
    }

    public void setDisplayTime(long displayTime) {
        this.displayTime = displayTime;
    }

    public UILabel getLabel() {
        return label;
    }
}
