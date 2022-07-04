package fr.phoenix.engine;

import fr.phoenix.engine.object.render.Color;
import fr.phoenix.engine.vector.RayCast;
import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;

public class PixelPainter extends Thread{

    private final GraphicEngine ge;
    private final int ratioX;
    private final int ratioY;
    private final int sizeX;
    private final int sizeY;
    private final int fromX;
    private final int fromY;
    private final BufferedImage image;
    private final Graphics g;
    private final Graphics finalGraphic;

    public PixelPainter(Graphics g, GraphicEngine ge, int fromX, int fromY, int sizeX, int sizeY, int ratioX, int ratioY) {
        this.ge = ge;
        this.fromX = fromX;
        this.fromY = fromY;
        this.sizeX = sizeX;
        this.sizeY= sizeY;
        this.ratioX = ratioX;
        this.ratioY = ratioY;
        image = new BufferedImage(sizeX*ratioX, sizeY*ratioY, BufferedImage.TYPE_INT_RGB);
        this.g = image.getGraphics();
        this.finalGraphic = g;
    }

    @Override
    public void run() {
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                RayCast ray = ge.getPlayer().getCamera().getRay(fromX+i*ratioX, fromY+j * ratioY);
                Color color = ge.getColor(ray, 1);
                if (color == null)
                    continue;
                setPriority(1);
                g.setColor(color.getColor());
                g.fillRect(i * ratioX, j * ratioY, ratioX, ratioY);
            }
        }
        g.dispose();
        finalGraphic.drawImage(image, fromX, fromY, null);
    }
}