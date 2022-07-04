package fr.phoenix.engine.object.basics;

import fr.phoenix.engine.object.Object3D;
import fr.phoenix.engine.object.render.Color;
import fr.phoenix.engine.object.render.RenderableOject;
import fr.phoenix.engine.vector.RayCast;
import fr.phoenix.engine.vector.Vector3;
import lombok.Getter;

import java.util.ArrayList;

public class BoundingBox extends Object3D implements RenderableOject {
    @Getter
    protected ArrayList<Object3D> inside = new ArrayList<>();

    public BoundingBox(Vector3 pos, double size) {
        this.position = pos;
        mins = pos.clone();
        maxs = mins.add(new Vector3(size,size,size));
    }
    public BoundingBox(ArrayList<? extends Object3D> triangles) {
        this.inside.addAll(triangles);
        apply();
    }

    public void apply(){
        maxs = new Vector3(Double.NEGATIVE_INFINITY,Double.NEGATIVE_INFINITY,Double.NEGATIVE_INFINITY);
        mins = new Vector3(Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY);
        for (Object3D triangle : inside) {
            maxs = maxs.max(triangle.maxs);
            mins = mins.min(triangle.mins);
        }
        this.position = mins;
    }

    @Override
    public boolean raycast(RayCast ray) {
        RayCast nearest = null;
        for (Object3D obj : inside) {
            if (!(obj instanceof RenderableOject))
                continue;
            RenderableOject ro = (RenderableOject) obj;
            RayCast clone = ray.clone();
            if(!ro.raycast(clone))
                continue;
            if (clone.getObject3D() == null)
                clone.setObject3D(obj);
            if (nearest == null || nearest.getHit().sub(nearest.getOrigin()).length() > clone.getHit().sub(clone.getOrigin()).length())
                nearest = clone;
        }
        if (nearest == null)
            return false;
        ray.apply(nearest);
        return true;
    }

    public void addObject(Object3D obj) {
        inside.add(obj);
    }

    public int insideItems() {
        return inside.size();
    }

}
