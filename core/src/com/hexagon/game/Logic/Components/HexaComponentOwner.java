package com.hexagon.game.Logic.Components;

import com.hexagon.game.Logic.HexaComponents;



import de.svdragster.logica.components.Component;
import de.svdragster.logica.manager.Entity.Entity;

/**
 * Created by Johannes on 13.02.2018.
 */

public class HexaComponentOwner extends Component {

    public String   name;

    public HexaComponentOwner(String Name){
        super.setType(HexaComponents.OWNER);

        name = Name;
    }

    public void setOwner(Entity e){ setBackAssociation(e);}

    @Override
    public String toString(){
        return "Owner( " + name + " ) => " + super.getID();
    }
}
