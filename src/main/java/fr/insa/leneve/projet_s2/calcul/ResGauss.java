/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.calcul;

/**
 *
 * @author adrie
 */
public class ResGauss {
    public int rang, signature; // les deux éléments essentiels

	ResGauss(int rang, int signature)
	{
    	this.rang = rang;
    	this.signature = signature;
	}

	@Override
	// éléments à renvoyer
	public String toString() {
    	return "ResGauss{" +
            	"rang=" + rang +
            	", signature=" + signature +
            	'}';
	}
}
