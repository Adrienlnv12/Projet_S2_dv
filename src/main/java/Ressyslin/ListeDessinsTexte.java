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

import java.util.*;

/**
  * souvent, dans les tps et interros, je demande de faire un petit programme dessinant
  * des triangles, des maisons, ... dont la taille depend d'un entier
  *
  * <p> dans le texte du sujet de l'interro, je dois donner des exemples.
  *
  * <p> Cette classe fourni des petits utilitaires permettant d'afficher plusieurs
  * petit 'dessins texte' de gauche a droite sur une ligne
  *
  * <p> les "dessins texte" successifs sont séparés par une collone de blancs
  *
  * @author François de Beuvron (INSA Strasbourg)
  * @version 08/2002
  */

public class ListeDessinsTexte extends ArrayList{

  public int getHauteurMax() {
    int res = 0;
    for (Iterator it = this.iterator(); it.hasNext(); ) {
      DessinTexte next = (DessinTexte) it.next();
      if (next.getNbrLig() > res) {
        res = next.getNbrLig();
      }
    }
    return res;
  }

  public String toTextualRep(int nbrBlancsEntreDessin) {
    return this.toTextualRep(nbrBlancsEntreDessin,"","");
  }

  public String toTextualRep(int nbrBlancsEntreDessin,String debutLigne) {
    return this.toTextualRep(nbrBlancsEntreDessin,debutLigne,"");
  }

 
  public String toTextualRep(int nbrEspaceEntreDessins,String debutLigne,String finLigne) {
      return toTextualRep(DessinTexte.multString(" ", nbrEspaceEntreDessins),
              debutLigne, finLigne);
  }
  public String toTextualRep(String separateur,String debutLigne,String finLigne) {
    StringBuffer res = new StringBuffer();
    int maxlig = this.getHauteurMax();
    for (int i = maxlig ; i >= 0 ; i --) {
      res.append(debutLigne);
      for (Iterator it = this.iterator(); it.hasNext(); ) {
        DessinTexte next = (DessinTexte) it.next();
        res.append(next.getLigne(i));
        if (it.hasNext()) {
          res.append(separateur);
        }
      }
      res.append(finLigne);
      res.append('\n');
    }
    return res.toString();
  }


}
