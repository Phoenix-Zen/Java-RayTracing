package fr.phoenix.engine;

import fr.phoenix.Display;
import fr.phoenix.engine.object.Object3D;
import fr.phoenix.engine.object.Player;
import fr.phoenix.engine.object.basics.Sphere;
import fr.phoenix.engine.object.render.RenderableOject;
import fr.phoenix.engine.vector.RayCast;
import fr.phoenix.engine.vector.Vector2;
import fr.phoenix.engine.vector.Vector3;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class GraphicEngine{

    @Getter
    private final int width;
    @Getter
    private final int height;
    @Getter
    private final Display display;

    public GraphicEngine(Display display) throws HeadlessException {
        objects.add(new Sphere(1f, new Vector3(2,0,0)));
        this.display = display;
        this.width = display.getWidth();
        this.height = display.getHeight();
    }

    private List<Object3D> objects = new ArrayList<>();
    private long lastTime = System.currentTimeMillis();
    private int fps = 0;
    private int frames = 0;
    @Getter
    private Player player = new Player();

    public void paint(Graphics g) {
        for (Object3D object : objects) {
            if (object instanceof RenderableOject) {
                RenderableOject ro = (RenderableOject) object;
                ro.render(g, player.getCamera());
            }
        }

        /*
        g.clearRect(0,0, width, height);
        for (int i = 0; i < width; i++)
         for (int j = 25; j < height; j++){
            paintPixel(g, i,j);
         }
    }
    private void paintPixel(Graphics g, int i, int j) {
        Vector3 dir = player.getCamera().getDir(i,j);
        Vector3 origin = player.getPosition();
        RayCast rayCast = new RayCast(origin, dir);
        for (Object3D object : objects) {
            if (object instanceof RenderableOject){
                RenderableOject ro = (RenderableOject) object;
                if (ro.intersect(rayCast))
                    g.setColor(Color.RED);
                else
                    g.setColor(Color.WHITE);
                g.drawLine(i,j,i,j);
            }
        }*/
    }
    boolean reversed = false;
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
