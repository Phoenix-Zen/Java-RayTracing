package fr.phoenix.engine.object;

import fr.phoenix.engine.object.basics.BoundingBox;
import fr.phoenix.engine.object.render.Color;
import fr.phoenix.engine.object.render.RenderableOject;
import fr.phoenix.engine.vector.RayCast;
import fr.phoenix.engine.vector.Vector3;
import lombok.Getter;
import lombok.Setter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Scene {

    @Getter
    private BufferedImage skybox;
    @Getter
    private ArrayList<Object3D> boxes = new ArrayList<>();
    @Getter
    private ArrayList<Object3D> objects = new ArrayList<>();
    @Getter
    @Setter
    private Vector3 light;
    @Getter
    private final double lighting = 4f;
    @Getter
    private final float ambientLight = .2f;

    public Scene() {
        try {
            skybox = ImageIO.read(new File("/home/flo/IdeaProjects/SimpleGame/assets/Nebula.png"));
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }

    }

    public void addObject(Object3D obj){
        objects.add(obj);
    }

}
