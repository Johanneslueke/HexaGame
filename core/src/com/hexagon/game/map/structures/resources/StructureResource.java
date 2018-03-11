package com.hexagon.game.map.structures.resources;

import com.hexagon.game.map.structures.Structure;
import com.hexagon.game.map.structures.StructureType;

/**
 * Created by Sven on 16.02.2018.
 */

public class StructureResource extends Structure {

    private ResourceType resourceType;

    public StructureResource(ResourceType resourceType) {
        super(StructureType.ORE);
        this.resourceType = resourceType;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }
}
