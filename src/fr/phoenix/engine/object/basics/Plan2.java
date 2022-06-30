package fr.phoenix.engine.object.basics;

import fr.phoenix.engine.object.Object3D;
import fr.phoenix.engine.object.render.Color;
import fr.phoenix.engine.object.render.RenderableOject;
import fr.phoenix.engine.vector.RayCast;
import fr.phoenix.engine.vector.Vector3;

public class Plan2 extends Object3D implements RenderableOject {

    private final Color color;
    private final Vector3 normal;

    public Plan2(Vector3 point,Vector3 normal, Color color) {
        this.position = point;
        this.normal = normal.normalize();
        this.color = color;
    }

    @Override
    public Color color() {
        return color;
    }

    @Override
    public boolean raycast(RayCast ray) {
        double denominator = ray.getDirection().dotProduct(normal);
        if (denominator == 0)
            return false;

        double t = position.sub(ray.getOrigin()).dotProduct(normal)/denominator;
        if (t < 0)
            return false;
        ray.setHit(ray.getOrigin().add(ray.getDirection().times(t-0.001)));
        ray.setNormal(normal);
        return true;
    }
}
