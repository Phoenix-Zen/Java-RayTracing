package fr.phoenix.engine.object.basics;

import fr.phoenix.engine.object.Object3D;
import fr.phoenix.engine.object.render.RenderableOject;
import fr.phoenix.engine.vector.RayCast;
import fr.phoenix.engine.vector.Vector3;

import java.awt.*;

public class Triangle extends Object3D implements RenderableOject {

    private final Vector3 a;
    private final Vector3 b;
    private final Vector3 c;

    private static double EPSILON = 0.0000001;

    public Triangle(Vector3 a, Vector3 b, Vector3 c) {
        this.position = a;
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public Color color() {
        return Color.BLACK;
    }

    @Override
    public double raycast(RayCast ray) {
        Vector3 edge1 = a.sub(c);
        Vector3 edge2 = b.sub(c);
        Vector3 h = ray.getDirection().crossProduct(edge2);
        double dist = h.dotProduct(edge1);
        if (dist > -EPSILON && dist < EPSILON)
            return 1;
        double f = 1.0 / dist;
        Vector3 s = ray.getOrigin().sub(c);
        double u = f*(s.dotProduct(h));
        if (u < 0.0 || u > 1.0)
            return 1;
        Vector3 q = s.crossProduct(edge1);
        double v = f * ray.getDirection().dotProduct(q);
        if (v < 0.0 || u + v > 1.0)
            return 1;
        // On calcule t pour savoir ou le point d'intersection se situe sur la ligne.
        double t = f * edge2.dotProduct(q);

        Vector3 intersect;
        if (t > EPSILON) // // Intersection avec le rayon
        {
            intersect = ray.getOrigin().add(ray.getDirection().times(t));
            return 1;
        } else // On a bien une intersection de droite, mais pas de rayon.
            return 1;
    }
}
