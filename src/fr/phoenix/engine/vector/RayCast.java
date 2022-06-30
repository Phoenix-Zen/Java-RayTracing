package fr.phoenix.engine.vector;

import fr.phoenix.engine.object.Object3D;
import lombok.Getter;
import lombok.Setter;

public class RayCast {

    @Getter
    private final Vector3 origin;
    @Getter
    private final Vector3 direction;
    @Getter
    @Setter
    private Vector3 hit = null;
    @Getter
    @Setter
    private Object3D object3D = null;
    @Getter
    @Setter
    private Vector3 normal = null;
    @Getter
    @Setter
    private double reflection = 0;


    public RayCast(Vector3 origin, Vector3 direction) {
        this.origin = origin;
        this.direction = direction.normalize();
    }

    public RayCast clone(){
        return new RayCast(origin, direction);
    }
    public void apply(RayCast rayCast){
        hit = rayCast.getHit();
        object3D = rayCast.getObject3D();
        normal = rayCast.getNormal();
        reflection = rayCast.getReflection();
    }
}
