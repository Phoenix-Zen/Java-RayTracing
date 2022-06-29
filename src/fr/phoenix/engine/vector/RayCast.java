package fr.phoenix.engine.vector;

import lombok.Getter;

public class RayCast {

    @Getter
    private final Vector3 origin;
    @Getter
    private final Vector3 direction;

    public RayCast(Vector3 origin, Vector3 direction) {
        this.origin = origin;
        this.direction = direction;
    }
}
