package fr.phoenix.engine.object.render;

import fr.phoenix.engine.vector.RayCast;

public interface RenderableOject {
    //void render(Graphics g, Camera camera);

    Color color();
    boolean raycast(RayCast ray);
}
