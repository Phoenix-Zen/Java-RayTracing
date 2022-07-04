package fr.phoenix.engine;

import fr.phoenix.Display;
import fr.phoenix.engine.object.Object3D;
import fr.phoenix.engine.object.Player;
import fr.phoenix.engine.object.Scene;
import fr.phoenix.engine.object.basics.Plan;
import fr.phoenix.engine.object.render.Color;
import fr.phoenix.engine.object.render.RenderableOject;
import fr.phoenix.engine.vector.RayCast;
import fr.phoenix.engine.vector.Vector2;
import fr.phoenix.engine.vector.Vector3;
import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GraphicEngine{

    @Getter
    private final int width;
    @Getter
    private final int height;
    @Getter
    private final Display display;
    private final Scene scene;
    public GraphicEngine(Display display) {
        this.display = display;
        this.width = display.getWidth();
        this.height = display.getHeight();
        this.scene = new Scene();

        scene.setLight(new Vector3(3, 30, 2));

        ObjLoader loader = new ObjLoader("/home/flo/IdeaProjects/SimpleGame/assets/tree.obj");
        loader.load();
        scene.addObject(loader.getObject());
        scene.addObject(new Plan(-1, Color.DARK_GRAY));
        System.out.println("ENDED");
    }
    @Getter
    private final Player player = new Player();

    public boolean rayCast(RayCast ray){
        RayCast nearest = null;
        int out = 0;
        for (Object3D obj : scene.getObjects()) {
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
            float ambientLight = scene.getAmbientLight();
            Vector3 light = scene.getLight();
            float v = Math.min(Math.max(ambientLight, (float) (scene.getLighting()*light.sub(hit).normalize().dotProduct(ray.getNormal().normalize()))), 1);
            RayCast rayLight = new RayCast(hit, light.sub(hit).normalize());
            if (rayCast(rayLight))
                v = ambientLight;
            RenderableOject ro = (RenderableOject) ray.getObject3D();
            Color color = ray.getColor();
            if (color == null)
                return null;
            if (ray.getReflection() > 0 && reflectTime > 0){
                Vector3 reflect = ray.getDirection().sub(ray.getNormal().times(2*ray.getDirection().dotProduct(ray.getNormal())));
                RayCast rayReflection = new RayCast(hit, reflect);
                rayCast(rayReflection);
                Color reflectedColor = getColor(rayReflection, reflectTime - 1);
                if (reflectedColor == null)
                    return color.multiply(v);
                color = color.mix(reflectedColor, (float) ray.getReflection());
            }
            return color.multiply(v);
        }else {
            Vector3 dir = ray.getDirection().normalize();
            double u = 0.5+Math.atan2(dir.getX(), dir.getZ())/(2*Math.PI);
            double v = 0.5-Math.asin(dir.getY())/Math.PI;
            int rgb = 0;
            try{
                BufferedImage skybox = scene.getSkybox();
                rgb = skybox.getRGB((int) (u* skybox.getWidth())+ (u > .9? -1 : 0), (int) (v* skybox.getHeight())+ (v > .9? -1 : 0));
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
                if (color == null)
                    continue;
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
    }
}
