package fr.phoenix.engine.object.basics;

import fr.phoenix.engine.object.Object3D;
import fr.phoenix.engine.object.render.Color;
import fr.phoenix.engine.object.render.RenderableOject;
import fr.phoenix.engine.vector.RayCast;
import fr.phoenix.engine.vector.Vector3;

public class Sphere extends Object3D implements RenderableOject {

    private final double rayon;
    private final Color color;
    private final double reflection;

    public Sphere(double rayon, Vector3 position, Color color) {
        this(rayon, position, color, 0);
    }
    public Sphere(double rayon, Vector3 position, Color color, double reflection) {
        this.rayon = rayon;
        this.position = position;
        this.color = color;
        this.reflection = reflection;
    }


    @Override
    public Color color() {
        return color;
    }

    /*@Override
        public boolean intersect(RayCast rayCast) {
            Vector3 L = position.sub(rayCast.getOrigin());
            Vector3 O = rayCast.getDirection();
            double t = L.dotProduct(O);
            if (t < 0)
                return false;
            return L.squaredLength() - t*t < rayon*rayon;
        }
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
    */
    @Override
    public boolean raycast(RayCast ray) {
        Vector3 dir = ray.getDirection();
        Vector3 hyp = position.sub(ray.getOrigin());
        double angle = (Math.acos(hyp.dotProduct(dir)/hyp.length()*dir.length()))*180/Math.PI;
        if (angle > 90) {return false;}
        double side = hyp.projectOn(dir);

        //0 dark | 1 light
        if (rayon*rayon <= hyp.squaredLength() - side*side)
            return false;

        double xsqr = rayon*rayon - hyp.squaredLength() + side*side;
        Vector3 onSphere = ray.getOrigin().add(dir.normalize().times(side-Math.sqrt(xsqr)));
        Vector3 normal = onSphere.sub(position).normalize();
        ray.setNormal(normal);
        ray.setHit(onSphere);
        ray.setReflection(reflection);
        return true;
    }

}
