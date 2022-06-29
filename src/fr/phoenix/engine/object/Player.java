package fr.phoenix.engine.object;

import fr.phoenix.engine.vector.Vector3;
import lombok.Getter;

public class Player extends Object3D{

    @Getter
    private static double MOVEMENT_SPEED = 0.1;

    public Player() {
        position = new Vector3(0,0,0);
    }

    @Getter
    Camera camera = new Camera(this);


}
