package fr.phoenix.engine.object;

import fr.phoenix.Display;
import fr.phoenix.engine.vector.RayCast;
import fr.phoenix.engine.vector.Vector2;
import fr.phoenix.engine.vector.Vector3;
import lombok.Getter;

public class Camera {

    @Getter
    private final int FOV = 90; //in degreeObject
    @Getter
    public Vector2 resolution = new Vector2(150, 150);

    @Getter
    private Vector2 rotation = new Vector2(-90,0); //in degree
    //X: 0.5065019615726758, Y:0.6977904598416802, Z:0.5065019615726757 | 0

    //VECT2 X: 0.7071067811865476, Y:0.0, Z:0.7071067811865475 | 0
    //VECT2 X: -0.7071067811865477, Y:0.0, Z:-0.7071067811865475 | 180
    private final Player player;

    public Camera(Player player) {
        this.player = player;
    }

    public Vector3 dir(Vector2 rot){
        double cosY = cos(rot.getY());
        return new Vector3(cosY * cos(rot.getX()), sin(rot.getY()), cosY * sin(rot.getX()));
    }


    public double toRad(double v){
        return v*Math.PI/180;
    }
    public double toDegree(double v){
        return v*180/Math.PI;
    }
    private double sin(double v){
        return Math.sin(toRad(v));
    }
    private double cos(double v){
        return Math.cos(toRad(v));
    }


    public Vector3 pos() {
        return player.position;
    }

    public Vector3 forward() {
        return right().add(left()).times(0.5);
    }

    public Vector3 right(){
        return getRay(Display.getWIDTH(), Display.getHEIGHT()/2).getDirection().normalize().times(distFromCam);
    }
    private static final double distFromCam = 1;
    public Vector3 left(){
        return getRay(0,Display.getHEIGHT()/2).getDirection().normalize().times(distFromCam);
    }
    public Vector3 top(){
        Vector3 forward = forward();
        return forward.crossProduct(right().sub(forward)).add(forward);
    }
    public Vector3 bot(){
        return forward().add(top().sub(forward()).times(-1));
    }

    public Vector2 screenSize(){
        return new Vector2(left().sub(right()).length(), top().sub(bot()).length());
    }

    public void rotate(Vector2 add) {
        this.rotation = this.rotation.add(add);
        Vector2 v = new Vector2(0, rotation.getY() + FOV / 2.0 - FOV * 1.0 * 10 / Display.getHEIGHT());
    }

    public RayCast getRay(int i, int j){
        return new RayCast(pos(), dir(new Vector2(rotation.getX()+FOV/2.0 - i*FOV/(float) Display.getWIDTH(),rotation.getY()+FOV/2.0 - j*FOV/(float)Display.getHEIGHT())));
    }
}
