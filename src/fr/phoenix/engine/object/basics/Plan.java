package fr.phoenix.engine.object.basics;

import fr.phoenix.engine.object.Object3D;
import fr.phoenix.engine.object.render.RenderableOject;
import fr.phoenix.engine.vector.RayCast;
import fr.phoenix.engine.vector.Vector3;

import java.awt.*;

public class Plan extends Object3D implements RenderableOject {

    private final Vector3 axis1;
    private final Vector3 axis2;
    private final Color color;

    public Plan(Vector3 pos, Vector3 axis1, Vector3 axis2, Color color) {
        this.position = pos;
        this.axis1 = axis1;
        this.axis2 = axis2;
        this.color = color;
    }

    @Override
    public Color color() {
        return color;
    }

    @Override
    public double raycast(RayCast ray) {
        Vector3 normal = axis1.crossProduct(axis2);
        double D = normal.dotProduct(position);
        double t = -(normal.dotProduct(ray.getOrigin())+D)/(normal.dotProduct(ray.getDirection()));
        if (t < 0)
            return .2;
        System.out.println(normal);
        return 1;
    }
}
