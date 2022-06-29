package fr.phoenix.engine.object;

import fr.phoenix.engine.vector.Vector3;
import lombok.Getter;

public class Object3D {
    @Getter
    protected Vector3 position;

    public void move(Vector3 v) {
        position = position.add(v);
    }
}
