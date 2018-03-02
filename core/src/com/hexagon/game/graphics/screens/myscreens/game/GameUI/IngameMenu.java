package com.hexagon.game.graphics.screens.myscreens.game.GameUI;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.hexagon.game.graphics.screens.ScreenManager;
import com.hexagon.game.graphics.screens.ScreenType;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.ui.buttons.UiButton;
import com.hexagon.game.graphics.ui.windows.DropdownWindow;
import com.hexagon.game.network.HexaServer;
import com.hexagon.game.network.packets.PacketLeave;

/**
 * Created by Johannes on 25.02.2018.
 */

public class IngameMenu {

    public UiButton        Menu;
    public DropdownWindow  MenuContent;

    public UiButton         ExitButton;
    public UiButton         ConnectButton;
    public UiButton         DisconnectButton;


    public IngameMenu(float Right,float Top, float MenuPosX, float MenuPosY,float MenuWidth,float MenuHeight,final Stage stage){

        Menu = new UiButton("Menu",Right-100,Top,20,50);
        ExitButton = new UiButton("EXIT",0,0,20,50);;
        DisconnectButton = new UiButton("Disconnect",0,ExitButton.getDisplayY()+50,20,50);;
        ConnectButton = new UiButton("Connect",0,DisconnectButton.getDisplayY()+50,20,50);;
        MenuContent = new DropdownWindow(
                MenuPosX,
                MenuPosY,
                MenuWidth,
                MenuHeight,
                0,
                10
        );

        Menu.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if(MenuContent.isVisible()) {
                    MenuContent.hide(stage);
                }
                else{
                    MenuContent.show(stage);
                }
            }
        });

        ExitButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (GameManager.instance.server != null && GameManager.instance.server.isRunning()) {
                    GameManager.instance.server.send(new PacketLeave(HexaServer.senderId, false));
                    System.out.println("Leaving the game...");
                } else {
                    ScreenManager.getInstance().setCurrentScreen(ScreenType.MAIN_MENU);
                }
            }
        });

        DisconnectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().setCurrentScreen(ScreenType.MAIN_MENU);
            }
        });

        ConnectButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                ScreenManager.getInstance().setCurrentScreen(ScreenType.MAIN_MENU);
            }
        });

        MenuContent.add(ExitButton,stage);
        MenuContent.add(ConnectButton,stage);
        MenuContent.add(DisconnectButton,stage);
        MenuContent.updateElements();

    }
}
