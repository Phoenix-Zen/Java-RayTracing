package fr.phoenix.engine;

import fr.phoenix.engine.object.Object3D;
import fr.phoenix.engine.object.basics.BoundingBox;
import fr.phoenix.engine.object.basics.Triangle;
import fr.phoenix.engine.object.render.Color;
import fr.phoenix.engine.vector.Vector3;
import lombok.SneakyThrows;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

public class ObjLoader {

    private final File file;
    private ArrayList<Vector3> pts = new ArrayList<>();
    private ArrayList<BoundingBox> objs = new ArrayList<>();

    private final String path;
    public ObjLoader(String path) {
        this.path = path;
        this.file = new File(path);
    }

    public BoundingBox getObject(){
        for (BoundingBox obj : objs)
            obj.apply();
        return new BoundingBox(objs);
    }

    public void load(){
        loadPixels();
        loadFaces();
    }

    private final int limitFacePerBox = 20;

    @SneakyThrows
    private void loadFaces() {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        int couldnt = 0;
        int loaded = 0;


        BoundingBox[] boxIdForPoint = new BoundingBox[pts.size()];
        ArrayList<Object3D> faces;
        Color color = Color.WHITE;
        while (line != null){
            if (line.startsWith("usemtl ")){
                switch (line.split(" ")[1]){
                    case "Bark":
                        color = Color.BROWN;
                        break;
                    case "Tree":
                        color = Color.GREEN;
                }
            }
            if (line.startsWith("f ")){
                faces = new ArrayList<>();
                String[] pIds = line.split(" ");
                Vector3[] involvedPts = new Vector3[pIds.length-1];
                int[] involvedPtsId = new int[pIds.length-1];
                for (int i = 1; i < pIds.length; i++) {
                    int id = Integer.parseInt(pIds[i].split("/")[0]);
                    involvedPts[i - 1] = pts.get(id - 1);
                    involvedPtsId[i-1] = id-1;
                }

                if (involvedPts.length == 3)
                    faces.add(new Triangle(involvedPts[0], involvedPts[1], involvedPts[2], color));
                else if (involvedPts.length == 4) {
                    faces.add(new Triangle(involvedPts[0], involvedPts[1], involvedPts[2], color));
                    faces.add(new Triangle(involvedPts[0], involvedPts[2], involvedPts[3], color));
                }else
                    couldnt++;

                if (faces.size() > 0) {
                    //BOUNDING BOX
                    BoundingBox box = null;
                    for (int id : involvedPtsId) {
                        if (boxIdForPoint[id] != null && boxIdForPoint[id].insideItems() < limitFacePerBox)
                            box = boxIdForPoint[id];
                    }
                    if (box == null) {
                        box = new BoundingBox(faces);
                        objs.add(box);
                    } else {
                        for (Object3D face : faces)
                            box.addObject(face);
                    }
                    for (int id : involvedPtsId) {
                        if (boxIdForPoint[id] == null || boxIdForPoint[id].insideItems() >= limitFacePerBox)
                            boxIdForPoint[id] = box;
                    }
                }
                loaded+=faces.size();
            }
            line = reader.readLine();
        }
        System.out.println("LOADED "+ objs.size()+" box including "+loaded+" faces (couldnt load "+couldnt+")");

    }

    @SneakyThrows
    private void loadPixels() {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = reader.readLine();
        while (line != null){
            if (line.startsWith("v ")){
                String[] cords = line.split(" ");
                double x = Double.parseDouble(cords[1]);
                double y = Double.parseDouble(cords[2]);
                double z = Double.parseDouble(cords[3]);
                pts.add(new Vector3(x,y,z));
            }
            line = reader.readLine();
        }
        System.out.println("LOADED "+pts.size()+" points");
    }
}
