package com.hexagon.game.graphics.screens.myscreens.game.GameUI;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.hexagon.game.Logic.HexaComponents;
import com.hexagon.game.graphics.screens.myscreens.game.GameManager;
import com.hexagon.game.graphics.screens.myscreens.game.ScreenGame;
import com.hexagon.game.graphics.ui.UILabel;
import com.hexagon.game.graphics.ui.UpdateEvent;
import com.hexagon.game.graphics.ui.windows.GroupWindow;
import com.hexagon.game.graphics.ui.windows.Window;

/**
 * Created by Johannes on 25.02.2018.
 */

public class StatusBar {

    public Window      Top = new Window(0,Gdx.graphics.getHeight()-50,Gdx.graphics.getWidth(),50);

    public float level = Top.getHeight()-50+10;
    public float rightEnd = Top.getWidth();
    public float ScreenWidth = Gdx.graphics.getWidth();
    public float ScreenHeight = Gdx.graphics.getHeight();

    public float MenuWidth = 200;
    public float MenuHeight = 200;
    public float StatusHeight = 50;


    public UILabel StatusInfos     = new UILabel(10,level,200,StatusHeight,32,"Test");

    public final UILabel OreResource = new UILabel(250,level,200,StatusHeight,32,"Ore: 0");
    public final UILabel WoodResource = new UILabel(400,level,200,StatusHeight,32,"Wood: 0");
    public final UILabel StoneResource = new UILabel(575,level,200,StatusHeight,32,"Stone: 0");

    public final UILabel PlayerID = new UILabel(775,level,200,StatusHeight,16,GameManager.instance.server.getLocalClientID().toString());
    public IngameMenu  MainMenu;



    public StatusBar(final Stage stage, GroupWindow window){
        MainMenu =  new IngameMenu(
                rightEnd,level,
                ScreenWidth/2-MenuWidth/2,ScreenHeight/2-MenuHeight/2,
                MenuWidth,MenuHeight,
                stage
        );

        StatusInfos.setUpdateEvent(new UpdateEvent(){

            @Override
            public void onUpdate() {
                StatusInfos.getLabel().setText("" + Gdx.graphics.getFramesPerSecond() + " FPS, " + ScreenGame.renderedTiles + " Tiles");
            }
        });

        OreResource.setUpdateEvent(new UpdateEvent() {
            @Override
            public void onUpdate() {
                OreResource.getLabel().setText("Ore: " + GameManager.instance.getPlayerResources().get(HexaComponents.ORE.name()));
            }
        });

        WoodResource.setUpdateEvent(new UpdateEvent() {
            @Override
            public void onUpdate() {
                WoodResource.getLabel().setText("Wood: " + GameManager.instance.getPlayerResources().get(HexaComponents.WOOD.name()));
            }
        });

        StoneResource.setUpdateEvent(new UpdateEvent() {
            @Override
            public void onUpdate() {
                StoneResource.getLabel().setText("Stone: " + GameManager.instance.getPlayerResources().get(HexaComponents.STONE.name()));
            }
        });

        PlayerID.setUpdateEvent(new UpdateEvent() {
            @Override
            public void onUpdate() {
                PlayerID.getLabel().setText(GameManager.instance.server.getLocalClientID().toString());
            }
        });


        Top.add(OreResource,stage);
        Top.add(WoodResource,stage);
        Top.add(StoneResource,stage);
        Top.add(StatusInfos,stage);
        Top.add(PlayerID,stage);
        Top.add(MainMenu.Menu,stage);
        Top.updateElements();

        window.getWindowList().add(Top);
        window.getWindowList().add(MainMenu.MenuContent);
        window.show(stage);


    }




}
