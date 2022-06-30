package fr.phoenix.engine;

import fr.phoenix.Display;
import fr.phoenix.engine.object.Object3D;
import fr.phoenix.engine.object.Player;
import fr.phoenix.engine.object.basics.Plan;
import fr.phoenix.engine.object.basics.Sphere;
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
    private double lighting = 2f;
    @Getter
    private static float ambientLight = .05f;

    private BufferedImage skybox;

    public GraphicEngine(Display display) {
        try {
            skybox = ImageIO.read(new File("/home/flo/IdeaProjects/SimpleGame/assets/SkyBox2.png"));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

        this.display = display;
        this.width = display.getWidth();
        this.height = display.getHeight();

        this.light = new Vector3(2, 4, 3);

        objects.add(new Sphere(1, new Vector3(4, 0, 0), Color.WHITE));
        objects.add(new Plan(-1, Color.DARK_GRAY));
        //objects.add(new Plan(new Vector3(0, 0,1), new Vector3(1, 0,0), new Vector3(0, 1,0), Color.BLUE));
    }

    private List<Object3D> objects = new ArrayList<>();
    private long lastTime = System.currentTimeMillis();
    private int fps = 0;
    private int frames = 0;
    @Getter
    private Player player = new Player();

    public boolean rayCast(RayCast ray){
        for (Object3D obj : objects) {
            if (!(obj instanceof RenderableOject))
                continue;
            RenderableOject ro = (RenderableOject) obj;
            Vector3 hit = new Vector3(0,0,0);
            if (ro.raycast(ray)) {
                ray.setObject3D(obj);
                return true;
            }
        }
        return false;
    }

    public Color getColor(RayCast ray){
        if(rayCast(ray)) {
            float v = Math.min(Math.max(ambientLight, (float) (lighting*light.sub(ray.getHit()).normalize().dotProduct(ray.getNormal().normalize()))), 1);
            RayCast rayLight = new RayCast(ray.getHit(), light.sub(ray.getHit()).normalize());
            if (rayCast(rayLight))
                v = ambientLight;
            RenderableOject ro = (RenderableOject) ray.getObject3D();
            return ro.color().multiply(v);
        }else {
            Vector3 dir = ray.getDirection().normalize();
            double u = 0.5+Math.atan2(dir.getX(), dir.getZ())/(2*Math.PI);
            double v = 0.5-Math.asin(dir.getY())/Math.PI;
            int rgb = skybox.getRGB((int) (u*skybox.getWidth()), (int) (v*skybox.getHeight()));
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
                Color color = getColor(ray);
                if (ray.getReflection() != 0){
                    Vector3 reflect = ray.getDirection().sub(ray.getNormal().times(2*ray.getDirection().dotProduct(ray.getNormal())));
                    RayCast rayReflection = new RayCast(ray.getHit(), reflect);
                    rayCast(rayReflection);
                    color = color.mix(getColor(rayReflection), (float) ray.getReflection());
                }
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
