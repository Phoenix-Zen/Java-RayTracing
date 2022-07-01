package fr.phoenix.engine.object.basics;

import fr.phoenix.engine.object.Object3D;
import fr.phoenix.engine.object.render.Color;
import fr.phoenix.engine.object.render.RenderableOject;
import fr.phoenix.engine.vector.RayCast;
import fr.phoenix.engine.vector.Vector2;
import fr.phoenix.engine.vector.Vector3;

import java.util.ArrayList;

public class Cube extends Object3D implements RenderableOject {

    private final double size;
    private final Vector3 normal;
    private final Vector3 right;
    private final Vector3 forward;
    private final Color color;
    private final double reflection;
    private ArrayList<Triangle> triangles = new ArrayList<>();

    public Cube(Vector3 pos, Vector3 forward, Vector3 right, double size, Color color) {
        this(pos, forward,right, size,color,0);
    }
    public Cube(Vector3 pos, Vector3 forward, Vector3 right, double size, Color color, double reflection) {
        this.size = size;
        this.color = color;
        this.position = pos;
        this.forward = forward.normalize();
        this.right = right.normalize();
        this.reflection = reflection;

        Vector3 tr = pos.add(forward.times(size/2.0)).add(right.times(size/2.0));
        Vector3 tl = tr.sub(right.times(size));
        Vector3 bl = tl.sub(forward.times(size));
        Vector3 br = bl.add(right.times(size));
        triangles.add(new Triangle(tl,tr,br, color, reflection));
        triangles.add(new Triangle(tl,bl,br, color, reflection));
        normal = forward.crossProduct(right).normalize();
        add(tr,br);
        add(br,bl);
        add(bl,tl);
        add(tl,tr);
        Vector3 up = normal.times(size);
        triangles.add(new Triangle(tl.add(up),tr.add(up),br.add(up), color, reflection));
        triangles.add(new Triangle(tl.add(up),bl.add(up),br.add(up), color, reflection));

        maxs = new Vector3(Double.NEGATIVE_INFINITY,Double.NEGATIVE_INFINITY,Double.NEGATIVE_INFINITY);
        mins = new Vector3(Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY);

        for (Triangle triangle : triangles) {
            maxs = maxs.max(triangle.getMax());
            mins = mins.min(triangle.getMin());
        }
    }

    private void add(Vector3 a, Vector3 b) {
        Vector3 up = normal.times(size);
        triangles.add(new Triangle(a,b,a.add(up), color,reflection));
        triangles.add(new Triangle(b,a.add(up),b.add(up), color, reflection));
    }

    @Override
    public Color color() {
        return color;
    }

    @Override
    public boolean raycast(RayCast ray) {
        RayCast nearest = null;
        for (Triangle triangle : triangles) {
            RayCast clone = ray.clone();
            if(!triangle.raycast(clone))
                continue;
            clone.setObject3D(triangle);
            if (nearest == null || nearest.getHit().length() > clone.getHit().length())
                nearest = clone;
        }
        if (nearest == null)
            return false;
        ray.apply(nearest);
        return true;
    }

}
