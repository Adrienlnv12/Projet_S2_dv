/*
Copyright 2000-2014 Francois de Bertrand de Beuvron

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



import Ressyslin.DessinTexte;
import Ressyslin.ListeDessinsTexte;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Optional;

/**
 * Représentation de matrice rectangulaire. TD module M2 informatique INSA
 * Strasbourg. Les parties en commentaire font référence au sujet de TD
 *
 * @author francois
 */
public class Matrice {

    //--------------- partie 1.1
    /**
     * nombre de ligne de la matrice. Cet attribut n'est pas indispensable ici
     * puisque l'on représente les matrices par des tableau à deux dimension de
     * double. On pourrait dont obtenir le nombre de lignes par :
     * {@code this.coeffs.length} On décide tout de même de conserver
     * explicitement le nombre de lignes pour voir l'utilisation d'attributs
     * simples.
     */
    private int nbrLig;

    /**
     * nombre de colonnes de la matrice. Voir commentaire sur {@link #nbrLig}
     */
    private int nbrCol;

    /**
     * les coefficients de la matrice.
     */
    private double[][] coeffs;

    /**
     * le pivôt est considéré nul s'il est inférieur ou égal à EPSILON_PIVOT
     */
    private static final double EPSILON_PIVOT = 1E-8;

    //--------------- partie 1.2
    /**
     * Cree la matrice nulle de taille nbrLig x nbrCol.
     *
     * @param nbrLig nombre de ligne
     * @param nbrCol nombre de colonnes
     */
    public Matrice(int nbrLig, int nbrCol) {
        this.nbrLig = nbrLig;
        this.nbrCol = nbrCol;
        this.coeffs = new double[nbrLig][nbrCol];
        // il est inutile en java d'initialiser explicitement les coeffs à
        // zéro : java initialise automatiquement à zéro les tableaux
        // numériques.
        // !!! ce ne serait pas forcément le cas dans d'autres langages (ex C)
    }

    /**
     * un autre constructeur possible, non demandé dans le sujet. on fourni
     * explicitement tous les coeffs sous forme de tableau on suppose que le
     * tableau de tableau fourni est effectivement rectangulaire. Attention : il
     * n'y a pas copie : le tableau fourni devient le tableau des coefficients
     * de la matrice. Comme tout cela est un peu "dangereux" on va en faire un
     * constructeur privé
     */
    private Matrice(double[][] coeffs) {
        this.nbrLig = coeffs.length;
        this.nbrCol = coeffs[0].length;
        this.coeffs = coeffs;
    }

    //--------------- partie 1.3
    /**
     * matrice identité.
     *
     * @param taille
     * @return la matrice identité (carrée) taille x taille
     */
    public static Matrice identite(int taille) {
        Matrice res = new Matrice(taille, taille);
        for (int i = 0; i < taille; i++) {
            res.coeffs[i][i] = 1;
        }
        return res;
    }

    public static Matrice matTest1() {
        Matrice res = new Matrice(3, 3);
        int cur = 0;
        for (int i = 0; i < res.nbrLig; i++) {
            for (int j = 0; j < res.nbrCol; j++) {
                res.coeffs[i][j] = cur;
                cur++;
            }
        }
        return res;
    }

    public static Matrice matTest2() {
        Matrice res = Matrice.matTest1();
        res.coeffs[1][1] = -res.coeffs[1][1];
        res.coeffs[2][2] = -res.coeffs[2][2];
        return res;
    }

    /**
     * renvoie aléatoirement 1 ou 2 avec la proba 1/2 1/2.
     *
     * @return aléatoirement 1 ou 2.
     */
    public static int aleaUnOuDeux() {
        if (Math.random() < 0.5) {
            return 1;
        } else {
            return 2;
        }
    }

    /**
     * crée une matrice de taille nbrLig x nbrCol composée aléatoirement
     * uniquement de -1,0 ou 1. La probabilité qu'un coeff soit nul est
     * proportionDeZero. Ensuite, il y a probabilité égale, donc
     * (1-proportionDeZero)/2 d'avoir un -1 ou un 1.
     *
     * @param nbrLig
     * @param nbrCol
     * @param proportionDeZero proba qu'un coeff soit nul
     * @return la nouvelle matrice
     */
    public static Matrice matAleaZeroUnDeux(int nbrLig, int nbrCol, double proportionDeZero) {
        Matrice res = new Matrice(nbrLig, nbrCol);
        for (int i = 0; i < nbrLig; i++) {
            for (int j = 0; j < nbrCol; j++) {
                if (Math.random() >= proportionDeZero) {
                    res.coeffs[i][j] = aleaUnOuDeux();
                }
            }
        }
        return res;
    }

    public static Matrice creeVecteur(double[] composantes) {
        Matrice res = new Matrice(composantes.length, 1);
        for (int i = 0; i < composantes.length; i++) {
            res.coeffs[i][0] = composantes[i];
        }
        return res;
    }

    //--------------- partie 1.4
    public int getNbrLig() {
        return this.nbrLig;
    }

    public int getNbrCol() {
        return this.nbrCol;
    }

    public double get(int lig, int col) {
        return this.coeffs[lig][col];
    }

    public void set(int lig, int col, double val) {
        this.coeffs[lig][col] = val;
    }

    //--------------- non demandé : c'est simplement pour avoir éventuellement
    //  un affichage plus simple dans le sujet
    public static String formatDouble(double x) {
//        return formatDouble2Digits(x);
//        return formatDoubleMax2Decimales(x);
        return formatDoubleFixe(x);
    }

    public static String formatDoubleMax2Decimales(double x) {
        DecimalFormat f = new DecimalFormat("#.##");
        return f.format(x);
    }

    public static String formatDouble2Digits(double x) {
        return String.format("%+3.1f", x);
    }

    public static String formatDoubleFixe(double x) {
        return String.format("%+4.2E", x);
    }

    //--------------- partie 1.5
    @Override
    public String toString() {
        // oui, il serait plus efficace d'utiliser un {@link java.lang.StringBuilder}
        // mais ils n'ont pas été vu
        String res = "";
        for (int i = 0; i < nbrLig; i++) {
            res = res + "[";
            for (int j = 0; j < nbrCol; j++) {
                res = res + formatDouble(this.get(i, j));
                if (j < nbrCol - 1) {
                    res = res + " ";
                }
            }
            res = res + "]\n";
        }
        return res;
    }

    //--------------- partie 1.6
    public static void test1() {
        System.out.println("----------- test 1 --------------");
        int nl = 4, nc = 6;
        double pz = 0.3;
        System.out.println("matrice alea de taille " + nl + " x " + nc
                + " (proba de zero : " + pz + ") : ");
        System.out.println(Matrice.matAleaZeroUnDeux(nl, nc, pz));
    }

    //--------------- partie 2.1
    public Matrice concatLig(Matrice m2) {
        if (this.getNbrCol() != m2.getNbrCol()) {
            throw new Error("les matrices doivent avoir même nombre de colonnes");
        }
        Matrice res = new Matrice(this.getNbrLig() + m2.getNbrLig(), this.getNbrCol());
        for (int i = 0; i < res.getNbrLig(); i++) {
            for (int j = 0; j < res.getNbrCol(); j++) {
                if (i < this.getNbrLig()) {
                    res.set(i, j, this.get(i, j));
                } else {
                    res.set(i, j, m2.get(i - this.getNbrLig(), j));
                }
            }
        }
        return res;
    }

    public Matrice concatCol(Matrice m2) {
        if (this.getNbrLig() != m2.getNbrLig()) {
            throw new Error("les matrices doivent avoir même nombre de lignes");
        }
        Matrice res = new Matrice(this.getNbrLig(), this.getNbrCol() + m2.getNbrCol());
        for (int i = 0; i < res.getNbrLig(); i++) {
            for (int j = 0; j < res.getNbrCol(); j++) {
                if (j < this.getNbrCol()) {
                    res.set(i, j, this.get(i, j));
                } else {
                    res.set(i, j, m2.get(i, j - this.getNbrCol()));
                }
            }
        }
        return res;
    }

    //--------------- partie 2.2
    /**
     * sous matrice constituée par les ligne lig telles que
     * {@code ligMin <= lig <= ligMax}
     *
     * @param ligMin
     * @param ligMax
     * @return une nouvelle matrice constituée d'une copie des lignes
     * correspondantes
     */
    public Matrice subLignes(int ligMin, int ligMax) {
        if (ligMin < 0 || ligMax < ligMin || ligMax >= this.getNbrLig()) {
            throw new Error("indices lignes invalides");
        }
        Matrice res = new Matrice(ligMax - ligMin + 1, this.getNbrCol());
        for (int lig = 0; lig < res.getNbrLig(); lig++) {
            for (int col = 0; col < res.getNbrCol(); col++) {
                res.set(lig, col, this.get(ligMin + lig, col));
            }
        }
        return res;
    }

    /**
     * sous matrice constituée par les colonnes col telles que
     * {@code colMin <= col <= colMax}
     *
     * @param colMin
     * @param colMax
     * @return une nouvelle matrice constituée d'une copie des lignes
     * correspondantes
     */
    public Matrice subCols(int colMin, int colMax) {
        if (colMin < 0 || colMax < colMin || colMax >= this.getNbrCol()) {
            throw new Error("indices colonnes invalides");
        }
        Matrice res = new Matrice(this.getNbrLig(), colMax - colMin + 1);
        for (int lig = 0; lig < res.getNbrLig(); lig++) {
            for (int col = 0; col < res.getNbrCol(); col++) {
                res.set(lig, col, this.get(lig, colMin + col));
            }
        }
        return res;
    }

    /**
     *
     * @return une copie de la matrice.
     */
    public Matrice copie() {
        return this.subLignes(0, this.getNbrLig() - 1);
    }

    //--------------- partie 2.3
    public Matrice transposee() {
        Matrice res = new Matrice(this.getNbrCol(), this.getNbrLig());
        for (int i = 0; i < res.getNbrLig(); i++) {
            for (int j = 0; j < res.getNbrCol(); j++) {
                res.set(i, j, this.get(j, i));
            }
        }
        return res;
    }

    //--------------- partie 2.4
    public Matrice metAuCarre() {
        return this.concatCol(Matrice.identite(this.getNbrLig())).concatLig(
                Matrice.identite(this.getNbrCol()).concatCol(this.transposee()));
    }

    public static int intAlea(int bmin, int bmax) {
        return (int) (Math.random() * (bmax - bmin + 1)) + bmin;
    }

    public static void test2() {
        System.out.println("----------- test 2 --------------");
        int nl = intAlea(1, 4), nc = intAlea(1, 4);
        double pz = 0.33;
        Matrice m = Matrice.matAleaZeroUnDeux(nl, nc, pz);
        Matrice mc = m.metAuCarre();
        Matrice mbis = mc.subLignes(0, m.getNbrLig() - 1).subCols(0, m.getNbrCol() - 1);
        System.out.println("m : matrice de base : ");
        System.out.println(m);
        System.out.println("mc : mise au carré : ");
        System.out.println(mc);
        System.out.println("mbis : (sub de mc) : ");
        System.out.println(mbis);
    }

    //--------------- partie 3.1
    /**
     * calcule la somme de deux matrices.
     *
     * @param m2
     * @return this+m2
     */
    public Matrice add(Matrice m2) {
        if (this.getNbrLig() != m2.getNbrLig() || this.getNbrCol() != m2.getNbrCol()) {
            throw new Error("tailles incompatibles pour add");
        }
        Matrice res = new Matrice(this.getNbrLig(), this.getNbrCol());
        for (int i = 0; i < res.getNbrLig(); i++) {
            for (int j = 0; j < res.getNbrCol(); j++) {
                res.set(i, j, this.get(i, j) + m2.get(i, j));
            }
        }
        return res;
    }

    //--------------- partie 3.2
    /**
     * calcule la matrice opposée. {@code Opp_i,j = - M_i,j}.
     *
     * @return
     */
    public Matrice opp() {
        Matrice res = new Matrice(this.getNbrLig(), this.getNbrCol());
        for (int i = 0; i < res.getNbrLig(); i++) {
            for (int j = 0; j < res.getNbrCol(); j++) {
                res.set(i, j, -this.get(i, j));
            }
        }
        return res;
    }

    //--------------- partie 3.3
    /**
     * calcule la this-m2.
     *
     * @param m2
     * @return this-m2
     */
    public Matrice moins(Matrice m2) {
        return this.add(m2.opp());
    }

    //--------------- partie 3.4
    public Matrice mult(Matrice m2) {
        if (this.getNbrCol() != m2.getNbrLig()) {
            throw new Error("tailles incompatibles pour mult");
        }
        Matrice res = new Matrice(this.getNbrLig(), m2.getNbrCol());
        for (int i = 0; i < res.getNbrLig(); i++) {
            for (int j = 0; j < res.getNbrCol(); j++) {
                double cur = 0;
                for (int k = 0; k < this.getNbrCol(); k++) {
                    cur = cur + this.get(i, k) * m2.get(k, j);
                }
                res.set(i, j, cur);
            }
        }
        return res;
    }
    //--------------- partie 3.5

    public static void test3() {
        System.out.println("----------- test 3 --------------");
        Matrice m = Matrice.matTest1();
        System.out.println("mat M : ");
        System.out.println(m);
        System.out.println("M + M^2 :");
        System.out.println(m.add(m.mult(m)));
    }

    //--------------- partie 4.3.1
    /**
     * permute deux lignes de la matrice.
     *
     * @param lig1
     * @param lig2
     * @return la signature de la permutation (1 si |i2 - i1|, -1 sinon)
     */
    public int permuteLigne(int lig1, int lig2) {
        // on utilise ici le fait que les tableaux à deux dimensions en java
        // sont en fait des tableaux de tableaux.
        // voir ci-dessous une autre définition qui ne se sert pas de cette
        // propriété, et qui sera sans doute l'implémentation proposée par
        // les étudiants
        double[] tempLigne = this.coeffs[lig1];
        this.coeffs[lig1] = this.coeffs[lig2];
        this.coeffs[lig2] = tempLigne;
        if (lig1 == lig2) {
            return 1;
        } else {
            return -1;
        }
    }

    public int permuteLigneV2(int lig1, int lig2) {
        // on utilise ici le fait que les tableaux à deux dimensions en java
        // sont en fait des tableaux de tableaux.
        // voir ci-dessous une autre définition qui ne se sert pas de cette
        // propriété, et qui sera sans doute l'implémentation proposée par
        // les étudiants
        for (int col = 0; col < this.getNbrCol(); col++) {
            double temp = this.get(lig1, col);
            this.set(lig1, col, this.get(lig2, col));
            this.set(lig2, col, temp);
        }
        if (Math.abs(lig2 - lig2) % 2 == 0) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * transvection de la ligne lig2 par rapport à la ligne lig1dans le cadre
     * d'une descente de gauss : pour éviter les erreurs d'arrondis, on met
     * explicitement à zero M_lig2,lig1. Pour les autres colonnes :
     * {@code M_lig2,j = M_lig2,j - (M_lig2,lig1 / M_lig1,lig1) * M_lig1,j}
     *
     * @param lig1 doit être inférieur au nombre de colonnes de la matrice
     * @param lig2
     */
    public void transvection(int lig1, int lig2) {
        if (lig1 >= this.getNbrCol()) {
            throw new Error("lig1 doit être inférieur au nombre de colonnes de la matrice");
        }
        if (this.get(lig1, lig1) == 0) {
            throw new Error("pivot nul : ligne " + lig1 + " Mat :\n" + this);
        }
        double p = this.get(lig2, lig1) / this.get(lig1, lig1);
        for (int col = 0; col < this.getNbrCol(); col++) {
            if (col == lig1) {
                this.set(lig2, col, 0);
            } else {
                this.set(lig2, col, this.get(lig2, col) - p * this.get(lig1, col));
            }
        }
    }

    /**
     * trouve une ligne avec un bon pivot. Cette méthode est facile à comprendre
     * et à programmer, mais assez délicate à définir précisément. En gros, on
     * en est à l'étape e de la méthode de Gauss. On cherche un pivot pour le
     * placer en M_e,e par permutation de ligne. n ne considère que les lignes
     * "en dessous" (lig >= e) de la ligne e, et on veut repérer la ligne ligMax
     * qui contient le plus grand élément M_lig,i en valeur absolue.
     *
     * On a en plus un cas particulier : si tous les éléments M_lig,i sont nuls,
     * on ne peut pas trouver de pivot, et la méthode le signalera en renvoyant
     * -1.
     *
     * @param e etape dans la methode de Gauss, défini l'élément diagonal, pivot
     * M_e,e
     * @return -1 si aucun pivot non nul, sinon numéro de ligne contenant le
     * plus grand pivot en valeur absolue.
     */
    public int lignePlusGrandPivot(int e) {
        if (e >= this.getNbrLig() || e >= this.getNbrCol()) {
            throw new Error("mauvais indice de pivot : M_e,e doit exister");
        }
        double curMax = Math.abs(this.get(e, e));
        int imax = e;
        for (int i = e + 1; i < this.getNbrLig(); i++) {
            if (Math.abs(this.get(i, e)) > curMax) {
                curMax = Math.abs(this.get(i, e));
                imax = i;
            }
        }
        if (curMax <= EPSILON_PIVOT) {
            return -1;
        } else {
            return imax;
        }

    }

    public static class ResGauss {

        public int rang;
        public int signaturePermutation;

        public ResGauss(int rang, int signaturePermutation) {
            this.rang = rang;
            this.signaturePermutation = signaturePermutation;
        }

        @Override
        public String toString() {
            return "{ResGauss : rang = " + this.rang + " ; sigPerm = "
                    + this.signaturePermutation + "}";
        }
    }

    /**
     * Annule les élément sous-diagonaux d'une matrice .
     *
     * @return un {@link ResGauss} permettant de connaitre la signature de la
     * permutation appliquée aux lignes, et le nombre d'étapes effectuées.
     */
    public ResGauss descenteGauss() {
        int e = 0;
        int imax;
        int signature = 1;
        while (e < this.getNbrLig() && e < this.getNbrCol()
                && (imax = this.lignePlusGrandPivot(e)) != -1) {
            signature = signature * this.permuteLigne(e, imax);
            for (int lig = e + 1; lig < this.getNbrLig(); lig++) {
                this.transvection(e, lig);
            }
            e++;
        }
        return new ResGauss(e, signature);
    }

    public static void test4() {
        // une syntaxe de création de tableau en extension
        // pas forcément connue mais facile à comprendre :
        // lors de la création, j'indique entre accolade
        // les éléments du tableau créé. Inutile dans ce cas
        // de définir explicitement la taille du tableau : c'est 
        // le nombre d'élements entre les accolades
        System.out.println("--------- test 4 --------------");
        Matrice vect = Matrice.creeVecteur(new double[]{1, 2, 3});
        Matrice[] matTests = new Matrice[]{
            Matrice.matTest1().concatCol(vect),
            Matrice.matTest2().concatCol(vect)
        };
        // la encore, une syntaxe du for pas forcément connue.
        // disons simplement que la variable m va prendre succéssivement
        // toutes les valeurs du tableau matTests.
        // cette syntaxe est disponible pour les tableaux, mais aussi pour
        // toutes les collections (par exemple les listes).
        for (Matrice m : matTests) {
            System.out.println("==============================================");
            System.out.println("matrice : ");
            System.out.println(m);
            ResGauss res = m.descenteGauss();
            System.out.println("matrice après descenteGauss : ");
            System.out.println(m);
            System.out.println("résultat retourné : ");
            System.out.println(res);
        }
    }

    //--------------- partie 4.4
    /**
     * calcule le déterminant d'une matrice carrée.
     *
     * @return le déterminant de this
     */
    public double determinant() {
        if (this.getNbrLig() != this.getNbrCol()) {
            throw new Error("déterminant défini uniquement pour les matrices carrées");
        }
        Matrice ms = this.copie();
        ResGauss tri = ms.descenteGauss();
        if (tri.rang != this.getNbrLig()) {
            return 0;
        } else {
            double det = tri.signaturePermutation;
            for (int k = 0; k < this.getNbrLig(); k++) {
                det = det * ms.get(k, k);
            }
            return det;
        }
    }

    //--------------- partie 4.5
    /**
     * transforme une Matrice en Matrix JAMA
     */
   

    /**
     * transforme une Matrix JAMA en Matrice
     */
    
    

    /**
     * calcule la différence (en valeur absolue) entre les déterminants tels que
     * calculés par notre classe Matrice, et la classe Matrix de JAMA.
     *
     * @return la diff.
     */
  

    
        

    //============== fin des exos demandés en TD =========================
    //--------------- partie 4.3.4
    // à priori, pas à faire durant les TD : c'est la partie à faire en TP
    /**
     * rend unitaire les éléments diagonaux non nuls. Suppose que tous les
     * éléments diagonaux M_i,i sont non nuls pour {@code 0 <= i < rang}
     *
     * @param rang le nombre d'éléments diagonaux non nuls
     */
    public void pivotsUnitaires(int rang) {
        for (int lig = 0; lig < rang; lig++) {
            double div = this.get(lig, lig);
            if (div == 0) {
                throw new Error("pivot nul : ligne " + lig + " Mat :\n" + this);
            }
            for (int col = 0; col < this.getNbrCol(); col++) {
                this.set(lig, col, this.get(lig, col) / div);
            }
        }
    }

    public void remonteeGauss(int rang) {
        for (int e = rang - 1; e >= 0; e--) {
            for (int lig = e - 1; lig >= 0; lig--) {
                this.transvection(e, lig);
            }
        }
    }

    /**
     * Resultat de la résolution d'un système linéaire. Note : on aurait pu
     * utiliser la classe {@link java.util.Optional} mais on reste ici avec du
     * java "basic"
     */
    public static class ResSysLin {

        /**
         * vrai ssi le système admet une solution unique
         */
        private boolean solUnique;
        /**
         * solution du système linéaire. contient null si {@link #solUnique} est
         * false sinon, contient le vecteur solution sous la forme d'une Matrice
         * d'une seule colonne.
         */
        private Matrice sol;

        private ResSysLin() {
            this.solUnique = false;
        }

        private ResSysLin(Matrice sol) {
            this.solUnique = true;
            this.sol = sol;
        }

        public static ResSysLin pasDeSolUnique() {
            return new ResSysLin();
        }

        public static ResSysLin sol(Matrice sol) {
            return new ResSysLin(sol);
        }

        public String toString() {
            if (this.solUnique) {
                return this.sol.toString();
            } else {
                return "{pas de sol unique}";
            }
        }

    }

    public Matrice getColonne(int col) {
        Matrice res = new Matrice(this.getNbrLig(), 1);
        for (int lig = 0; lig < this.getNbrLig(); lig++) {
            res.set(lig, 0, this.get(lig, col));
        }
        return res;
    }

    public ResSysLin resoudSysLin(Matrice secondMembre) {
        if (this.getNbrLig() != this.getNbrCol()) {
            throw new Error("La matrice n'est pas carrée");
        }
        if (secondMembre.getNbrCol() != 1) {
            throw new Error("Le second membre n'est pas un vecteur");
        }
        Matrice toGauss = this.concatCol(secondMembre);
        ResGauss res = toGauss.descenteGauss();
        if (res.rang == this.getNbrLig()) {
            toGauss.pivotsUnitaires(res.rang);
            toGauss.remonteeGauss(res.rang);
            return ResSysLin.sol(toGauss.getColonne(res.rang));
        } else {
            return ResSysLin.pasDeSolUnique();
        }
    }

    /**
     * calcule si possible la matrice inverse.
     *
     * @return un Optional contenant la matrice inverse si this est inversible,
     * null sinon.
     */
    public Optional<Matrice>inverse() {
        if (this.getNbrLig() != this.getNbrCol()) {
            throw new Error("inverse seulement pour les matrices carrées");
        }
        Matrice toGauss = this.concatCol(Matrice.identite(this.getNbrLig()));
        ResGauss triSup = toGauss.descenteGauss();
        if (triSup.rang == this.getNbrLig()) {
            toGauss.pivotsUnitaires(triSup.rang);
            toGauss.remonteeGauss(triSup.rang);
            Matrice inverse = toGauss.subCols(this.getNbrCol(), 2 * this.getNbrCol() - 1);
            return Optional.of(inverse);
        } else {
            return Optional.ofNullable(null);
        }
    }

    // quelque petites méthodes utilitaires pour tester que le calcul
    // de l'inverse (et donc de la descente et de la remontée) est correct
    // en particulier, à cause des erreur d'arrondi, on a besoin de tester
    // non pas l'égalité exacte, mais l'égalité approchée de deux matrice
    // on utilisera la norme la plus simple : le max des coeffs (en val absolue)
    /**
     * calcule la norme sup, aussi appelée norme "infinie".
     * {@code ||M|| = sup(i in 0..nbrLig-1 ; j in 0..nbrCol-1 ; | M_i,j |}
     *
     * @return
     */
    public double normeSup() {
        double max = 0;
        for (int lig = 0; lig < this.getNbrLig(); lig++) {
            for (int col = 0; col < this.getNbrCol(); col++) {
                double cur = Math.abs(this.get(lig, col));
                if (cur > max) {
                    max = cur;
                }
            }
        }
        return max;
    }

    /**
     * distance entre deux matrice basée sur la norme sup : {@link #normeSup()
     * }.
     *
     * @param m2
     * @return distance(this,m2) suivant la norme {@link #normeSup() }
     */
    public double distSup(Matrice m2) {
        return this.moins(m2).normeSup();
    }

    public static void testTraceInverse() {
        Matrice m = Matrice.matTest2();
        System.out.println("M : \n" + m);
        Matrice toGauss = m.concatCol(Matrice.identite(m.getNbrLig()));
        System.out.println("toGauss : \n" + toGauss);
        ResGauss triSup = toGauss.descenteGauss();
        System.out.println("après descente : \n" + toGauss);
        System.out.println("res = " + triSup);
        if (triSup.rang == m.getNbrLig()) {
            toGauss.pivotsUnitaires(triSup.rang);
            System.out.println("après pivots unitaires : \n" + toGauss);
            toGauss.remonteeGauss(triSup.rang);
            System.out.println("après remontée : \n" + toGauss);
            Matrice inverse = toGauss.subCols(m.getNbrCol(), 2 * m.getNbrCol() - 1);
            System.out.println("inverse : \n" + inverse);
            Matrice idCalc = m.mult(inverse);
            System.out.println("M . M-1 : \n" + idCalc);
            System.out.println("dist(M . M-1 , Id) = "
                    + idCalc.distSup(Matrice.identite(m.getNbrLig())));
        } else {
            System.out.println("non inversible");
        }

    }

    /**
     * test le calcul d'inverse nbrTest fois sur des matrice alea 0-1-2 de
     * taille taille avec une proportion de 0 de 0.33 taille. affiche une erreur
     * si printErreurs est vrai, et M est inversible, et distSup(M.M-1,Id) >
     * epsilonDistAcceptable
     *
     * @param nbrTest
     * @param taille
     * @param epsilonDistAcceptable
     */
    public static void testInverse(int nbrTest, int taille, double epsilonDistAcceptable,
            boolean printErreurs) {
        System.out.println("test distance(M . M-1,Identite) < epsilon");
        System.out.println("nbr Tests : " + nbrTest);
        System.out.println("taille des matrices : " + taille);
        System.out.println("Matrices contenant 0,1, ou 2 avec probas ~ 1/3;1/3;1/3");
        System.out.println("epsilon : " + epsilonDistAcceptable);
        int nbrNonInversible = 0;
        int nbrErreur = 0;
        for (int i = 0; i < nbrTest; i++) {
            Matrice m = Matrice.matAleaZeroUnDeux(taille, taille, 0.33);
            Matrice id = Matrice.identite(taille);
            Matrice mc = m.copie().concatCol(id);
            ResGauss rg = mc.descenteGauss();
            if (rg.rang != taille) {
                nbrNonInversible++;
            } else {
                mc.pivotsUnitaires(taille);
                mc.remonteeGauss(taille);
                Matrice inverse = mc.subCols(taille, 2 * taille - 1);
                Matrice idCalc = m.mult(inverse);
                double dist = id.distSup(idCalc);
                if (dist > epsilonDistAcceptable) {
                    nbrErreur++;
                    if (printErreurs) {
                        System.out.println("--------- problème ------------");
                        System.out.println("M =");
                        System.out.println(m);
                        System.out.println("M-1 = ");
                        System.out.println(inverse);
                        System.out.println("M . M-1 =");
                        System.out.println(idCalc);
                        System.out.println("distance sup = " + dist);
                    }
                }
            }
        }
        System.out.println("non inversible : " + nbrNonInversible + "/" + nbrTest
                + " ~ " + (nbrNonInversible * 100.0 / nbrTest) + " %");
        System.out.println("nombre d'inverses avec dist > " + epsilonDistAcceptable + " : "
                + nbrErreur + " (devrait être 0)");
    }

    // non demandé, utilisés pour les exemples de l'énoncé
    public static void test2Old() {
        int nlig = intAlea(1, 4);
        int ncol = intAlea(1, 4);
        double pz = 0.33;
        Matrice m = Matrice.matAleaZeroUnDeux(nlig, ncol, pz);
        System.out.println("matrice alea : ");
        System.out.println(m);
        System.out.println("matrice dans carre : ");
        System.out.println(m.metAuCarre());
    }

    public static void exempleCreeVecteur() {
        System.out.println("----- exempleCreeVecteur");
        double[] ex = new double[]{1, 2, 3};
        Matrice m = Matrice.creeVecteur(ex);
        ListeDessinsTexte txt = new ListeDessinsTexte();
        txt.add(new DessinTexte(Arrays.toString(ex) + "\n\n", "tableau", 0));
        txt.add(new DessinTexte("==>\n\n", " ", 0));
        txt.add(new DessinTexte(m.toString(), "Matrice", 0));
        System.out.println(txt.toTextualRep(2));
    }

    public static void exempleConcatLig() {
        System.out.println("----- exempleConcatLig");
        Matrice m = Matrice.matAleaZeroUnDeux(3, 4, 0.33);
        Matrice n = Matrice.matAleaZeroUnDeux(2, 4, 0.33);
        ListeDessinsTexte txt = new ListeDessinsTexte();
        txt.add(new DessinTexte(m.toString(), "M", 0));
        txt.add(new DessinTexte("concatLig\n\n", " ", 0));
        txt.add(new DessinTexte(n.toString(), "N", 0));
        txt.add(new DessinTexte("==>\n\n", " ", 0));
        txt.add(new DessinTexte(m.concatLig(n).toString(), "M conc N", 0));
        System.out.println(txt.toTextualRep(2));
    }

    public static void exempleConcatCol() {
        System.out.println("----- exempleConcatCol");
        Matrice m = Matrice.matAleaZeroUnDeux(3, 2, 0.33);
        Matrice n = Matrice.matAleaZeroUnDeux(3, 3, 0.33);
        ListeDessinsTexte txt = new ListeDessinsTexte();
        txt.add(new DessinTexte(m.toString(), "M", 0));
        txt.add(new DessinTexte("concatCol\n\n", " ", 0));
        txt.add(new DessinTexte(n.toString(), "N", 0));
        txt.add(new DessinTexte("==>\n\n", " ", 0));
        txt.add(new DessinTexte(m.concatCol(n).toString(), "M conc N", 0));
        System.out.println(txt.toTextualRep(2));
    }

    public static void exempleSubLignes() {
        System.out.println("----- exempleSubLignes");
        Matrice m = Matrice.matAleaZeroUnDeux(5, 4, 0.33);
        int nMin = 1, nMax = 3;
        ListeDessinsTexte txt = new ListeDessinsTexte();
        txt.add(new DessinTexte(m.toString(), "M", 0));
        txt.add(new DessinTexte(".subLignes(" + nMin + "," + nMax + ")\n\n", " ", 0));
        txt.add(new DessinTexte("==>\n\n", " ", 0));
        txt.add(new DessinTexte(m.subLignes(nMin, nMax).toString(), "sub M", 0));
        System.out.println(txt.toTextualRep(2));
    }

    public static void exempleSubCols() {
        System.out.println("----- exempleSubCols");
        Matrice m = Matrice.matAleaZeroUnDeux(3, 6, 0.33);
        int cMin = 1, cMax = 3;
        ListeDessinsTexte txt = new ListeDessinsTexte();
        txt.add(new DessinTexte(m.toString(), "M", 0));
        txt.add(new DessinTexte(".subCols(" + cMin + "," + cMax + ")\n\n", " ", 0));
        txt.add(new DessinTexte("==>\n\n", " ", 0));
        txt.add(new DessinTexte(m.subCols(cMin, cMax).toString(), "sub M", 0));
        System.out.println(txt.toTextualRep(2));
    }

    public static void exempleTranspose() {
        System.out.println("----- exempleTranspose");
        Matrice m = Matrice.matAleaZeroUnDeux(5, 2, 0.33);
        ListeDessinsTexte txt = new ListeDessinsTexte();
        txt.add(new DessinTexte(m.toString(), "M", 0));
        txt.add(new DessinTexte("==>\n\n\n", " ", 0));
        txt.add(new DessinTexte(m.transposee().toString(), "Mt", 0));
        System.out.println(txt.toTextualRep(2));
    }

    public static void exempleMetAuCarre() {
        System.out.println("----- exempleMetAuCarre");
        Matrice m = Matrice.matAleaZeroUnDeux(2, 3, 0.33);
        ListeDessinsTexte txt = new ListeDessinsTexte();
        txt.add(new DessinTexte(m.toString() + "\n\n", "M", 0));
        txt.add(new DessinTexte("==>\n\n\n", " ", 0));
        txt.add(new DessinTexte(m.metAuCarre().toString(), "Carre", 0));
        System.out.println(txt.toTextualRep(2));
    }

    public static void exempleAdd() {
        System.out.println("----- exempleAdd");
        Matrice m = Matrice.matAleaZeroUnDeux(4, 3, 0.33);
        Matrice n = Matrice.matAleaZeroUnDeux(4, 3, 0.33);
        ListeDessinsTexte txt = new ListeDessinsTexte();
        txt.add(new DessinTexte(m.toString(), "M", 0));
        txt.add(new DessinTexte("x\n\n", " ", 0));
        txt.add(new DessinTexte(n.toString(), "N", 0));
        txt.add(new DessinTexte("==>\n\n", " ", 0));
        txt.add(new DessinTexte(m.add(n).toString(), "M + N", 0));
        System.out.println(txt.toTextualRep(2));
    }

    public static void exempleOpp() {
        System.out.println("----- exempleOpp");
        Matrice m = Matrice.matAleaZeroUnDeux(4, 3, 0.33);
        ListeDessinsTexte txt = new ListeDessinsTexte();
        txt.add(new DessinTexte(m.toString(), "M", 0));
        txt.add(new DessinTexte(".opp()\n\n", " ", 0));
        txt.add(new DessinTexte("==>\n\n", " ", 0));
        txt.add(new DessinTexte(m.opp().toString(), "-M", 0));
        System.out.println(txt.toTextualRep(2));
    }

    public static void exempleTest3() {
        System.out.println("----- exempleTest3");
        Matrice m = Matrice.matTest1();
        ListeDessinsTexte txt = new ListeDessinsTexte();
        txt.add(new DessinTexte(m.toString(), "M", 0));
        txt.add(new DessinTexte("\n\n", " ", 0));
        txt.add(new DessinTexte("==>\n\n", " ", 0));
        txt.add(new DessinTexte(m.add(m.mult(m)).toString(), "M + M^2", 0));
        System.out.println(txt.toTextualRep(2));
    }

    public static void exempleMoins() {
        System.out.println("----- exempleMoins");
        Matrice m = Matrice.matAleaZeroUnDeux(4, 3, 0.33);
        Matrice n = Matrice.matAleaZeroUnDeux(4, 3, 0.33);
        ListeDessinsTexte txt = new ListeDessinsTexte();
        txt.add(new DessinTexte(m.toString(), "M", 0));
        txt.add(new DessinTexte(" - \n\n", " ", 0));
        txt.add(new DessinTexte(n.toString(), "N", 0));
        txt.add(new DessinTexte("==>\n\n", " ", 0));
        txt.add(new DessinTexte(m.moins(n).toString(), "M - N", 0));
        System.out.println(txt.toTextualRep(2));
    }

    public static void exempleMult() {
        System.out.println("----- exempleMult");
        Matrice m = Matrice.matAleaZeroUnDeux(3, 4, 0.33);
        Matrice n = Matrice.matAleaZeroUnDeux(4, 2, 0.33);
        ListeDessinsTexte txt = new ListeDessinsTexte();
        txt.add(new DessinTexte(m.toString(), "M", 0));
        txt.add(new DessinTexte("x\n\n", " ", 0));
        txt.add(new DessinTexte(n.toString(), "N", 0));
        txt.add(new DessinTexte("==>\n\n", " ", 0));
        txt.add(new DessinTexte(m.mult(n).toString(), "M . N", 0));
        System.out.println(txt.toTextualRep(2));
    }

    public static void exemplePermutLigne() {
        System.out.println("----- exemplePermutLigne");
        Matrice m = Matrice.matAleaZeroUnDeux(4, 3, 0.33);
        int i1 = 1, i2 = 3;
        ListeDessinsTexte txt = new ListeDessinsTexte();
        txt.add(new DessinTexte(m.toString(), "M", 0));
        int res = m.permuteLigne(i1, i2);
        txt.add(new DessinTexte(".permuteLigne(" + i1 + "," + i2 + ") ==> \n\n", " ", 0));
        txt.add(new DessinTexte(m.toString(), "M après", 0));
        txt.add(new DessinTexte(" (parite = " + res + ")\n\n", " ", 0));
        System.out.println(txt.toTextualRep(2));
    }

    public static void exempleTransvection() {
        System.out.println("----- exempleTransvection");
        Matrice m = Matrice.matTest1();
        m.permuteLigne(0, 1);
        int lig1 = 0, lig2 = 2;
        ListeDessinsTexte txt = new ListeDessinsTexte();
        txt.add(new DessinTexte(m.toString(), "M", 0));
        m.transvection(lig1, lig2);
        txt.add(new DessinTexte(".transvection(" + lig1 + "," + lig2 + ") ==> \n\n", " ", 0));
        txt.add(new DessinTexte(m.toString(), "M après", 0));
        System.out.println(txt.toTextualRep(2));
    }

    public static void exempleLignePlusGrandPivot() {
        System.out.println("----- exempleLignePlusGrandPivot");
        Matrice m = Matrice.matTest1();
        int e = 0;
        ListeDessinsTexte txt = new ListeDessinsTexte();
        txt.add(new DessinTexte(m.toString(), "M", 0));
        int res = m.lignePlusGrandPivot(e);
        txt.add(new DessinTexte(".lignePlusGrandPivot(" + e + ") ==> " + res + "\n\n", " ", 0));
        System.out.println(txt.toTextualRep(2));
        m = Matrice.identite(3);
        m.set(1, 1, 0);
        m.set(1, 2, 1);
        m.set(0, 1, 1);
        e = 1;
        txt = new ListeDessinsTexte();
        txt.add(new DessinTexte(m.toString(), "M", 0));
        res = m.lignePlusGrandPivot(e);
        txt.add(new DessinTexte(".lignePlusGrandPivot(" + e + ") ==> " + res + "\n\n", " ", 0));
        System.out.println(txt.toTextualRep(2));
    }

    public ResGauss descenteGaussAvecTrace() {
        System.out.println("----- descenteGaussAvecTrace");
        int e = 0;
        int imax;
        int signature = 1;
        while (e < this.getNbrLig() && e < this.getNbrCol()
                && (imax = this.lignePlusGrandPivot(e)) != -1) {
            System.out.println("------------ étape " + e + " ------------");
            System.out.println("ligne pivot max : " + imax);
            signature = signature * this.permuteLigne(e, imax);
            System.out.println("mat permutée = \n" + this);
            for (int lig = e + 1; lig < this.getNbrLig(); lig++) {
                this.transvection(e, lig);
            }
            System.out.println("mat après les transvections");
            System.out.println(this);
            e++;
        }
        return new ResGauss(e, signature);
    }

    public static Matrice matSujet() {
        return new Matrice(new double[][]{
            {1, 4, 1},
            {1, 2, -1},
            {3, 1, 2}
        });
    }

    public static Matrice vectSujet() {
        return new Matrice(new double[][]{
            {3},
            {4},
            {1}
        });
    }

    public static void resoudExempleTreillisPortique() {
        System.out.println("----- treillis portique du sujet de projet");
        Matrice mat = new Matrice(6, 6);
        double a = Math.sqrt(2) / 2;
        int i = 0;
        mat.coeffs[i][0] = a;
        mat.coeffs[i][3] = 1;

        i++;
        mat.coeffs[i][0] = -a;
        mat.coeffs[i][2] = -1;
        mat.coeffs[i][4] = 1;

        i++;
        mat.coeffs[i][1] = a;
        mat.coeffs[i][5] = 1;

        i++;
        mat.coeffs[i][1] = a;
        mat.coeffs[i][2] = 1;

        i++;
        mat.coeffs[i][0] = -a;
        mat.coeffs[i][1] = -a;

        i++;
        mat.coeffs[i][0] = a;
        mat.coeffs[i][1] = -a;
        System.out.println("mat : \n" + mat);

        Matrice sm = new Matrice(6, 1);
        sm.coeffs[5][0] = 1000;
        System.out.println("second membre : \n" + sm);

        ResSysLin res = mat.resoudSysLin(sm);
        if (res.solUnique) {
            System.out.println("sol : \n" + res.sol);
        } else {
            System.out.println("pas de sol");
        }

    }

    public static Matrice exempleSujet() {
        return matSujet().concatCol(vectSujet());
    }

    public static void exempleDescenteGauss(Matrice m) {
        System.out.println("----- exempleDescenteGauss");
        ListeDessinsTexte txt = new ListeDessinsTexte();
        txt.add(new DessinTexte(m.toString(), "M", 0));
        ResGauss res = m.descenteGauss();
        txt.add(new DessinTexte(".descenteGauss() ==> \n\n", " ", 0));
        txt.add(new DessinTexte(m.toString(), "M après", 0));
        txt.add(new DessinTexte("rang : " + res.rang
                + "\nsigPerm : " + res.signaturePermutation + "\n", 0));
        System.out.println(txt.toTextualRep(2));
    }

    public static void exempleDeterminant(Matrice m) {
        System.out.println("----- exempleDeterminant");
        ListeDessinsTexte txt = new ListeDessinsTexte();
        txt.add(new DessinTexte(m.toString(), "M", 0));
        Matrice ms = m.copie();
        ResGauss res = ms.descenteGauss();
        txt.add(new DessinTexte(".descenteGauss() ==> \n\n", " ", 0));
        txt.add(new DessinTexte(ms.toString(), "M après", 0));
        txt.add(new DessinTexte("rang : " + res.rang
                + "\nsigPerm : " + res.signaturePermutation + "\n", 0));
        System.out.println(txt.toTextualRep(2));
        txt = new ListeDessinsTexte();
        txt.add(new DessinTexte(m.toString(), "M", 0));
        txt.add(new DessinTexte(".determinant() ==> " + m.determinant() + "\n\n\n"));
        System.out.println(txt.toTextualRep(2));
    }

    public static void exempleSujetRemontee() {
        System.out.println("----- exempleSujetRemontee");
        Matrice m = Matrice.exempleSujet();
        System.out.println("matrice sujet : ");
        System.out.println(m);
        ResGauss res = m.descenteGauss();
        System.out.println("après descente :");
        System.out.println(m);
        m.pivotsUnitaires(res.rang);
        System.out.println("après pivots unitaires :");
        System.out.println(m);
        m.remonteeGauss(res.rang);
        System.out.println("après remontée : ");
        System.out.println(m);
        System.out.println("résolution directe du système : ");
        System.out.println(matSujet().resoudSysLin(vectSujet()));
    }

    public static void exempleInverse() {
        System.out.println("----- exempleInverse");
        Matrice m = Matrice.matSujet().concatCol(Matrice.identite(3));
        System.out.println("matrice sujet pour calcul inverse : ");
        System.out.println(m);
        ResGauss res = m.descenteGauss();
        System.out.println("après descente :");
        System.out.println(m);
        m.pivotsUnitaires(res.rang);
        System.out.println("après pivots unitaires :");
        System.out.println(m);
        m.remonteeGauss(res.rang);
        System.out.println("après remontée : ");
        System.out.println(m);
    }

    public static void tousLesExemples() {
        System.out.println("========== EXEMPLES DU SUJET ===================");
        exempleCreeVecteur();
        exempleConcatLig();
        exempleConcatCol();
        exempleSubLignes();
        exempleSubCols();
        exempleTranspose();
        exempleMetAuCarre();
        exempleAdd();
        exempleOpp();
        exempleTest3();
        exempleMoins();
        exempleMult();
        exemplePermutLigne();
        exempleTransvection();
        exempleLignePlusGrandPivot();
        Matrice.exempleSujet().descenteGaussAvecTrace();
        resoudExempleTreillisPortique();
        exempleDescenteGauss(Matrice.exempleSujet());
        exempleDeterminant(Matrice.matTest1());
        exempleSujetRemontee();
        exempleInverse();

    }

    

    public static void testsDemandesDansLeSujet() {
        System.out.println("============ TESTS DEMANDES DANS LE SUJET =========");
        test1();
        test2();
        test3();
        test4();
    }

    public static void testsSupplementaires() {
        System.out.println("============ TESTS SUPPLEMENTAIRES =========");
        testTraceInverse();
    }
    
    
    // les matrices ci-dessous posent des problèmes particuliers
    // d'instabilité numérique si l'on ne prend pas garde
    // dans la transvection et le test de pivot "nul"
    public static Matrice matProblem1() {
        double[][] coeffs = new double[][]{
            {+1.00E+00, +0.00E+00, +2.00E+00, +2.00E+00, +2.00E+00},
            {+2.00E+00, +0.00E+00, +1.00E+00, +0.00E+00, +2.00E+00},
            {+0.00E+00, +0.00E+00, +1.00E+00, +0.00E+00, +2.00E+00},
            {+0.00E+00, +2.00E+00, +0.00E+00, +1.00E+00, +0.00E+00},
            {+2.00E+00, +2.00E+00, +0.00E+00, +0.00E+00, +1.00E+00},};
        return new Matrice(coeffs);
    }

    public static Matrice matProblem2() {
        double[][] coeffs = new double[][]{
            {+1.00E+00, +2.00E+00, +1.00E+00, +0.00E+00},
            {+1.00E+00, +0.00E+00, +0.00E+00, +1.00E+00},
            {+0.00E+00, +1.00E+00, +1.00E+00, +1.00E+00},
            {+2.00E+00, +1.00E+00, +0.00E+00, +0.00E+00},};
        return new Matrice(coeffs);
    }

    public static Matrice matProblem3() {
        double[][] coeffs = new double[][]{
            {+1.00E+00, 1.00E+00, 0.00E+00},
            {+0.00E+00, 0.00E+00, 2.00E+00},
            {+2.00E+00, 0.00E+00, 1.00E+00}
        };
        return new Matrice(coeffs);
    }

    public static void testProblem2() {
        Matrice m = Matrice.matProblem2();
        ResGauss res = m.concatCol(Matrice.identite(m.getNbrLig())).descenteGaussAvecTrace();
        System.out.println("res : " + res);
    }

   
}
