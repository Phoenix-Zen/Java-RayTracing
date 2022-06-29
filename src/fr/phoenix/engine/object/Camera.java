package fr.phoenix.engine.object;

import fr.phoenix.Display;
import fr.phoenix.engine.vector.Vector2;
import fr.phoenix.engine.vector.Vector3;
import lombok.Getter;
import lombok.Setter;

public class Camera {

    @Getter
    private final int FOV = 90; //in degree

    @Getter
    private Vector2 rotation = new Vector2(0,0); //in degree
    //X: 0.06427876096865394, Y:0.0, Z:0.0766044443118978 | 0
    //X: 0.06156614753256583, Y:0.0, Z:0.07880107536067221 | 2
    //X: -0.07660444431189779, Y:0.0, Z:0.06427876096865395 | 90
    //X: -0.06427876096865395, Y:0.0, Z:-0.07660444431189779 | 180

    //LTR
    //X: -0.9090389553440874, Y:0.0, Z:1.0833504408394037 | 40
    //X: 0.0, Y:0.0, Z:1.414213562373095 | 0
    //X: -0.4836895252959505, Y:0.0, Z:1.3289260487773493 | 20

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
        double half = FOV / 2f;
        Vector3 d = dir(rotation.add(new Vector2(half, 0)));
        return d.normalize().times(distFromCam);
    }
    private static final double distFromCam = 1;
    public Vector3 left(){
        double half = FOV / 2f;
        return dir(rotation.sub(new Vector2(half, 0))).normalize().times(distFromCam);
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

    public void rotate(Vector2 rotation) {
        this.rotation = this.rotation.add(rotation);
    }
}
