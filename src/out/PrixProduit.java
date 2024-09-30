package out;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import models.AdministrationAnalytique;
import models.Centre;
import models.Production;
import models.Produit;

public class PrixProduit {
	Produit sujet;
	List<Production> prods;
	HashMap<Centre, Double> depenseParCentre;

	public PrixProduit(AdministrationAnalytique aa, Connection c, int idProduit) throws Exception {
		setSujet(c, idProduit);
		setProds(Production.getByPeriodByProduit(c, idProduit, aa.getDateDebut(), aa.getDateFin()));
		setDepenseParCentre(aa.getTotalDepenseParCentre(c));
		
		setDepenseParCentreNonAdministratif("Centre1");
		setQuantite();
		setPrix_unitaire();
	}

	public static List<PrixProduit> getPrixProduits(AdministrationAnalytique aa, Connection c) throws Exception{
		List<PrixProduit> pp = new ArrayList<>();

		List<Produit> produits = new Produit().getAll(c);
    for (Produit produit : produits) {
      pp.add(new PrixProduit(aa, c, produit.getId()));
    }

		return pp;
	}

	// done
	HashMap<Centre, Double> depenseParCentreNonAdministratif;

	//done
	double cout_non_admin;
	double cout_admin;

	// done
	double quantite;

	double prix_unitaire;

	public void setSujet(Connection c, int id) throws Exception {
		this.sujet = new Produit();
		this.sujet.getById(c, id);
	}

	public Produit getSujet() {
		return sujet;
	}

	public double getQuantite() {
		return quantite;
	}

	public void setQuantite(){
		double quantite = 0;
		for (Production production : getProds()) {
			quantite += production.getQuantite().doubleValue();
		}
		setQuantite(quantite);
	}

	public void setQuantite(double quantite) {
		this.quantite = quantite;
	}

	public List<Production> getProds() {
		return prods;
	}

	public void setProds(List<Production> prods) {
		this.prods = prods;
	}

	public HashMap<Centre, Double> getDepenseParCentre() {
		return depenseParCentre;
	}

	public void setDepenseParCentre(HashMap<Centre, Double> depenseParCentre) {
		this.depenseParCentre = depenseParCentre;
	}

	public double getPrix_unitaire() {
		return prix_unitaire;
	}

	public void setPrix_unitaire(double prix_unitaire) {
		this.prix_unitaire = prix_unitaire;
	}

	public void setPrix_unitaire() {
		double prix_unitaire = (getCout_non_admin()+getCout_admin())/getQuantite();
		this.prix_unitaire = prix_unitaire;
	}

	public HashMap<Centre, Double> getDepenseParCentreNonAdministratif() {
		return depenseParCentreNonAdministratif;
	}

	public void setDepenseParCentreNonAdministratif(String administratif) {
		HashMap<Centre, Double> result = (HashMap<Centre, Double>) getDepenseParCentre().clone();
		double cout_non_admin = 0;
		double cout_admin = 0;

		Centre alana = new Centre();
		for (Centre c : result.keySet()) {
			if (c.getNom().equals(administratif)) {
				alana= c;
			} else {
				cout_non_admin += result.get(c);
			}
		}
		cout_admin = result.remove(alana);

		setCout_admin(cout_admin);
		setCout_non_admin(cout_non_admin);

		for (Centre c : result.keySet()) {
			double proportion = result.get(c)/cout_non_admin;
			double nouv_valeur = proportion*cout_admin+result.get(c);
			result.put(c, nouv_valeur);
		}

		this.setDepenseParCentreNonAdministratif(result);

	}

	public void setSujet(Produit sujet) {
		this.sujet = sujet;
	}

	public void setDepenseParCentreNonAdministratif(HashMap<Centre, Double> depenseParCentreNonAdministratif) {
		this.depenseParCentreNonAdministratif = depenseParCentreNonAdministratif;
	}

	public double getCout_non_admin() {
		return cout_non_admin;
	}

	public void setCout_non_admin(double totalCoutNonAdministratif) {
		this.cout_non_admin = totalCoutNonAdministratif;
	}

	public double getCout_admin() {
		return cout_admin;
	}

	public void setCout_admin(double cout_admin) {
		this.cout_admin = cout_admin;
	}

}
