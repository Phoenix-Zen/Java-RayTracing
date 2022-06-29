package fr.phoenix.engine.object.basics;

import fr.phoenix.Display;
import fr.phoenix.engine.object.Camera;
import fr.phoenix.engine.object.Object3D;
import fr.phoenix.engine.object.render.RenderableOject;
import fr.phoenix.engine.vector.Vector2;
import fr.phoenix.engine.vector.Vector3;

import java.awt.*;

public class Sphere extends Object3D implements RenderableOject {

    private final double rayon;

    public Sphere(double rayon, Vector3 position) {
        this.rayon = rayon;
        this.position = position;
    }


    /*@Override
    public boolean intersect(RayCast rayCast) {
        Vector3 L = position.sub(rayCast.getOrigin());
        Vector3 O = rayCast.getDirection();
        double t = L.dotProduct(O);
        if (t < 0)
            return false;
        return L.squaredLength() - t*t < rayon*rayon;
    }*/

    @Override
    public void render(Graphics g, Camera camera) {
        Vector3 dir = position.sub(camera.pos());
        Vector3 midle = camera.forward();


        double angle = Math.acos(dir.dotProduct(midle) / (dir.length()*midle.length()));//rad
        if (camera.toDegree(angle) >= camera.getFOV()/2f)
            return;
        Vector3 ltr = camera.right().sub(camera.left());
        Vector3 ttb = camera.bot().sub(camera.top());
        double aoX = dir.projectOn(ltr)+ltr.length()/2;
        double aoY = dir.projectOn(ttb)+ttb.length()/2;;

        //System.out.println(camera.right().toString()+" RIGHT");

        Vector3 oX = ltr.times(aoX/ltr.length());
        Vector3 oY = ttb.times(aoY/ttb.length());

        Vector3 o = camera.forward().add(oX).add(oY);
        double camToO = o.sub(camera.pos()).length();
        double distScreen = camToO / position.sub(camera.pos()).length() * rayon * 2;

        double transposedRayon = rayon * camToO / (camToO+dir.length());

        Vector2 size = Display.getDimension().times(transposedRayon / ltr.length());
        int sizeX = (int) size.getX();
        int sizeY = (int) size.getY();
        int placeX = (int) (Display.getWIDTH() * (aoX < 0 ? -1 : 1) * oX.length()/ltr.length());
        int placeY = (int) (Display.getHEIGHT() * (aoY < 0 ? -1 : 1) * oY.length()/ttb.length());
        g.fillOval(placeX- sizeX/2, placeY- sizeY/2, sizeX, sizeY);
    }

}
