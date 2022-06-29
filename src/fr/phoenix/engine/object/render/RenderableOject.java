package fr.phoenix.engine.object.render;

import fr.phoenix.engine.object.Camera;
import fr.phoenix.engine.vector.RayCast;

import java.awt.*;

public interface RenderableOject {
    //void render(Graphics g, Camera camera);

    Color color();
    double raycast(RayCast ray);
}
