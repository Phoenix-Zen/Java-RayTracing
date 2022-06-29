package fr.phoenix.engine.vector;

import lombok.Getter;
import lombok.Setter;

public class Vector2 implements Cloneable{

    @Getter
    @Setter
    protected double x;
    @Getter
    @Setter
    protected double y;

    public Vector2(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector2 add(Vector2 v){
        return new Vector2(x+v.x, y+v.y);
    }
    public Vector2 sub(Vector2 v){
        return new Vector2(x-v.x, y-v.y);
    }
    public Vector2 times(double t){
        return new Vector2(x*t,y*t);
    }

    @Override
    public String toString() {
        return "X: "+x+", Y:"+y;
    }
    @Override
    protected Vector2 clone() {
        return new Vector2(x,y);
    }
}
