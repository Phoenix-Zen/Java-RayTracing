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

    private final Vector3 normal;
    public Triangle(Vector3 a, Vector3 b, Vector3 c, Color color) {
        this(a,b,c,color,0);
    }
    public Triangle(Vector3 a, Vector3 b, Vector3 c, Color color, double reflection) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.color = color;
        this.reflection = reflection;
        normal = b.sub(a).crossProduct(c.sub(a));
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
        double t = a.sub(ray.getOrigin()).dotProduct(normal)/denominator;
        if (t < 0)
            return false;
        Vector3 hit = ray.getOrigin().add(ray.getDirection().times(t-0.001));

        if (!checkEdge(a,b, hit))
            return false;
        if (!checkEdge(b,c, hit))
            return false;
        if (!checkEdge(c,a, hit))
            return false;

        //v2 - v1
        Vector3 edge1 = c.sub(b);
        //v0 - v2
        Vector3 edge2 = a.sub(c);

        ray.setHit(hit);
        ray.setNormal(normal.times(denominator > 0 ? -1 : 1));
        ray.setReflection(reflection);
        return true;
    }

    private boolean checkEdge(Vector3 a, Vector3 b, Vector3 hit) {
        Vector3 edge = b.sub(a);
        Vector3 vh = hit.sub(a);
        Vector3 C = edge.crossProduct(vh);
        //double v = (C.length()/2)/normal.length();
        return !(normal.dotProduct(C) < 0);
    }

    public Vector3 getMax() {
        double x = Math.max(c.getX(), Math.max(a.getX(), b.getX()));
        double y = Math.max(c.getY(), Math.max(a.getY(), b.getY()));
        double z = Math.max(c.getZ(), Math.max(a.getZ(), b.getZ()));
        return new Vector3(x,y,z);
    }
    public Vector3 getMin() {
        double x = Math.min(c.getX(), Math.min(a.getX(), b.getX()));
        double y = Math.min(c.getY(), Math.min(a.getY(), b.getY()));
        double z = Math.min(c.getZ(), Math.min(a.getZ(), b.getZ()));
        return new Vector3(x,y,z);
    }
}
