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
        if (!obj.maxs.isFinite() || !obj.mins.isFinite()){
            boxes.add(obj);
            return;
        }
        for (int x = (int) obj.mins.getX(); x <= obj.maxs.getX(); x++)
            for (int y = (int) obj.mins.getY(); y <= obj.maxs.getY(); y++)
                for (int z = (int) obj.mins.getZ(); z <= obj.maxs.getZ(); z++)
                    getBox(new Vector3(x,y,z)).addObject(obj);

    }

    private BoundingBox getBox(Vector3 position) {
        int x = (int) position.getX();
        int y = (int) position.getY();
        int z = (int) position.getZ();
        for (Object3D box : boxes) {
            if (box instanceof BoundingBox) {
                Vector3 posBox = box.getPosition();
                int xBox = (int) posBox.getX();
                int yBox = (int) posBox.getY();
                int zBox = (int) posBox.getZ();
                if (xBox == x && yBox == y && zBox == z)
                    return (BoundingBox) box;
            }
        }
        BoundingBox newBox = new BoundingBox(new Vector3(x,y,z), 1);
        boxes.add(newBox);
        return newBox;
    }

    public void addObjects(ArrayList<Object3D> objs) {
        for (Object3D obj : objs)
            addObject(obj);
    }
}
