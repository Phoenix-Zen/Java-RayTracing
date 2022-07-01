package fr.phoenix.engine.object;

import fr.phoenix.engine.vector.RayCast;
import fr.phoenix.engine.vector.Vector3;
import lombok.Getter;

public class Object3D {
    public Vector3 mins = new Vector3(Double.NEGATIVE_INFINITY,Double.NEGATIVE_INFINITY,Double.NEGATIVE_INFINITY);
    public Vector3 maxs = new Vector3(Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY,Double.POSITIVE_INFINITY);
    @Getter
    protected Vector3 position;

    public void move(Vector3 v) {
        position = position.add(v);
    }

    public boolean intersection(RayCast ray) {
        double tmin = Double.NEGATIVE_INFINITY;
        double tmax = Double.POSITIVE_INFINITY;

        double x = ray.getDirection().getX();
        if (x != 0.0) {
            double ox = ray.getOrigin().getX();
            double tx1 = (mins.getX() - ox)/x;
            double tx2 = (maxs.getX() - ox)/x;

            tmin = Math.max(tmin, Math.min(tx1, tx2));
            tmax = Math.min(tmax, Math.max(tx1, tx2));
        }

        double y = ray.getDirection().getY();
        if (y != 0.0) {
            double oy = ray.getOrigin().getY();
            double ty1 = (mins.getY() - oy)/y;
            double ty2 = (maxs.getY() - oy)/y;

            tmin = Math.max(tmin, Math.min(ty1, ty2));
            tmax = Math.min(tmax, Math.max(ty1, ty2));
        }
        double z = ray.getDirection().getZ();
        if (z != 0.0) {
            double oz = ray.getOrigin().getZ();
            double tz1 = (mins.getZ() - oz)/z;
            double tz2 = (maxs.getZ() - oz)/z;

            tmin = Math.max(tmin, Math.min(tz1, tz2));
            tmax = Math.min(tmax, Math.max(tz1, tz2));
        }
        return tmax >= tmin;
    }
}
