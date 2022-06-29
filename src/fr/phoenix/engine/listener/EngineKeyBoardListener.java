package fr.phoenix.engine.listener;

import fr.phoenix.engine.GraphicEngine;
import fr.phoenix.engine.object.Player;
import fr.phoenix.engine.vector.Vector2;
import fr.phoenix.engine.vector.Vector3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EngineKeyBoardListener extends AbstractAction {
    private final GraphicEngine engine;

    public EngineKeyBoardListener(GraphicEngine engine) {
        this.engine = engine;
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Vector3 forward = engine.getPlayer().getCamera().forward();
        forward.setY(0);
        forward = forward.normalize().times(Player.getMOVEMENT_SPEED());
        Vector3 right = forward.crossProduct(new Vector3(0, 1, 0)).normalize().times(Player.getMOVEMENT_SPEED());
        String ac = actionEvent.getActionCommand();
        System.out.println(ac);
        if (ac.equals("w"))
            engine.getPlayer().getPosition().move(forward);
        if (ac.equals("a"))
            engine.getPlayer().getPosition().move(right.times(-1));
        if (ac.equals("s"))
            engine.getPlayer().getPosition().move(forward.times(-1));
        if (ac.equals("d"))
            engine.getPlayer().getPosition().move(right);
    }
}
