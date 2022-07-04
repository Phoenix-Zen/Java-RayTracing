package fr.phoenix.engine.object.basics;

import fr.phoenix.engine.object.render.Color;
import fr.phoenix.engine.vector.Vector3;

public class Cube extends BoundingBox {

    private final double size;
    private final Vector3 normal;
    private final Vector3 right;
    private final Vector3 forward;
    private final double reflection;
    private final Color color;

    public Cube(Vector3 pos, Vector3 forward, Vector3 right, double size, Color color) {
        this(pos, forward,right, size,color,0);
    }
    public Cube(Vector3 pos, Vector3 forward, Vector3 right, double size, Color color, double reflection) {
        super(pos, size);
        this.size = size;
        this.position = pos;
        this.forward = forward.normalize();
        this.right = right.normalize();
        this.reflection = reflection;
        this.color = color;

        Vector3 tr = pos.add(forward.times(size/2.0)).add(right.times(size/2.0));
        Vector3 tl = tr.sub(right.times(size));
        Vector3 bl = tl.sub(forward.times(size));
        Vector3 br = bl.add(right.times(size));
        inside.add(new Triangle(tl,tr,br, color, reflection));
        inside.add(new Triangle(tl,bl,br, color, reflection));
        normal = forward.crossProduct(right).normalize();
        add(tr,br);
        add(br,bl);
        add(bl,tl);
        add(tl,tr);
        Vector3 up = normal.times(size);
        inside.add(new Triangle(tl.add(up),tr.add(up),br.add(up), color, reflection));
        inside.add(new Triangle(tl.add(up),bl.add(up),br.add(up), color, reflection));

        apply();
    }

    private void add(Vector3 a, Vector3 b) {
        Vector3 up = normal.times(size);
        inside.add(new Triangle(a,b,a.add(up), color,reflection));
        inside.add(new Triangle(b,a.add(up),b.add(up), color, reflection));
    }
}
