package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Rubrique {
	private int id;
	private String nom;
	private boolean estVariable;
	private int idUniteOeuvre;
	private java.sql.Date dateInsertion;

	// Constructeurs
	public Rubrique() {
	}

	public Rubrique(int id, String nom, boolean estVariable, int idUniteOeuvre, java.sql.Date dateInsertion) {
		this.id = id;
		this.nom = nom;
		this.estVariable = estVariable;
		this.idUniteOeuvre = idUniteOeuvre;
		this.dateInsertion = dateInsertion;
	}

	// Getters et Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public boolean isEstVariable() {
		return estVariable;
	}

	public void setEstVariable(boolean estVariable) {
		this.estVariable = estVariable;
	}

	public int getIdUniteOeuvre() {
		return idUniteOeuvre;
	}

	public void setIdUniteOeuvre(int idUniteOeuvre) {
		this.idUniteOeuvre = idUniteOeuvre;
	}

	public java.sql.Date getDateInsertion() {
		return dateInsertion;
	}

	public void setDateInsertion(java.sql.Date dateInsertion) {
		this.dateInsertion = dateInsertion;
	}

	// Méthodes pour CRUD
	public void save(Connection c) throws Exception {
		PreparedStatement ps = c
				.prepareStatement("INSERT INTO Rubrique (nom, estVariable, idUniteOeuvre, dateInsertion) VALUES (?, ?, ?, ?)");
		ps.setString(1, getNom());
		ps.setBoolean(2, isEstVariable());
		ps.setInt(3, getIdUniteOeuvre());
		ps.setDate(4, getDateInsertion());
		ps.executeUpdate();
		ps.close();
	}

	public void update(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement(
				"UPDATE Rubrique SET nom = ?, estVariable = ?, idUniteOeuvre = ?, dateInsertion = ? WHERE id = ?");
		ps.setString(1, getNom());
		ps.setBoolean(2, isEstVariable());
		ps.setInt(3, getIdUniteOeuvre());
		ps.setDate(4, getDateInsertion());
		ps.setInt(5, getId());
		ps.executeUpdate();
		ps.close();
	}

	public void delete(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("DELETE FROM Rubrique WHERE id = ?");
		ps.setInt(1, getId());
		ps.executeUpdate();
		ps.close();
	}

	public void getById(Connection c, int id) throws Exception {
		PreparedStatement ps = c.prepareStatement("SELECT * FROM Rubrique WHERE id = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			this.id = rs.getInt("id");
			this.nom = rs.getString("nom");
			this.estVariable = rs.getBoolean("estVariable");
			this.idUniteOeuvre = rs.getInt("idUniteOeuvre");
			this.dateInsertion = rs.getDate("dateInsertion");
		}

		rs.close();
		ps.close();
	}
}
