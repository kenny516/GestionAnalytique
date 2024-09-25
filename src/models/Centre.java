package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Centre {
	private int id;
	private String nom;
	private int idNature;

	// Constructeurs
	public Centre() {
	}

	public Centre(int id, String nom, int idNature) {
		this.id = id;
		this.nom = nom;
		this.idNature = idNature;
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

	public int getIdNature() {
		return idNature;
	}

	public void setIdNature(int idNature) {
		this.idNature = idNature;
	}

	// Méthodes pour CRUD
	public void save(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("INSERT INTO Centre (nom, idNature) VALUES (?, ?)");
		ps.setString(1, getNom());
		ps.setInt(2, getIdNature());
		ps.executeUpdate();
		ps.close();
	}

	public void update(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("UPDATE Centre SET nom = ?, idNature = ? WHERE id = ?");
		ps.setString(1, getNom());
		ps.setInt(2, getIdNature());
		ps.setInt(3, getId());
		ps.executeUpdate();
		ps.close();
	}

	public void delete(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("DELETE FROM Centre WHERE id = ?");
		ps.setInt(1, getId());
		ps.executeUpdate();
		ps.close();
	}

	public void getById(Connection c, int id) throws Exception {
		PreparedStatement ps = c.prepareStatement("SELECT * FROM Centre WHERE id = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			this.id = rs.getInt("id");
			this.nom = rs.getString("nom");
			this.idNature = rs.getInt("idNature");
		}

		rs.close();
		ps.close();
	}
}