package fr.phoenix.engine.object.basics;

import fr.phoenix.engine.object.Object3D;
import fr.phoenix.engine.object.render.Color;
import fr.phoenix.engine.object.render.RenderableOject;
import fr.phoenix.engine.vector.RayCast;
import fr.phoenix.engine.vector.Vector3;

public class Triangle extends Object3D implements RenderableOject {

    private final Color color;
    private final Vector3 a;
    private final Vector3 b;
    private final Vector3 c;
    private final double reflection;


    public Triangle(Vector3 a, Vector3 b, Vector3 c, Color color) {
        this(a,b,c,color,0);
    }
    public Triangle(Vector3 a, Vector3 b, Vector3 c, Color color, double reflection) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.color = color;
        this.reflection = reflection;
    }

    @Override
    public Color color() {
        return color;
    }

    @Override
    public boolean raycast(RayCast ray) {
        Vector3 normal = b.sub(a).crossProduct(c.sub(a));
        double denominator = ray.getDirection().dotProduct(normal);
        if (denominator == 0)
            return false;
        double t = a.sub(ray.getOrigin()).dotProduct(normal)/denominator;
        if (t < 0)
            return false;
        Vector3 hit = ray.getOrigin().add(ray.getDirection().times(t-0.001));
        t-=-0.001;
        if (!checkBarycentric(a,b,c,hit))
            return false;
        ray.setHit(hit);
        ray.setNormal(normal.times(normal.dotProduct(ray.getDirection()) < 0 ? 1 : -1));
        ray.setReflection(reflection);
        return true;
    }

    private boolean checkBarycentric(Vector3 a, Vector3 b, Vector3 c, Vector3 hit) {
        Vector3 v0 = b.sub(a);
        Vector3 v1 = c.sub(a);
        Vector3 v2 = hit.sub(a);
        double d00 = v0.dotProduct(v0);
        double d01 = v0.dotProduct(v1);
        double d11 = v1.dotProduct(v1);
        double d20 = v2.dotProduct(v0);
        double d21 = v2.dotProduct(v1);
        double denom = d00 * d11 - d01 * d01;
        double v = (d11 * d20 - d01 * d21) / denom;
        double w = (d00 * d21 - d01 * d20) / denom;
        double u = 1.0 - v - w;
        return u > 0 && u < 1 && v > 0 && v < 1 && w > 0 && w < 1;
    }
}
