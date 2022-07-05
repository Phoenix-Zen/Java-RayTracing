package fr.phoenix.engine;

import fr.phoenix.engine.object.render.Color;
import fr.phoenix.engine.vector.RayCast;
import lombok.Getter;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Arrays;

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
        boolean smooth = GraphicEngine.smooth;
        RayCast[][] rays = new RayCast[sizeX][sizeY];
        int[][] precise = new int[sizeX][sizeY];
        for (int i = 0; i < sizeX; i++) {
            for (int j = 0; j < sizeY; j++) {
                if (precise[i][j] != 0)
                    continue;
                RayCast ray = ge.getPlayer().getCamera().getRay(fromX+i*ratioX, fromY+j * ratioY);
                Color color = ge.getColor(ray, 1);
                if (color == null)
                    continue;
                rays[i][j] = ray;
                g.setColor(color.getColor());
                g.fillRect(i * ratioX, j * ratioY, ratioX, ratioY);

                if (smooth){
                    if (i != 0 && rays[i-1][j].getObject3D() != ray.getObject3D()){
                        precise[i-1][j] = 1;
                        precise[i][j] = 1;
                        if (j != 0 && rays[i][j-1].getObject3D() != ray.getObject3D()){
                            precise[i][j-1] = 1;
                            precise[i-1][j-1] = 1;
                        }
                    }
                    else if (j != 0 && rays[i][j-1].getObject3D() != ray.getObject3D()){
                        precise[i][j-1] = 1;
                        precise[i][j] = 1;
                    }
                }
            }
        }
        if (smooth){
            int ratioXHalf = ratioX / 2;
            int ratioYHalf = ratioY / 2;
            for (int i = 0; i < precise.length; i++) {
                for (int j = 0; j < precise[i].length; j++) {
                    if (precise[i][j] == 0)
                        continue;
                    for (int id = 0; id < ratioXHalf; id++) {
                        for (int jd = 0; jd < ratioYHalf; jd++) {
                            RayCast ray = ge.getPlayer().getCamera().getRay(fromX+i*ratioX+id*ratioXHalf, fromY+j * ratioY+jd*ratioYHalf);
                            Color color = ge.getColor(ray, 1);
                            if (color == null || ray.getObject3D() == null)
                                continue;
                            g.setColor(color.getColor());
                            g.fillRect(i * ratioX+id*ratioXHalf, j * ratioY+jd*ratioYHalf, ratioXHalf, ratioYHalf);
                        }
                    }
                }
            }
        }
        g.dispose();
        finalGraphic.drawImage(image, fromX, fromY, null);
    }
}