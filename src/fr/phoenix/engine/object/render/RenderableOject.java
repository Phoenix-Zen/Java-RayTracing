package fr.phoenix.engine.object.render;

import fr.phoenix.engine.vector.RayCast;
import fr.phoenix.engine.vector.Vector3;

public interface RenderableOject {
    //void render(Graphics g, Camera camera);
    Color color();
    boolean raycast(RayCast ray);
}
