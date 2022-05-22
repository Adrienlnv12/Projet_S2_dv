package fr.insa.leneve.projet_s2.interfa;



import fr.insa.leneve.projet_s2.structure.Barre;
import fr.insa.leneve.projet_s2.structure.Noeud.Noeud;
import fr.insa.leneve.projet_s2.structure.Terrain.Triangle;
import fr.insa.leneve.projet_s2.structure.forme.Treillis;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Save {
/*
    public static Treillis getTreillis(String path) throws IOException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(path));
            return reader.getTreillis();
        }catch (IOException ioException){
            return new Treillis();
        }
    }

    public static void saveTreillis(Treillis treillis, File fout) throws IOException {
        try (BufferedWriter bout = new BufferedWriter(new FileWriter(fout))) {

        for (Triangle triangle: treillis.getTriangles()) {
            bout.append(triangle.saveString()).append("\n");
        }
        bout.append("FINTRIANGLES\n");

        
        for (Noeud noeud: treillis.getNoeuds()) {
            bout.append(noeud.saveString()).append("\n");
        }
        bout.append("FINNOEUDS\n");

        for (Barre barre: treillis.getBarres()) {
            bout.append(barre.saveString()).append("\n");
        }
        bout.append("FINBARRES\n");

        try {
            FileWriter writer = new FileWriter(savePath);
            writer.write(bout.toString());
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }
    }
*/
}
