package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Depenses {
	private int id;
	private java.sql.Date date;
	private double montant;

	private Rubrique rubrique;
	private List<PartsParCentre> pCentre;

	// Constructeurs
	public Depenses() {
	}

	public Depenses(int id, java.sql.Date date, double montant) {
		this.id = id;
		this.date = date;
		this.montant = montant;
	}

	// Getters et Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public java.sql.Date getDate() {
		return date;
	}

	public void setDate(java.sql.Date date) {
		this.date = date;
	}

	public double getMontant() {
		return montant;
	}

	public void setMontant(double montant) {
		this.montant = montant;
	}

	public static List<Depenses> getAll(Connection c) throws Exception {
		List<Depenses> depenses = new ArrayList<>();
		PreparedStatement ps = c.prepareStatement("SELECT * FROM Depenses");
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Depenses depense = new Depenses();
			depense.setId(rs.getInt("id"));
			depense.setDate(rs.getDate("dateDepense"));
			depense.setMontant(rs.getDouble("montant"));
			depenses.add(depense);
		}

		rs.close();
		ps.close();
		return depenses;
	}

	// Méthodes pour CRUD
	public void saveNoError(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("INSERT INTO Depenses (dateDepense, montant) VALUES (?, ?, ?)");
		ps.setDate(2, getDate());
		ps.setDouble(3, getMontant());
		ps.executeUpdate();
		ps.close();
	}

	public void save(Connection c) throws Exception {
		try {
			c.setAutoCommit(false);
			saveNoError(c);
			c.commit();
		} catch (Exception e) {
			c.rollback();
			throw e;
		}
	}

	public void save(Connection c,List<PartsParCentre> pc) throws Exception {
		try {
			c.setAutoCommit(false);

			saveNoError(c);
			for (PartsParCentre partsParCentre : pc) {
				AssoDepensesParts p = new AssoDepensesParts(0, getId(),partsParCentre.getId());
				p.saveNoError(c);
			}

			c.commit();
		} catch (Exception e) {
			c.rollback();
		}
	} 

	public void updateNoError(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("UPDATE Depenses SET dateDepense = ?, montant = ? WHERE id = ?");
		ps.setDate(2, getDate());
		ps.setDouble(3, getMontant());
		ps.setInt(4, getId());
		ps.executeUpdate();
		ps.close();
	}

	public void update(Connection c) throws Exception {
		try {
			c.setAutoCommit(false);
			updateNoError(c);
			c.commit();
		} catch (Exception e) {
			c.rollback();
			throw e;
		}
	}

	public void deleteNoError(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("DELETE FROM Depenses WHERE id = ?");
		ps.setInt(1, getId());
		ps.executeUpdate();
		ps.close();
	}

	public void delete(Connection c) throws Exception {
		try {
			c.setAutoCommit(false);
			deleteNoError(c);
			c.commit();
		} catch (Exception e) {
			c.rollback();
			throw e;
		}
	}

	public void getById(Connection c, int id) throws Exception {
		PreparedStatement ps = c.prepareStatement("SELECT * FROM Depenses WHERE id = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			this.id = rs.getInt("id");
			this.date = rs.getDate("dateDepense");
			this.montant = rs.getDouble("montant");
		}

		rs.close();
		ps.close();
	}

	public List<PartsParCentre> getpCentre() {
		return pCentre;
	}

	public void setpCentre(List<PartsParCentre> pCentre) {
		this.pCentre = pCentre;
	}

	public void setpCentre(Connection c) throws Exception {
		List<PartsParCentre> partsParCentre = new ArrayList<>();
		PreparedStatement ps = c
				.prepareStatement("select * from PartsParCentre where id in (select idPart from AssoDepensesParts where idDepense = ?)");
		ps.setInt(1, getId());
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			PartsParCentre parts = new PartsParCentre();
			parts.setId(rs.getInt("id"));
			parts.setRubrique(rs.getInt("idRubrique"));

			parts.setCentre(rs.getInt("idCentre"));
			parts.setCentre(c);

			parts.setValeur(rs.getBigDecimal("valeur"));
			parts.setDateInsertion(rs.getDate("dateInsertion"));
		}

		rs.close();
		ps.close();

		setpCentre(partsParCentre);
	}

	public void setRubrique(Connection c) throws Exception{
		this.rubrique = new Rubrique();
		this.rubrique.getById(c, id);
		getRubrique().setpCentre(c);
	}
	public Rubrique getRubrique() {
		return rubrique;
	}

}
