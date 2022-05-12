package fr.insa.leneve.projet_s2.interfa;

import fr.insa.leneve.projet_s2.Numerateur;
import fr.insa.leneve.projet_s2.structure.*;
import fr.insa.leneve.projet_s2.structure.Noeud.*;
import fr.insa.leneve.projet_s2.structure.Terrain.PointTerrain;
import fr.insa.leneve.projet_s2.structure.Terrain.Terrain;
import fr.insa.leneve.projet_s2.structure.Terrain.Triangle;
import fr.insa.leneve.projet_s2.structure.Treillis;
import fr.insa.leneve.projet_s2.structure.TypedeBarre;
import fr.insa.leneve.projet_s2.structure.forme.Point;
import javafx.scene.paint.Color;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;

public class BNFReader extends BufferedReader {

    private Terrain terrain;
    private final HashMap<Integer, TypedeBarre> catalogue = new HashMap<>();
    private final HashMap<Integer, Noeud> noeuds = new HashMap<>();
    private final ArrayList<Barre> barres = new ArrayList<>();
    private Numerateur numerateur;

    private int catalogueId = 0, noeudId = 0, barreId = 0, triangleId = 0;

    private ArrayList<HashMap<String, String>> file = new ArrayList<>();

    public BNFReader(Reader in) {
        super(in);
        try {
            initFile();
        }catch (IOException | InvocationTargetException ioException){
            ioException.printStackTrace();
        }
    }

    private void initFile() throws IOException, InvocationTargetException {
        String line;
        while ((line = this.readLine()) != null){
            String[] lineArr = line.split(";");
            switch (lineArr[0]){
                case "ZoneConstructible" -> terrain = new Terrain(Double.parseDouble(lineArr[1]),
                       Double.parseDouble(lineArr[2]), Double.parseDouble(lineArr[3]), Double.parseDouble(lineArr[4]));
                case "Triangle" -> addTriangle(lineArr);
                case "TypeBarre" -> addTypeBarre(lineArr);
                case "AppuiDouble", "AppuiSimple", "NoeudSimple", "AppuiEncastre" -> addNoeud(lineArr);
                case "Barre" -> addBarre(lineArr);
            }
        }
        numerateur = new Numerateur(noeudId, barreId, catalogueId, triangleId);
    }

    private void addTriangle(String[] triangles){
        triangleId = Integer.parseInt(triangles[1]);
        PointTerrain pt1 = new PointTerrain(toPoint(triangles[2]));
        PointTerrain pt2 = new PointTerrain(toPoint(triangles[3]));
        PointTerrain pt3 = new PointTerrain(toPoint(triangles[4]));

        for (PointTerrain p : terrain.getPoints()) {
            if(p.EstPoint(pt1)) pt1 = p;
            if(p.EstPoint(pt2)) pt2 = p;
            if(p.EstPoint(pt3)) pt3 = p;
        }
        terrain.addPoint(pt1);
        terrain.addPoint(pt2);
        terrain.addPoint(pt3);

        Triangle triangle = new Triangle(pt1, pt2, pt3, triangleId, terrain);

        terrain.addTriangle(triangle);
    }

    private Point toPoint(String point){
        point = point.replace("(", "");
        point = point.replace(")", "");
        String[] values = point.split(",");
        return new Point(Double.parseDouble(values[0]), Double.parseDouble(values[1]));
    }

    private void addTypeBarre(String[] type){
        int id = Integer.parseInt(type[1]);
        catalogue.put(id, new TypedeBarre(type[7], Double.parseDouble(type[2]), Double.parseDouble(type[3]),
                Double.parseDouble(type[4]), Double.parseDouble(type[5]),
                Double.parseDouble(type[6]), id, Color.web(type[8])));
        catalogueId ++;
    }

    private void addNoeud(String[] strNoeud){
        Noeud noeud;
        noeudId = Integer.parseInt(strNoeud[1]);
        switch (strNoeud[0]){
            case "AppuiDouble" -> {
                Triangle triangle = terrain.getTriangle(Integer.parseInt(strNoeud[2]));
                if(triangle != null) noeud = new NoeudAppuiDouble(triangle, triangle.getSegments()[Integer.parseInt(strNoeud[3])], Double.parseDouble(strNoeud[4]), noeudId);
                else noeud = null;
            }
            case "AppuiSimple" -> {
                Triangle triangle = terrain.getTriangle(Integer.parseInt(strNoeud[2]));
                if(triangle != null) noeud = new NoeudAppuiSimple(triangle, triangle.getSegments()[Integer.parseInt(strNoeud[3])], Double.parseDouble(strNoeud[4]), noeudId);
                else noeud = null;
            }
            case "NoeudSimple" -> noeud = new NoeudSimple(toPoint(strNoeud[2]), noeudId);
            default -> noeud = new NoeudSimple(0,0,0);
        }
        if(noeud != null) noeuds.put(noeud.getId(), noeud);
    }

    public void addBarre(String[] strBarre){
        barreId = Integer.parseInt(strBarre[1]);
        Noeud pA = noeuds.get(Integer.parseInt(strBarre[3]));
        Noeud pB = noeuds.get(Integer.parseInt(strBarre[4]));
        Barre barre = new Barre(pA, pB, catalogue.get(Integer.parseInt(strBarre[2])), barreId);

        pA.addBarres(barre);
        pB.addBarres(barre);

        barres.add(barre);


    }

    public Treillis getTreillis(){
        return new Treillis(terrain, new ArrayList<>(noeuds.values()), barres, new ArrayList<>(catalogue.values()), numerateur);
    }

}
