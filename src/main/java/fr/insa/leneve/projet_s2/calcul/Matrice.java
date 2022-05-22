/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package fr.insa.leneve.projet_s2.calcul;

/**
 *
 * @author adrie
 */
public class Matrice {
    private final int nbrLig, nbrCol;
	private double[][] coeffs;

	public Matrice(int nl, int nc, double[][] coeffs){ // déclaration des éléments
    	this.nbrLig = nl;
    	this.nbrCol = nc;
    	this.coeffs = coeffs;
	}

	public Matrice(int nl, int nc) //méthode matrice
	{
    	double[][] coeffs = new double[nl][nc];
    	for (int i = 0; i < nl; i++) { //ligne
        	for (int j = 0; j < nc; j++) { //colonne
            	coeffs[i][j] = 0;
        	}
    	}
    	this.nbrLig = nl;
    	this.nbrCol = nc;
    	this.coeffs = coeffs;
	}

	public Matrice(int size){
    	this(size, size);
	}
// ----------------------------------------------------------------------------
	@Override
	public String toString() { // affichage de la matrice

    	String present = "";

    	for (int i = 0; i < nbrLig; i++) {
        	present += "[ ";
        	for (int j = 0; j < nbrCol; j++) {
            	present += String.format("%+4.2E",coeffs[i][j])+ " ";
        	}
        	present += "]\n";
    	}

    	return present;
	}
// ----------------------------------------------------------------------------
	public Matrice identite(int size){  // on crée la matrice identité, 0 partout sauf pour i = j
    	Matrice id = new Matrice(size);
    	for (int i = 0; i < size; i++) {
        	id.coeffs[i][i] = 1;
    	}
    	return id;
	}

	public static Matrice matTest1() {
    	Matrice matTest = new Matrice(3); // matrice 3x3
    	for (int i = 0; i < 3; i++) {
        	for (int j = 0; j < 3; j++) {
            	matTest.coeffs[i][j] = i * 3 + j;
        	}
    	}
    	return matTest;
	}

	public static int aleaUnOuDeux() {return Math.random() < 0.5 ? 1: 2;} 

	public static Matrice matAleaZeroUnDeux(int nl, int nc, double pz){
    	Matrice mat = new Matrice(nl, nc);
    	for (int i = 0; i < nl; i++) {
        	for (int j = 0; j < nc; j++) {
            	mat.coeffs[i][j] = Math.random() < pz ? 0: aleaUnOuDeux(); 
        	}
    	}
    	return mat;
	}

	public static Matrice creeVecteur(double[] values) {
    	Matrice vect = new Matrice(values.length, 1);
    	for (int i = 0; i < values.length; i++) { // il s'agit d'un vecteur donc nbrCol = 1 par défaut
        	vect.coeffs[i][0] = values[i];
    	}
    	return vect;
	}
// Get et Set
	public Double get(int i,int j) {
    	return coeffs[i][j];
	}

	public int getNbrCol() {
    	return nbrCol;
	}

	public int getNbrLig() {
    	return nbrLig;
	}

	public void set(int i, int j, double value) {
    	coeffs[i][j] = value;
	}

	public Matrice concatLig(Matrice mat2) {
        // prend deux matrices et concatène les matrices dans une matrice telle que nbrLigMatTot = NbrLig1 + NbrLig2
    	if (this.nbrCol != mat2.nbrCol) {
        	// gestion d'une erreur : cas ou nbrCol1 != nbrCol2
        	throw new Error("Le nombre de colonnes est différent : "+ this.nbrCol + " et " + mat2.nbrCol);
    	}
    	Matrice mat = new Matrice(this.nbrLig + mat2.nbrLig, this.nbrCol);
    	for (int i = 0; i < mat.nbrLig; i++) {
        	for (int j = 0; j < mat.nbrCol; j++) {
            	mat.coeffs[i][j] = (i < this.nbrLig) ? this.coeffs[i][j] : mat2.coeffs[i - this.nbrLig][j];
        	}
    	}
    	return mat;

	}

	public static Matrice concatCol(Matrice mat1, Matrice mat2) { 
// prend deux matrices et concatène les matrices dans une matrice telle que nbrColMatTot = NbrCol1 + NbrCol2
    	if (mat1.nbrLig != mat2.nbrLig) {
        	throw new Error("Le nombre de lignes est différent : " + mat1.nbrLig + " et " + mat2.nbrLig);
    	} else {

        	Matrice mat = new Matrice(mat1.nbrLig, mat1.nbrCol + mat2.nbrCol);
        	for (int i = 0; i < mat.nbrLig; i++) {
            	for (int j = 0; j < mat.nbrCol; j++) {
                	mat.coeffs[i][j] = (j < mat1.nbrCol) ? mat1.coeffs[i][j] : mat2.coeffs[i][j - mat1.nbrCol];
            	}
        	}
        	return mat;
    	}
	}

    
	public Matrice subLignes(int nMin, int nMax){
    	if(nMin < 0 || nMax >= nbrLig){
        	throw new Error("erreur sur nMin ou nMax"); // gestion de l'erreur : le nombre de lignes est impossible
    	} else {
        	Matrice mat = new Matrice(nMax - nMin + 1, nbrCol);
        	for (int i = 0; i < mat.nbrLig; i++) {
            	for (int j = 0; j < mat.nbrCol; j++) {
                	System.out.println(i + " et " +j); // coeff a(23) "2 et 3" Pas compris ca A
                	mat.coeffs[i][j] = coeffs[nMin + i][j];
            	}
        	}
        	return mat;
    	}
	}

	public Matrice subCol(int nMin, int nMax){
    	if(nMin < 0 || nMax >= nbrCol){
        	throw new Error("erreur sur nMin ou nMax");  // gestion de l'erreur : le nombre de colonnes est impossible
    	} else {
        	Matrice mat = new Matrice(nbrLig, nMax - nMin + 1);
        	for (int i = 0; i < mat.nbrLig; i++) {
            	for (int j = 0; j < mat.nbrCol; j++) {
                	mat.coeffs[i][j] = coeffs[i][nMin + j]; // idem avec les colonnes De meme pas compris A
            	}
        	}
        	return mat;
    	}
	}

	public Matrice transposee() {   // cette méthode permet de transposer une matrice : colonnes <-> lignes
    	Matrice mat = new Matrice(nbrCol, nbrLig);
    	for (int i = 0; i < mat.nbrCol; i++) {
        	for (int j = 0; j < nbrLig; j++) {
            	mat.coeffs[i][j] = coeffs[j][i]; // ex : on échange a(23) et a(32) Pas sur que ca fonctionne A
        	}
    	}
    	return mat;
	}

	public Matrice metAuCarre()  {
    	return concatCol(this.concatLig(identite(nbrCol)), identite(nbrLig).concatLig(this.transposee()));
	}

	public static int intAlea(int bmin, int bmax){
    	return (int)(bmin + Math.random()*(bmax-bmin));
	}

//                   	A D D I T I O N
	public Matrice add(Matrice mat2){
    	if(this.nbrLig != mat2.nbrLig || this.nbrCol != mat2.nbrCol){ // comparons le nbrLig et le nbrCol pour +
        	throw new Error("matrices de tailles différentes"); // considère l'erreur : TaillesMatrices !=
    	}
    	else {
        	double[][] coeffs = new double[this.nbrLig][this.nbrCol];
        	for (int i = 0; i < coeffs.length; i++) {
            	for (int j = 0; j < coeffs[i].length; j++) {
                	coeffs[i][j] = this.coeffs[i][j] + mat2.coeffs[i][j]; // on ajoute simplement les coefficients
            	}
        	}
        	return new Matrice(this.nbrLig, this.nbrCol, coeffs);
    	}

	}

	public Matrice opp(){       	// pas d'erreur car une seule matrice(opposé)
    	double[][] coeffs = new double[this.nbrCol][this.nbrLig];
    	for (int i = 0; i < coeffs.length; i++) {
        	for (int j = 0; j < coeffs[i].length; j++) {
            	coeffs[i][j] = -this.coeffs[i][j]; // qui à a(23) associe -a(23)
        	}
    	}
    	return new Matrice(this.nbrLig, this.nbrCol, coeffs);
	}

	public Matrice moins(Matrice mat2){
    	return this.add(mat2.opp());	// différence = addition de l'opposé
	}
//              	M U L T I P L I C A T I O N
	public Matrice mult(Matrice mat2){
    	if (this.nbrCol != mat2.nbrLig) throw new Error("erreur de dimension"); // erreur selon la formule, (et la mult ne commute pas)
    	Matrice out = new Matrice(this.nbrLig, mat2.nbrCol);
    	for (int i = 0; i < out.nbrLig; i++) {      	// lignes par lignes de M1
        	for (int j = 0; j < out.nbrCol; j++) {  	// colonnes par colonnes de M2
            	double coeff = 0;                   	// initialisation de coeffs
            	for (int k = 0; k < this.nbrCol; k++) {
                	coeff+= this.coeffs[i][k] * mat2.coeffs[k][j]; // formule de multiplication de deux matrices
            	}
            	out.coeffs[i][j] = coeff;
        	}
    	}
    	return out;
	}

	public static Matrice test3() {
    	return matTest1().add(matTest1().mult(matTest1()));
	}

	public int permuteLigne(int l1, int l2) { // on permute les lignes nommées L1 et L2
    	double[][] newCoeffs = new double[nbrLig][nbrCol];
    	for (int i = 0; i < coeffs.length; i++) {
        	for (int j = 0; j < coeffs[i].length; j++) {
            	newCoeffs[i][j] = coeffs[i][j];   // newCoeff permet de stocker un coeff le temps d'échanger
                                              	// ex : A <- B ; C <- A et B <- C pour A <-> B
        	}
    	}
    	boolean egaux = true;               	// les coefficients sont égaux

    	for (int i = 0; i < nbrCol; i++) {

        	if(coeffs[l1][i] != coeffs[l2][i]){
            	egaux = false;              	// les coefficients sont différents
        	}
        	newCoeffs[l2][i] = coeffs[l1][i];  //on échange ici
        	newCoeffs[l1][i] = coeffs[l2][i];
    	}

    	if (egaux) return 1;    	// on ne change rien, puisqu'ils sont identiques.
    	else {
        	coeffs = newCoeffs; 	// échangeons
        	return -1;
    	}
	}

	public void transvection(int l1, int l2) {      	// opérations élementaires sur les lignes / colonnes a regarder trop fatigué A
    	if (l1 > nbrCol) throw new Error("l1 > nbrCol");      	// impossible
    	if (coeffs[l1][l1] == 0.0) throw new Error("Ml1 l1 = 0"); // si le coeff est nul.

    	double p = coeffs[l2][l1] / coeffs[l1][l1]; // opération

    	for (int i = 0; i < nbrCol; i++) {
        	if (i == l1) coeffs[l2][l1] = 0.0;  // si égalité : on annule le coeff (grande avancée dans la méthode)
        	else coeffs[l2][i] = coeffs[l2][i] - p * coeffs[l1][i];
    	}
	}

	public int lignePlusGrandPivot(int e) { // maitenant, on introduit le PIVOT a regarder trop fatigué A
    	if(e >= nbrCol || e >= nbrLig ) throw new Error("e trop grand"); // si e est > à au moins une dimension de la matrice
    	double max = 0.0; // initialisation
    	int maxI = 0; 	// initialisation
    	for (int i = e; i < nbrLig; i++) {
        	if( max < Math.abs(coeffs[i][e])) {
            	max = Math.abs(coeffs[i][e]); // il prend sa valeur.
            	maxI = i;                 	// indice de la ligne
        	}
    	}
    	if(max > Math.pow(10, -8.0)) return maxI; // tolérance à 0,00000001
    	else return -1;
	}
// ---------- D E S C E N T E   D E	G A U S S ----------------
	/*a regarder trop fatigué A
	Un petit point algèbre s'impose :
	définition d'une signature :
	- elle vaut 1 si le nombre d'inversions est pair.
	- elle vaut -1 si le nombre d'inversions est impair.
	*/
	public ResGauss descenteGauss() {           	// trigonalisons la matrice.
    	int signature = 1;  	// initialisation la signature à 1.
    	int rang = Math.min(this.nbrCol,this.nbrLig);
    	// rang appartient forcément à [1, min(n, p)] avec n lignes et p colonnes
   	 
    	for (int i = 0; i < this.nbrLig; i++) {
        	for (int j = 0; j < this.nbrLig; j++) {
            	int lppp = lignePlusGrandPivot(j);
            	if (lppp!= -1){
                	permuteLigne(j, lppp);  // on permute la ligne et celle du plus grand pivot
            	}
            	else signature= -signature; // alors la signature est opposée
        	}
    	}

    	for (int i = 0; i < this.nbrLig; i++) {
        	for (int j =0; j< i; j++) { // échelonne.
            	if (coeffs[i][j] != 0) transvection(j, i); // ssi !=0 !!!
        	}
    	}
    	return new ResGauss(rang,signature); 	// renvoie "2, 1" si rg(Mat)=2 et nbr de permutations pair
	}
// ----------------------------------------------------------------
    
//           	D E T E R M I N A N T
	public double determinant (int signature){
    	double determinant = signature; // initialisation
    	for(int i=0; i<this.nbrLig; i++){
	/* on calcule le déterminant comme le produit de tous les termes
 	diagonaux car la matrice est triangulaire après la descente de Gauss.
	*/
        	determinant = determinant * this.coeffs[i][i]; // quand i = j
    	}
    	return(determinant);
	}

	public Matrice pivotsdiag1 (){ // rend tous les coeffs diagonaux unitaires 
    	for(int i=0; i<this.nbrCol; i++){
        	for(int j=i; j<this.nbrLig;j++){
            	double pivot = this.coeffs[i][i];
            	this.coeffs[i][j] = this.coeffs[i][j] / pivot;
        	}
    	}
    	return this;
	}

	public Matrice remonteeGauss (){
    	for (int j = 0; j< this.nbrLig; j++) { // attention : j lignes et i colonnes
        	for (int i = 0; i < j; i++) {
            	if (coeffs[i][j] != 0) transvection(j, i); // toujours ssi !=0
        	}
    	}
    	return this;
	}

	public Matrice resolution() { // on résout avec la méthode de Gauss
    	ResGauss resGauss  = descenteGauss();
    	if(determinant(resGauss.signature) == 0) return null; // matrice non-inversible <=> det(Mat)=0 (donc aucune solution)
    	pivotsdiag1();
    	return remonteeGauss();
	}

	public static void main(String[] args) {

    	double[][] coeffs = {{0,1,2,1},{3,-4,5,2}, {6,7,-8,3}};
    	Matrice lamatrice = new Matrice(3,4, coeffs);
        
    	// on affiche tout ce qui a été utilisé
    	/*System.out.println(lamatrice.descenteGauss());

    	System.out.println("matrice avec la descente");
    	System.out.println(lamatrice);


    	System.out.println("pivots a un :");
    	System.out.println(lamatrice.pivotsdiag1());
    	//System.out.println(lamatrice);

    	System.out.println("matrice inversée :");
    	System.out.println(lamatrice.remonteeGauss());
    	//System.out.println(lamatrice);
*/
	}
}
