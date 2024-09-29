package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 * Contient des cles etrangeres , voir setCleEtrangere(Connection c) pour les regles
 */

public class PartsParCentre {
	private int id;
	private Rubrique rubrique;
	private Centre centre;
	private java.math.BigDecimal valeur;
	private java.sql.Date dateInsertion;

	// Constructeurs
	public PartsParCentre() {
	}

	public PartsParCentre(int id, int idRubrique, int idCentre, java.math.BigDecimal valeur,java.sql.Date dateInsertion, Connection c) throws Exception {
		this.id = id;
		setRubrique(idRubrique);
		setCentre(idCentre);
		this.valeur = valeur;
		this.dateInsertion = dateInsertion;
		setRubrique(c);
		setCentre(c);
	}

	public PartsParCentre(int id, int idRubrique, int idCentre, java.math.BigDecimal valeur,java.sql.Date dateInsertion) throws Exception {
		this.id = id;
		setRubrique(idRubrique);
		setCentre(idCentre);
		this.valeur = valeur;
		this.dateInsertion = dateInsertion;
	}

	// Getters et Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdRubrique() {
		return rubrique.getId();
	}

	public Rubrique getRubrique() {
		return rubrique;
	}

	public void setRubrique(int idRubrique) throws Exception{
		this.rubrique = new Rubrique();
		rubrique.setId(idRubrique);
	}

	/**
	 * TODO : gestion rubrique dans parts
	 * @param c
	 * @throws Exception
	 * 
	 * rubrique.part[i].rubrique = null
	 * rubrique.depense[i].rubrique = null
	 */
	public void setRubrique(Connection c) throws Exception{
		rubrique.getByIdAll(c, getIdRubrique());
	}

	public int getIdCentre() {
		return centre.getId();
	}

	public Centre getCentre() {
		return centre;
	}

	public void setCentre(int idCentre){
		this.centre = new Centre();
		this.centre.setId(idCentre);
	}

	public void setCentre(Connection c) throws Exception {
		centre.getByIdAll(c, getIdCentre());
	}

	public java.math.BigDecimal getValeur() {
		return valeur;
	}

	public void setValeur(java.math.BigDecimal valeur) {
		this.valeur = valeur;
	}

	public java.sql.Date getDateInsertion() {
		return dateInsertion;
	}

	public void setDateInsertion(java.sql.Date dateInsertion) {
		this.dateInsertion = dateInsertion;
	}

	public List<PartsParCentre> getAll(Connection c) throws Exception {
		List<PartsParCentre> partsParCentre = new ArrayList<>();
		PreparedStatement ps = c.prepareStatement("select * from PartsParCentre");
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			PartsParCentre parts = new PartsParCentre();
			parts.setId(rs.getInt("id"));
			parts.setRubrique(rs.getInt("idRubrique"));
			parts.setCentre(rs.getInt("idCentre"));
			parts.setValeur(rs.getBigDecimal("valeur"));
			parts.setDateInsertion(rs.getDate("dateInsertion"));
		}

		rs.close();
		ps.close();

		return partsParCentre;
	}

	// Méthodes pour CRUD
	public void saveNoError(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement(
				"INSERT INTO PartsParCentre (idRubrique, idCentre, valeur, dateInsertion) VALUES (?, ?, ?, ?)");
		ps.setInt(1, getIdRubrique());
		ps.setInt(2, getIdCentre());
		ps.setBigDecimal(3, getValeur());
		ps.setDate(4, getDateInsertion());
		ps.executeUpdate();
		ps.close();
	}

	public void updateNoError(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement(
				"UPDATE PartsParCentre SET idRubrique = ?, idCentre = ?, valeur = ?, dateInsertion = ? WHERE id = ?");
		ps.setInt(1, getIdRubrique());
		ps.setInt(2, getIdCentre());
		ps.setBigDecimal(3, getValeur());
		ps.setDate(4, getDateInsertion());
		ps.setInt(5, getId());
		ps.executeUpdate();
		ps.close();
	}

	public void deleteNoError(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("DELETE FROM PartsParCentre WHERE id = ?");
		ps.setInt(1, getId());
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
		PreparedStatement ps = c.prepareStatement("SELECT * FROM PartsParCentre WHERE id = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			this.id = rs.getInt("id");
			this.setRubrique(rs.getInt("idRubrique"));
			setCentre(rs.getInt("idCentre"));
			this.valeur = rs.getBigDecimal("valeur");
			this.dateInsertion = rs.getDate("dateInsertion");
		}

		rs.close();
		ps.close();
	}

	public void getByIdAll(Connection c , int id) throws Exception {
		getById(c, id);
		setCentre(c);
		setRubrique(c);
	}
}
