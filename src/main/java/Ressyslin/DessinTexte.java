/*
    Copyright 2000-2011 Francois de Bertrand de Beuvron

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


package Ressyslin;

import Ressyslin.utils.Console;


/**
  * souvent, dans les tps ou les interros, je demande de faire un petit programme dessinant
  * des triangles, des maisons, ... dont la taille depend d'un entier
  *
  * cette classe conserve ces dessin sous forme de tableau a deux dimensions
  * de caractères. La ligne du tableau N° 0 représente la ligne du bas du dessin
  *
  * @author François de Beuvron (INSA Strasbourg)
  * @version 08/2002
  */

public class DessinTexte {
  protected char[][] dessin;

  public DessinTexte(char[][] dessin) {
    this.dessin = dessin;
  }

  public DessinTexte(String dessin) {
    this(dessin,null);
  }

  /**
   * cree un DessinTexte constitué d'un dessin proprement dit plus nbrLignesAvantLegende lignes
   * blanche, plus une ligne contenant la légende centrée par rapport au dessin
   *
   * @param dessin
   * @param legende
   * @param nbrLignesAvantLegende
   */
  public DessinTexte(String dessin,String legende, int nbrLignesAvantLegende) {
    if (dessin == null) {
      dessin = "";
    }
    else {
      dessin = normaliseBlancsSaufRetourLigne(dessin);
    }
    if (legende != null) {
      legende = normaliseBlancs(legende);
    }
    this.dessin = toCharArray(dessin,legende,nbrLignesAvantLegende);
  }

  /**
   * cree un DessinTexte constitué d'un dessin proprement dit plus une ligne
   * blanche, plus une ligne contenant la légende centrée par rapport au dessin
   *
   * @param dessin
   * @param legende
   */
  public DessinTexte(String dessin,String legende) {
    this(dessin,legende,1);
  }

  /**
   * meme chose que {@link #DessinTexte(String dessin,String legende, int nbrLigneAvantLegende)}
   * mais la légende est simplement la representation textuelle d'un entier
   *
   */
  public DessinTexte(String dessin,int legendeNum, int nbrLigneAvantLegende) {
    this(dessin,""+legendeNum,nbrLigneAvantLegende);
  }

  /**
   * meme chose que {@link #DessinTexte(String dessin,String legende)}
   * mais la légende est simplement la representation textuelle d'un entier
   *
   */
  public DessinTexte(String dessin,int legendeNum) {
    this(dessin,""+legendeNum);
  }

  /**
   * transforme une chaine de caractère en tableau 2D de char (suivant les
   * caractères '\n' contenus dans la chaine
   * @param dessin le dessin sous forme de chaine de caractère
   * @param legende une légende a ajouter en dessous du dessin (après une ligne vide).
   *    la legende ne doit pas comporter de retour a la ligne
   *    ni légende ni ligne vide ajoutée si legende == null
   *    deux ligne vides ajoutées si legende.length == 0
   * @return le dessin sous forme de tableau en 2 dimensions de caractères
   */
  public static char[][] toCharArray(String dessin,String legende, int nbrLigneAvantLegende) {
    int nbrlig = 0;
    int nbrcol = 0;
    int curcol = 0;
    for(int i = 0 ; i < dessin.length(); i ++) {
      if (dessin.charAt(i) == '\n') {
        nbrlig ++;
        if (curcol > nbrcol) {
          nbrcol = curcol;
        }
        curcol = 0;
      }
      else {
        curcol ++;
      }
    }
    if (curcol > nbrcol) {
      nbrcol = curcol;
    }
    nbrlig ++;
    if (legende != null) {
      nbrlig = nbrlig + nbrLigneAvantLegende + 1;
      nbrcol = Math.max(nbrcol,legende.length());
      dessin = dessin + multString("\n",nbrLigneAvantLegende + 1) +
               multString(" ",(nbrcol-legende.length())/2) + legende;
    }
    char [][] res = new char[nbrlig][nbrcol];
    for(int i = 0 ; i < nbrlig ; i ++) {
      for (int j = 0 ; j < nbrcol ; j ++) {
        res[i][j] = ' ';
      }
    }
    int curlig  = 0;
    curcol = 0;
    for(int i = 0 ; i < dessin.length(); i ++) {
      if (dessin.charAt(i) == '\n') {
        curlig ++;
        curcol = 0;
      }
      else {
        res[nbrlig-curlig-1][curcol] = dessin.charAt(i);
        curcol ++;
      }
    }
    return res;
  }

  public static String normaliseBlancs(String originale) {
    StringBuffer res = new StringBuffer();
    for (int i = 0 ; i < originale.length(); i ++) {
      if (Character.isWhitespace(originale.charAt(i))) {
        res = res.append(' ');
      }
      else {
        res = res.append(originale.charAt(i));
      }
    }
    return res.toString();
  }

  public static String normaliseBlancsSaufRetourLigne(String originale) {
    StringBuffer res = new StringBuffer();
    for (int i = 0 ; i < originale.length(); i ++) {
      if (Character.isWhitespace(originale.charAt(i)) && originale.charAt(i) != '\n') {
        res = res.append(' ');
      }
      else {
        res = res.append(originale.charAt(i));
      }
    }
    return res.toString();
  }

  /**
   * "multiplication" de string
   * @param one string originale
   * @param nbr nombre de copies
   * @return une nouvelle chaine qui est la concaténation de nbr fois la chaine originale
   */
  public static String multString(String one,int nbr) {
    StringBuffer res = new StringBuffer();
    for(int i = 0 ; i < nbr ; i++) {
      res.append(one);
    }
    return res.toString();
  }

  /**
   * @return le nombre de lignes dans le dessin
   */
  public int getNbrLig() {
    return this.dessin.length;
  }

  public int getNbrCol() {
    if (this.dessin == null || this.dessin.length == 0) {
      return 0;
    }
    else {
      return this.dessin[0].length;
    }
  }

  /**
   * @param lig le numero de la ligne du dessin (0 = ligne du bas du dessin)
   * @return la chaine correspondant a la ligne lig dans le dessin si celle-ci existe
   *   ou une ligne de la meme taille que le dessin, mais ne contenant que des espace
   *   si la ligne n'est pas dans le dessin (le nombre de lignes dans le dessin est inférieur
   *   a lig)
   */
  public String getLigne(int lig) {
    if (lig >= this.getNbrLig()) {
      return multString(" ",this.getNbrCol());
    }
    else {
      return new String(this.dessin[lig]);
    }
  }

  public String toString() {
    StringBuffer res = new StringBuffer();
    for (int i = this.getNbrLig()-1 ; i >= 0 ; i --) {
      for (int j = 0 ; j < this.getNbrCol() ; j ++) {
        res.append(this.dessin[i][j]);
      }
      res.append('\n');
    }
    return res.toString();
  }

  public static void main(String[] args) {
    Console.println(new DessinTexte("coucou\ntoto\nand\nco",2,0).toString());
//    Console.println(new DessinTexte(null,"coucu",0).toString());
  }

}
