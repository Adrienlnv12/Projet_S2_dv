/*
Copyright 2000- Francois de Bertrand de Beuvron

This file is part of CoursBeuvron.

CoursBeuvron is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

CoursBeuvron is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with CoursBeuvron.  If not, see <http://www.gnu.org/licenses/>.
 */
package fr.insa.leneve.projet_s2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import fr.insa.leneve.projet_s2.recup.Lire;

/**
 *
 * @author francois
 */
public class Groupe extends Figure {

    private List<Figure> contient;

    public List<Figure> getContient() {
        return contient;
    }

    public Groupe() {
        this.contient = new ArrayList<Figure>();
    }

    public void add(Figure f) {
        if (f.getGroupe() != this) {
            if (f.getGroupe() != null) {
                throw new Error("figure déja dans un autre groupe");
            }
            this.contient.add(f);
            f.setGroupe(this);
        }
    }

    public void remove(Figure f) {
        if (f.getGroupe() != this) {
            throw new Error("la figure n'est pas dans le groupe");
        }
        this.contient.remove(f);
        f.setGroupe(null);
    }

    public void removeAll(List<Figure> lf) {
        for (Figure f : lf) {
            this.remove(f);
        }
    }

    public void clear() {
        List<Figure> toRemove = new ArrayList<>(this.contient);
        this.removeAll(toRemove);
    }

    public int size() {
        return this.contient.size();
    }

    /**
     * retourne la figure contenue dans le groupe la plus proche du point et au
     * maximum à distMax du point; retourne null si aucune figure n'est à une
     * distance plus faible que distMax;
     * @param p
     */
    public Figure plusProche(Noeud p, double distMax) {
        if (this.contient.isEmpty()) {
            return null;
        } else {
            Figure fmin = this.contient.get(0);
            double min = fmin.distanceNoeud(p);
            for (int i = 1; i < this.contient.size(); i++) {
                Figure fcur = this.contient.get(i);
                double cur = fcur.distanceNoeud(p);
                if (cur < min) {
                    min = cur;
                    fmin = fcur;
                }
            }
            if (min <= distMax) {
                return fmin;
            } else {
                return null;
            }
        }
    }

    /**
     * crée un sous groupe avec les figures contenues dans lf. . les figures de
     * lf doivent appartenir au groupe this. . un nouveau groupe sg est créé .
     * les figures de lf sont retirées de this . les figures de lf sont ajoutées
     * au nouveau groupe sg . le groupe sg est ajouté au groupe this
     *
     * @return le sous-groupe créé.
     */
    public Groupe sousGroupe(List<Figure> lf) {
        // verifie que les figures font actuellement partie du groupe
        // et les enleve du groupe
        for (int i = 0; i < lf.size(); i++) {
            Figure f = lf.get(i);
            if (f.getGroupe() != this) {
                throw new Error(f + " n'appartient pas au groupe " + this);
            }
            this.contient.remove(f);
            f.setGroupe(null);
        }
        Groupe sg = new Groupe();
        for (int i = 0; i < lf.size(); i++) {
            sg.add(lf.get(i));
        }
        this.add(sg);
        return sg;
    }

    public static String indente(String toIndente, String prefix) {
        return prefix + toIndente.replaceAll("\n", "\n" + prefix);
    }

    @Override
    public String toString() {
        String res = "Groupe {\n";
        for (int i = 0; i < this.contient.size(); i++) {
            res = res + indente(this.contient.get(i).toString(), "  ") + "\n";
        }
        return res + "}";
    }

    public static Groupe groupeTest() {
        Noeud p1 = new Noeud(10, 10);
        Noeud p2 = new Noeud(100, 10);
        Noeud p3 = new Noeud(100, 100);
        Noeud p4 = new Noeud(10, 100);
        Noeud p5 = new Noeud(50, 50);
        Noeud p6 = new Noeud(500, 500, Color.AQUAMARINE);
        Barre s1 = new Barre(p1, p2);
        Barre s2 = new Barre(p2, p3);
        Barre s3 = new Barre(p3, p1);
        Barre s4 = new Barre(p1, p4);
        Groupe triangle = new Groupe();
        triangle.add(s1);
        triangle.add(s2);
        triangle.add(s3);
        Groupe res = new Groupe();
        res.add(p5);
        res.add(p6);
        res.add(s4);
        res.add(triangle);
        for (int i = 0; i < 10; i++) {
            res.add(new Noeud(Math.random() * 500, Math.random() * 500,
                    Color.color(Math.random(), Math.random(), Math.random())));
        }
        for (int i = 0; i < 5; i++) {
            res.add(new Barre(new Noeud(Math.random() * 500, Math.random() * 500),
                    new Noeud(Math.random() * 500, Math.random() * 500),
                    Color.color(Math.random(), Math.random(), Math.random())));
        }
        return res;
    }

    public Noeud choisiNoeud() {
        List<Noeud> lp = new ArrayList<>();
        System.out.println("liste des points disponibles : ");
        int nbr = 0;
        for (int i = 0; i < this.contient.size(); i++) {
            Figure f = this.contient.get(i);
            if (f instanceof Noeud) {
                nbr++;
                lp.add((Noeud) f);
                System.out.println(nbr + ") " + f);
            }
        }
        if (nbr == 0) {
            System.out.println("Aucun point disponible");
            return null;
        } else {
            int rep = -1;
            while (rep < 0 || rep > nbr) {
                System.out.println("votre choix (0 pour annuler) : ");
                rep = Lire.i();
            }
            if (rep == 0) {
                return null;
            } else {
                return lp.get(rep - 1);
            }
        }
    }

    public List<Figure> choisiFigures() {
        List<Figure> res = new ArrayList<>();
        int rep = -1;
        while (rep != 0) {
            System.out.println("liste des figures disponibles : ");
            for (int i = 0; i < this.contient.size(); i++) {
                System.out.println((i + 1) + ") " + this.contient.get(i));
            }
            System.out.println("votre choix (0 pour finir) : ");
            rep = Lire.i();
            if (rep > 0 && rep <= this.contient.size()) {
                Figure f = this.contient.get(rep - 1);
                if (res.contains(f)) {
                    System.out.println("déja selectionnée !!");
                } else {
                    res.add(f);
                }
                System.out.println(res.size() + " figure(s) séléctionnée(s)");
            }
        }
        return res;
    }

    public void menuTexte() {
        int rep = -1;
        while (rep != 0) {
            System.out.println("Gestion textuelle d'un groupe de figure");
            System.out.println("---------------------------------------");
            System.out.println("1) afficher le groupe");
            System.out.println("2) ajouter un point");
            System.out.println("3) ajouter un segment avec deux nouveaux points");
            System.out.println("4) ajouter un segment sur deux points existants");
            System.out.println("5) créer un sous-groupe");
            System.out.println("6) afficher le rectangle englobant");
            System.out.println("7) calculer la distance à un point");
            System.out.println("8) retirer des figures du groupe");
            System.out.println("9) sauvegarder le groupe dans un fichier");
            System.out.println("0) quitter");
            System.out.println("votre choix : ");
            rep = Lire.i();
            if (rep == 1) {
                System.out.println(this);
            } else if (rep == 2) {
                Noeud np = Noeud.demandeNoeud();
                this.add(np);
            } else if (rep == 3) {
                Barre ns = Barre.demandeBarre();
                this.add(ns);
            } else if (rep == 4) {
                System.out.println("choisissez le début du segment");
                Noeud deb = this.choisiNoeud();
                if (deb != null) {
                    System.out.println("choisissez la fin du segment");
                    Noeud fin = this.choisiNoeud();
                    Barre ns = new Barre(deb, fin);
                    this.add(ns);
                }
            } else if (rep == 5) {
                List<Figure> select = this.choisiFigures();
                this.sousGroupe(select);
            } else if (rep == 6) {
                System.out.println("maxX = " + this.maxX() + " ; "
                        + "minX = " + this.minX() + "\n"
                        + "maxY = " + this.maxY() + " ; "
                        + "minY = " + this.minY() + "\n");
            } else if (rep == 7) {
                System.out.println("entrez un point :");
                Noeud p = Noeud.demandeNoeud();
                System.out.println("distance : " + this.distanceNoeud(p));
            } else if (rep == 8) {
                List<Figure> select = this.choisiFigures();
                this.removeAll(select);
            } else if (rep == 9) {
                System.out.println("répertoire courant : ");
                System.out.println(System.getProperty("user.dir"));
                System.out.println("entrez le chemin du fichier où sauvegarder le groupe de figure : ");
                String path = Lire.S();
                File fout = new File(path);
                try {
                    this.sauvegarde(fout);
                } catch (FileNotFoundException ex) {
                    System.out.println("fichier non trouvé : " + fout.getAbsolutePath());;
                } catch (IOException ex) {
                    System.out.println("problème de lecture : " + ex.getLocalizedMessage());;
                }

            }
        }
    }

    public static void test1() {
        System.out.println("groupe test : \n" + Groupe.groupeTest());
    }

    public static void testMenu() {
//        Groupe g = groupeTest();
        Groupe g = new Groupe();
        g.menuTexte();
    }

    public static void exempleProblemeSauvegarde() {
        Noeud p11 = new Noeud(1, 1);
        Noeud p12 = new Noeud(2, 2);
        Noeud p13 = new Noeud(2, 2);
        Noeud p14 = new Noeud(3, 3);
        Barre s11 = new Barre(p11, p12);
        Barre s12 = new Barre(p13, p14);
        Groupe gr1 = new Groupe();
        gr1.add(s11);
        gr1.add(s12);
        Noeud p21 = new Noeud(1, 1);
        Noeud p22 = new Noeud(2, 2);
        Noeud p24 = new Noeud(3, 3);
        Barre s21 = new Barre(p21, p22);
        Barre s22 = new Barre(p22, p24);
        Groupe gr2 = new Groupe();
        gr2.add(s21);
        gr2.add(s22);
        gr2.add(gr1);
        System.out.println("Groupe 1 : " + gr1);
        System.out.println("Groupe 2 : " + gr2);
        try {
            gr1.sauvegarde(new File("groupe1.txt"));
            gr2.sauvegarde(new File("groupe2.txt"));
        } catch (IOException ex) {
            throw new Error("probleme : " + ex.getMessage());
        }
    }

    public static void testLecture() {
        try {
            Figure lue = Figure.lecture(new File("groupe2.txt"));
            System.out.println("fig lue : " + lue);
        } catch (IOException ex) {
            throw new Error(ex);
        }
    }

//    public static void main(String[] args) {
////        test1();
//        testMenu();
////        exempleProblemeSauvegarde();
////        testLecture();
//    }
//
    /**
     * abscice maximale d'un groupe de figures. 0 si le groupe est vide.
     */
    @Override
    public double maxX() {
        if (this.contient.isEmpty()) {
            return 0;
        } else {
            double max = this.contient.get(0).maxX();
            for (int i = 1; i < this.contient.size(); i++) {
                double cur = this.contient.get(i).maxX();
                if (cur > max) {
                    max = cur;
                }
            }
            return max;
        }
    }

    /**
     * abscice minimale d'un groupe de figures. 0 si le groupe est vide.
     */
    @Override
    public double minX() {
        if (this.contient.isEmpty()) {
            return 0;
        } else {
            double min = this.contient.get(0).minX();
            for (int i = 1; i < this.contient.size(); i++) {
                double cur = this.contient.get(i).minX();
                if (cur < min) {
                    min = cur;
                }
            }
            return min;
        }
    }

    /**
     * ordonnee maximale d'un groupe de figures. 0 si le groupe est vide.
     */
    @Override
    public double maxY() {
        if (this.contient.isEmpty()) {
            return 0;
        } else {
            double max = this.contient.get(0).maxY();
            for (int i = 1; i < this.contient.size(); i++) {
                double cur = this.contient.get(i).maxY();
                if (cur > max) {
                    max = cur;
                }
            }
            return max;
        }
    }

    /**
     * ordonnee minimale d'un groupe de figures. 0 si le groupe est vide.
     */
    @Override
    public double minY() {
        if (this.contient.isEmpty()) {
            return 0;
        } else {
            double min = this.contient.get(0).minY();
            for (int i = 1; i < this.contient.size(); i++) {
                double cur = this.contient.get(i).minY();
                if (cur < min) {
                    min = cur;
                }
            }
            return min;
        }
    }
    
    @Override
    public double distanceNoeud(Noeud p) {
        if (this.contient.isEmpty()) {
            return new Noeud(0, 0).distanceNoeud(p);
        } else {
            double dist = this.contient.get(0).distanceNoeud(p);
            for (int i = 1; i < this.contient.size(); i++) {
                double cur = this.contient.get(i).distanceNoeud(p);
                if (cur < dist) {
                    dist = cur;
                }
            }
            return dist;
        }
    }

    @Override
    public void dessine(GraphicsContext context) {
        for (Figure f : this.contient) {
            f.dessine(context);
        }
    }

    @Override
    public void dessineSelection(GraphicsContext context) {
        for (Figure f : this.contient) {
            f.dessineSelection(context);
        }
    }

    @Override
    public void changeCouleur(Color value) {
        for (Figure f : this.contient) {
            f.changeCouleur(value);
        }
    }

    @Override
    public void save(Writer w, Numeroteur<Figure> num) throws IOException {
        if (!num.objExist(this)) {
            int id = num.creeID(this);
            for (Figure f : this.contient) {
                f.save(w, num);
            }
            w.append("Groupe;" + id);
            for (Figure f : this.contient) {
                w.append(";" + num.getID(f));
            }
            w.append("\n");
        }
    }

}
