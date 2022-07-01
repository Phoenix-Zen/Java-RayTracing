package fr.phoenix.engine;

import fr.phoenix.Display;
import fr.phoenix.engine.object.Object3D;
import fr.phoenix.engine.object.Player;
import fr.phoenix.engine.object.basics.*;
import fr.phoenix.engine.object.render.Color;
import fr.phoenix.engine.object.render.RenderableOject;
import fr.phoenix.engine.vector.RayCast;
import fr.phoenix.engine.vector.Vector2;
import fr.phoenix.engine.vector.Vector3;
import lombok.Getter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GraphicEngine{

    @Getter
    private final int width;
    @Getter
    private final int height;
    @Getter
    private final Display display;

    @Getter
    private Vector3 light;
    @Getter
    private double lighting = 1f;
    @Getter
    private static float ambientLight = .001f;

    private BufferedImage skybox;

    public GraphicEngine(Display display) {
        try {
            skybox = ImageIO.read(new File("/home/flo/IdeaProjects/SimpleGame/assets/Nebula.png"));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        this.display = display;
        this.width = display.getWidth();
        this.height = display.getHeight();

        this.light = new Vector3(15, 5, 15);

        objects.add(new Sphere(1, new Vector3(-4, 0, 0), Color.WHITE, .6));
        //objects.add(new Cube(new Vector3(3,0,3), new Vector3(1,0,0), new Vector3(0,0,1), 2,Color.WHITE, .15));
        objects.add(new Triangle(new Vector3(11, 2.2, 11),new Vector3(9,2.2,9),new Vector3(9,2.2,11), Color.BLUE, .2));
        //for (int i = 0; i < 20; i++) {
        objects.add(new Cube(new Vector3(10,0,10), new Vector3(1,0,0), new Vector3(0,0,1), 2,Color.WHITE, .15));
        //}
        //objects.add(new Plan2(new Vector3(-4,0,0), new Vector3(1, 0, 0), Color.WHITE));
        //objects.add(new Plan(new Vector3(0, 0,1), new Vector3(1, 0,0), new Vector3(0, 1,0), Color.BLUE));
    }

    private List<Object3D> objects = new ArrayList<>();
    private long lastTime = System.currentTimeMillis();
    private int fps = 0;
    private int frames = 0;
    @Getter
    private Player player = new Player();

    public boolean rayCast(RayCast ray){
        RayCast nearest = null;
        int out = 0;
        for (Object3D obj : objects) {
            RayCast clone = ray.clone();
            if (!(obj instanceof RenderableOject))
                continue;
            RenderableOject ro = (RenderableOject) obj;
            if (!obj.intersection(ray))
                continue;
            if(ro.raycast(clone)) {
                if (nearest == null || clone.getHit().sub(clone.getOrigin()).length() < nearest.getHit().sub(nearest.getOrigin()).length()) {
                    if (clone.getObject3D() == null)
                        clone.setObject3D(obj);
                    nearest = clone;
                }
            }
        }
        if (nearest == null)
            return false;
        ray.apply(nearest);
        return true;
    }

    public Color getColor(RayCast ray, int reflectTime){
        if(rayCast(ray)) {
            Vector3 hit = ray.getHit();
            float v = Math.min(Math.max(ambientLight, (float) (lighting*light.sub(hit).normalize().dotProduct(ray.getNormal().normalize()))), 1);
            RayCast rayLight = new RayCast(hit, light.sub(hit).normalize());
            if (rayCast(rayLight))
                v = ambientLight;
            RenderableOject ro = (RenderableOject) ray.getObject3D();
            Color color = ro.color();
            if (ray.getReflection() > 0 && reflectTime > 0){
                Vector3 reflect = ray.getDirection().sub(ray.getNormal().times(2*ray.getDirection().dotProduct(ray.getNormal())));
                RayCast rayReflection = new RayCast(hit, reflect);
                rayCast(rayReflection);
                Color reflectedColor = getColor(rayReflection, reflectTime - 1);
                color = color.mix(reflectedColor, (float) ray.getReflection());
            }
            return color.multiply(v);
        }else {
            Vector3 dir = ray.getDirection().normalize();
            double u = 0.5+Math.atan2(dir.getX(), dir.getZ())/(2*Math.PI);
            double v = 0.5-Math.asin(dir.getY())/Math.PI;
            int rgb = 0;
            try{
                rgb = skybox.getRGB((int) (u*skybox.getWidth())+ (u > .9? -1 : 0), (int) (v*skybox.getHeight())+ (v > .9? -1 : 0));
            }catch (ArrayIndexOutOfBoundsException e){
                System.out.println("ERROR");
            }
            return new Color(rgb);
        }
    }

    public void paint(Graphics graphics) {
        int resX = (int) player.getCamera().resolution.getX();
        int resY = (int) player.getCamera().resolution.getY();
        int ratioX = (int) (Display.getWIDTH() * 1.0 / resX);
        int ratioY = (int) (Display.getHEIGHT() * 1.0 / resY);
        for (int i = 0; i < resX; i++) {
            for (int j = 0; j < resY; j++) {
                RayCast ray = player.getCamera().getRay((int) (i * ratioX), (int) (j * ratioY));
                Color color = getColor(ray, 1);

                graphics.setColor(color.getColor());
                graphics.fillRect(i * ratioX, j * ratioY, ratioX, ratioY);

            }
        }
    }

    private boolean reversed = false;
    public void update() {
        if (!reversed)
            return;
        player.getCamera().rotate(new Vector2(0.2, 0).times(reversed ? -1 : 1));
        if (player.getCamera().getRotation().getX() > 40)
            reversed = true;
        if (player.getCamera().getRotation().getX() < -40)
            reversed = false;
        return;
        /*for (Object3D object : objects) {
            object.move(new Vector3((reversed ? -1 : 1) * 0.1, 0, 0));
            if (object.getPosition().length() > 8f)
                reversed = true;
            else if (object.getPosition().length() < 1f)
                reversed = false;
        }*/
    }
}
