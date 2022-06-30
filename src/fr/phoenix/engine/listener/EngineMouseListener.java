package fr.phoenix.engine.listener;

import fr.phoenix.Display;
import fr.phoenix.engine.GraphicEngine;
import fr.phoenix.engine.vector.Vector2;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class EngineMouseListener implements MouseListener {
    private final GraphicEngine engine;

    private Robot r;
    private Dimension screenSize;

    public EngineMouseListener(GraphicEngine engine) {
        this.engine = engine;
        try {
            r = new Robot();
            screenSize = engine.getDisplay().getToolkit().getScreenSize();
            r.mouseMove(screenSize.width/2, screenSize.height/2);
            last = MouseInfo.getPointerInfo().getLocation();
        } catch (AWTException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {

    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {

    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {

    }
    private Point last = MouseInfo.getPointerInfo().getLocation();
    public void update() {
        Point current = MouseInfo.getPointerInfo().getLocation();
        //last.y-current.y
        engine.getPlayer().getCamera().rotate(new Vector2(last.x-current.x, last.y-current.y).times(0.01));
        r.mouseMove(screenSize.width/2, screenSize.height/2);
    }
}
