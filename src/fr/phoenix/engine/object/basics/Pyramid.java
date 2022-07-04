package fr.phoenix.engine.object.basics;

import fr.phoenix.engine.object.render.Color;
import fr.phoenix.engine.vector.Vector3;

public class Pyramid extends BoundingBox {

    private final double size;
    private final Vector3 normal;
    private final Vector3 right;
    private final Vector3 forward;
    private final double reflection;

    public Pyramid(Vector3 pos, Vector3 forward, Vector3 right, double size, Color color) {
        this(pos, forward,right, size,color,0);
    }
    public Pyramid(Vector3 pos, Vector3 forward, Vector3 right, double size, Color color, double reflection) {
        super(pos, size);
        this.size = size;
        this.position = pos;
        this.forward = forward.normalize();
        this.right = right.normalize();
        this.reflection = reflection;
        this.normal = forward.crossProduct(right);
        Vector3 tr = pos.sub(normal.times(size/2.0)).add(forward.times(size/2.0)).add(right.times(size/2.0));
        Vector3 tl = tr.sub(right.times(size));
        Vector3 bl = tl.sub(forward.times(size));
        Vector3 br = bl.add(right.times(size));
        Vector3 top = pos.add(normal.times(size/2.0));
        inside.add(new Triangle(tr,tl,top, color, reflection));
        inside.add(new Triangle(tl,bl,top, color, reflection));
        inside.add(new Triangle(bl,br,top, color, reflection));
        inside.add(new Triangle(br,tr,top, color, reflection));

        inside.add(new Triangle(tr,tl,bl, color, reflection));
        inside.add(new Triangle(bl,tr,br, color, reflection));
        apply();
    }

}
