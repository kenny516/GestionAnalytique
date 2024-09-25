package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PartsParCentre {
	private int id;
	private int idRubrique;
	private int idCentre;
	private java.math.BigDecimal valeur;
	private java.sql.Date dateInsertion;

	// Constructeurs
	public PartsParCentre() {}

	public PartsParCentre(int id, int idRubrique, int idCentre, java.math.BigDecimal valeur, java.sql.Date dateInsertion) {
			this.id = id;
			this.idRubrique = idRubrique;
			this.idCentre = idCentre;
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
			return idRubrique;
	}

	public void setIdRubrique(int idRubrique) {
			this.idRubrique = idRubrique;
	}

	public int getIdCentre() {
			return idCentre;
	}

	public void setIdCentre(int idCentre) {
			this.idCentre = idCentre;
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

	// Méthodes pour CRUD
	public void save(Connection c) throws Exception {
			PreparedStatement ps = c.prepareStatement("INSERT INTO PartsParCentre (idRubrique, idCentre, valeur, dateInsertion) VALUES (?, ?, ?, ?)");
			ps.setInt(1, getIdRubrique());
			ps.setInt(2, getIdCentre());
			ps.setBigDecimal(3, getValeur());
			ps.setDate(4, getDateInsertion());
			ps.executeUpdate();
			ps.close();
	}

	public void update(Connection c) throws Exception {
			PreparedStatement ps = c.prepareStatement("UPDATE PartsParCentre SET idRubrique = ?, idCentre = ?, valeur = ?, dateInsertion = ? WHERE id = ?");
			ps.setInt(1, getIdRubrique());
			ps.setInt(2, getIdCentre());
			ps.setBigDecimal(3, getValeur());
			ps.setDate(4, getDateInsertion());
			ps.setInt(5, getId());
			ps.executeUpdate();
			ps.close();
	}

	public void delete(Connection c) throws Exception {
			PreparedStatement ps = c.prepareStatement("DELETE FROM PartsParCentre WHERE id = ?");
			ps.setInt(1, getId());
			ps.executeUpdate();
			ps.close();
	}

	public void getById(Connection c, int id) throws Exception {
			PreparedStatement ps = c.prepareStatement("SELECT * FROM PartsParCentre WHERE id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
					this.id = rs.getInt("id");
					this.idRubrique = rs.getInt("idRubrique");
					this.idCentre = rs.getInt("idCentre");
					this.valeur = rs.getBigDecimal("valeur");
					this.dateInsertion = rs.getDate("dateInsertion");
			}

			rs.close();
			ps.close();
	}
}



