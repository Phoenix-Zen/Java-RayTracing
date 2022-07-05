package fr.phoenix.engine.listener;

import fr.phoenix.engine.GraphicEngine;
import fr.phoenix.engine.vector.Vector2;
import fr.phoenix.engine.vector.Vector3;
import lombok.Getter;

import java.awt.event.*;

public class EngineKeyBoardListener extends KeyAdapter {

    @Getter
    private Vector3 cameraMotion = new Vector3(0,0,0);
    private final GraphicEngine engine;

    public EngineKeyBoardListener(GraphicEngine engine) {this.engine = engine;}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D) {
            cameraMotion.setX(0.2F);
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            cameraMotion.setX(-0.2F);
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            cameraMotion.setZ(0.2F);
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            cameraMotion.setZ(-0.2F);
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            cameraMotion.setY(0.2F);
        } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            cameraMotion.setY(-0.2F);
        } else if (e.getKeyCode() == KeyEvent.VK_1) {
            engine.getPlayer().getCamera().resolution = new Vector2(150,150);
        } else if (e.getKeyCode() == KeyEvent.VK_2) {
            engine.getPlayer().getCamera().resolution = new Vector2(300,300);
        } else if (e.getKeyCode() == KeyEvent.VK_3) {
            engine.getPlayer().getCamera().resolution = new Vector2(600,600);
        } else if (e.getKeyCode() == KeyEvent.VK_4) {
            engine.getPlayer().getCamera().resolution = new Vector2(1200,1200);
        } else if (e.getKeyCode() == KeyEvent.VK_5) {
            GraphicEngine.smooth = !GraphicEngine.smooth;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_D) {
            cameraMotion.setX(0);
        } else if (e.getKeyCode() == KeyEvent.VK_A) {
            cameraMotion.setX(0);
        } else if (e.getKeyCode() == KeyEvent.VK_W) {
            cameraMotion.setZ(0);
        } else if (e.getKeyCode() == KeyEvent.VK_S) {
            cameraMotion.setZ(0);
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            cameraMotion.setY(0);
        } else if (e.getKeyCode() == KeyEvent.VK_SHIFT) {
            cameraMotion.setY(0);
        }
    }

    private float speed = .4f;
    public void update() {
        Vector3 forward = engine.getPlayer().getCamera().forward();
        forward.setY(0);
        forward = forward.normalize();
        Vector3 right = forward.crossProduct(new Vector3(0,1,0)).normalize();

        engine.getPlayer().move(forward.times(cameraMotion.getZ()).add(new Vector3(0,1,0).times(cameraMotion.getY())).add(right.times(cameraMotion.getX())));
    }
}
