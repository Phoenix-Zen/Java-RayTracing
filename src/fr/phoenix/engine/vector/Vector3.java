package fr.phoenix.engine.vector;

import lombok.Getter;
import lombok.Setter;

public class Vector3 extends Vector2 implements Cloneable{
    @Getter
    @Setter
    private double z;
    public Vector3(double x, double y, double z) {
        super(x, y);
        this.z = z;
    }
    public Vector3 add(Vector3 v){
        return new Vector3(x+v.x, y+v.y, z+v.z);
    }
    public Vector3 sub(Vector3 v){
        return new Vector3(x-v.x, y-v.y, z-v.z);
    }
    public Vector3 times(double t){
        return new Vector3(x*t, y*t, z*t);
    }
    public double dotProduct(Vector3 v){
        return v.x*x+v.y*y+v.z*z;
    }
    public double projectOn(Vector3 v){
        return dotProduct(v)/v.length();
    }
    public Vector3 crossProduct(Vector3 v){
        return new Vector3(v.y*z-v.z*y , v.z*x-v.x*z , v.x*y-v.y*x);
    }


    @Override
    public String toString() {
        return "X: "+x+", Y:"+y+", Z:"+z;
    }

    @Override
    public Vector3 clone() {
        return new Vector3(x,y,z);
    }

    public double length() {
        return Math.sqrt(x*x+y*y+z*z);
    }
    public double squaredLength() {
        return x*x+y*y+z*z;
    }

    public Vector3 normalize() {
        double l = length();
        x/= l;
        y/= l;
        z/= l;
        return this;
    }

    public void move(Vector3 v) {
        x+=v.x;
        y+=v.y;
        z+=v.z;
    }

    public boolean isZero() {
        return x == 0 && y == 0 && z == 0;
    }

    public Vector3 projectOnApply(Vector3 sub) {
        return sub.normalize().times(projectOn(sub));
    }

    public Vector3 max(Vector3 min) {
        return new Vector3(Math.max(min.x,x),Math.max(min.y,y),Math.max(min.z,z));
    }

    public Vector3 min(Vector3 min) {
        return new Vector3(Math.min(min.x,x),Math.min(min.y,y),Math.min(min.z,z));
    }
}
