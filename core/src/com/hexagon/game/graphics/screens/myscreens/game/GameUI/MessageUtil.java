package com.hexagon.game.graphics.screens.myscreens.game.GameUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hexagon.game.graphics.ui.UILabel;
import com.hexagon.game.graphics.ui.WindowManager;
import com.hexagon.game.graphics.ui.windows.DropdownWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sven on 06.03.2018.
 */

public class MessageUtil {

    private List<Message>   messages = new ArrayList<>();
    private List<Message>   toRemove = new ArrayList<>();
    private DropdownWindow  window;
    private Stage           stage;

    public MessageUtil(Stage stage, WindowManager windowManager) {
        window = new DropdownWindow(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()-390, 1, 300, 0, 0);
        windowManager.addWindow(window);
        this.stage = stage;
        window.show(this.stage);
    }

    public void add(String string) {
        add(string, 5000, Color.LIGHT_GRAY);
    }

    public void add(String string, long displayTime, Color color) {
        if (string.length() > 60) {
            string = string.substring(0, 60);
        }
        UILabel label = new UILabel(50, 50, 0, 0, 26, string,
                color,
                new Color(0.1f, 0.1f, 0.2f, 0.9f));
        label.setX(-label.getWidth());
        label.setHeight(label.getHeight() + 8);

        messages.add(new Message(string, displayTime, label));
        if (messages.size() >= 16) {
            window.remove(messages.get(0).getLabel(), stage);
            messages.remove(0);
        }

        window.add(
                label,
                stage
        );
        window.orderAllNeatly(1);
        window.updateElements();

    }

    public void removeAll() {
        for (Message message : messages) {
            window.remove(message.getLabel(), stage);
        }
        messages.clear();
    }

    public void update() {
        for (Message message : messages) {
            if (System.currentTimeMillis() - message.getTimeCreated() >= message.getDisplayTime()) {
                toRemove.add(message);
                window.remove(message.getLabel(), stage);
            }
        }
        if (!toRemove.isEmpty()) {
            messages.removeAll(toRemove);
            toRemove.clear();

            window.orderAllNeatly(1);
            window.updateElements();
        }
    }

}
