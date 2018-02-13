package com.hexagon.game.Logic.Components;

import com.hexagon.game.Logic.HexaComponents;



import de.svdragster.logica.components.Component;
import de.svdragster.logica.manager.Entity.Entity;

/**
 * Created by Johannes on 13.02.2018.
 */

public class HexaComponentOwner extends Component {

    HexaComponentOwner(Entity e){
        super.setType(HexaComponents.OWNER);
        super.setBackAssociation(e);
    }
}
