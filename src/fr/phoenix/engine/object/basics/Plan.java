package fr.phoenix.engine.object.basics;

import fr.phoenix.engine.object.Object3D;
import fr.phoenix.engine.object.render.Color;
import fr.phoenix.engine.object.render.RenderableOject;
import fr.phoenix.engine.vector.RayCast;
import fr.phoenix.engine.vector.Vector3;

public class Plan extends Object3D implements RenderableOject {

    private final Color color;
    private final float height;
    private final double reflection;

    public Plan(float height, Color color) {
        this(height, color, 0);
    }
    public Plan(float height, Color color, double reflection) {
        this.height = height;
        this.position = new Vector3(0,height,0);
        this.color = color;
        this.reflection = reflection;
    }

    @Override
    public boolean raycast(RayCast ray) {
        if (ray.getDirection().getY() == 0)
            return false;
        if (ray.getOrigin().getY() < height && ray.getDirection().getY() > 0 || ray.getOrigin().getY() > height && ray.getDirection().getY() < 0) {
            ray.setNormal(ray.getOrigin().getY() < height ? new Vector3(0, -1, 0) : new Vector3(0, 1, 0));
            double k = (height-ray.getOrigin().getY())/ ray.getDirection().getY();
            Vector3 hit = ray.getOrigin().add(ray.getDirection().times(Math.abs(k)));
            hit.setY(height);
            ray.setHit(hit);
            ray.setReflection(reflection);
            ray.setColor(color);
            return true;
        }
        return false;
    }
}
