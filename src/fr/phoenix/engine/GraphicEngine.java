package fr.phoenix.engine;

import fr.phoenix.Display;
import fr.phoenix.engine.object.Object3D;
import fr.phoenix.engine.object.Player;
import fr.phoenix.engine.object.basics.Plan;
import fr.phoenix.engine.object.basics.Sphere;
import fr.phoenix.engine.object.basics.Triangle;
import fr.phoenix.engine.object.render.RenderableOject;
import fr.phoenix.engine.vector.RayCast;
import fr.phoenix.engine.vector.Vector2;
import fr.phoenix.engine.vector.Vector3;
import lombok.Getter;

import java.awt.*;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.List;

public class GraphicEngine{

    @Getter
    private final int width;
    @Getter
    private final int height;
    @Getter
    private final Display display;

    public GraphicEngine(Display display) {
        this.display = display;
        this.width = display.getWidth();
        this.height = display.getHeight();
        objects.add(new Sphere(1, new Vector3(7, 0, 6), Color.RED));
        objects.add(new Plan(new Vector3(0, -1,0), new Vector3(1, 0,0), new Vector3(0, 0,1), Color.CYAN));
        //objects.add(new Plan(new Vector3(0, 0,1), new Vector3(1, 0,0), new Vector3(0, 1,0), Color.BLUE));
    }

    private List<Object3D> objects = new ArrayList<>();
    private long lastTime = System.currentTimeMillis();
    private int fps = 0;
    private int frames = 0;
    @Getter
    private Player player = new Player();

    public void paint(Graphics graphics) {
        int resX = (int) player.getCamera().resolution.getX();
        int resY = (int) player.getCamera().resolution.getY();
        double ratioX = Display.getWIDTH() * 1.0 / resX;
        double ratioY = Display.getHEIGHT() * 1.0 / resY;
        BufferedImage image = new BufferedImage(resX, resY, BufferedImage.TYPE_INT_RGB);
        WritableRaster raster = image.getRaster();
        for (int i = 0; i < resX; i++) {
            for (int j = 0; j < resY; j++) {
                RayCast ray = player.getCamera().getRay((int) (i * ratioX), (int) (j * ratioY));
                float v = 1;
                Color color = null;
                for (Object3D obj : objects) {
                    if (!(obj instanceof RenderableOject))
                        continue;
                    RenderableOject ro = (RenderableOject) obj;
                    double luminosity = ro.raycast(ray);
                    if (luminosity != 1) {
                        v = (float) luminosity;
                        color = ro.color();
                        break;
                    }
                }
                if (color != null)
                    raster.setPixel(i,j,new float[]{v*color.getRed(),v*color.getGreen(),v*color.getBlue()});
            }
        }

        graphics.drawImage(image.getScaledInstance(Display.getWIDTH(), Display.getHEIGHT(), Image.SCALE_DEFAULT), 0, 20, null);
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
