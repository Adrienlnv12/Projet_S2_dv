package fr.insa.leneve.projet_s2.interfa;



import fr.insa.leneve.projet_s2.structure.Barre;
import fr.insa.leneve.projet_s2.structure.Noeud.Noeud;
import fr.insa.leneve.projet_s2.structure.Terrain.Triangle;
import fr.insa.leneve.projet_s2.structure.Treillis;
import fr.insa.leneve.projet_s2.structure.TypedeBarre;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Save {

    public static Treillis getTreillis(String path) {
        try {
            BNFReader reader = new BNFReader(new FileReader(path));
            return reader.getTreillis();
        }catch (IOException ioException){
            return new Treillis();
        }
    }

    public static void saveTreillis(Treillis treillis, String savePath) throws IOException{
        StringBuilder fileString = new StringBuilder(treillis.getTerrain().saveString() + "\n");

        for (Triangle triangle: treillis.getTerrain().getTriangles()) {
            fileString.append(triangle.saveString()).append("\n");
        }
        fileString.append("FINTRIANGLES\n");

        for (TypedeBarre type: treillis.getCatalogue()) {
            fileString.append(type.saveString()).append("\n");
        }
        fileString.append("FINCATALOGUE\n");

        for (Noeud noeud: treillis.getNoeuds()) {
            fileString.append(noeud.saveString()).append("\n");
        }
        fileString.append("FINNOEUDS\n");

        for (Barre barre: treillis.getBarres()) {
            fileString.append(barre.saveString()).append("\n");
        }
        fileString.append("FINBARRES\n");

        try {
            FileWriter writer = new FileWriter(savePath);
            writer.write(fileString.toString());
            writer.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

}
