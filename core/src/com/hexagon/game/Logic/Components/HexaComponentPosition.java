package com.hexagon.game.Logic.Components;

import com.hexagon.game.Logic.HexaComponents;
import com.hexagon.game.map.Point;

import de.svdragster.logica.components.Component;

/**
 * Created by Johannes on 19.02.2018.
 */

public class HexaComponentPosition extends Component {

    public Point position;
    public HexaComponentPosition(Point pos){
        setType(HexaComponents.POSITION);

        this.position = new Point(pos.getX(),pos.getY(),pos.getY());
    }
}
