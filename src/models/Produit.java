package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Produit {
	private int id;
	private String nom;
	private int idUniteOeuvre;

	// Constructeurs
	public Produit() {}

	public Produit(int id, String nom, int idUniteOeuvre) {
			this.id = id;
			this.nom = nom;
			this.idUniteOeuvre = idUniteOeuvre;
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

	public int getIdUniteOeuvre() {
			return idUniteOeuvre;
	}

	public void setIdUniteOeuvre(int idUniteOeuvre) {
			this.idUniteOeuvre = idUniteOeuvre;
	}

	// Méthodes pour CRUD
	public void save(Connection c) throws Exception {
			PreparedStatement ps = c.prepareStatement("INSERT INTO Produit (nom, idUniteOeuvre) VALUES (?, ?)");
			ps.setString(1, getNom());
			ps.setInt(2, getIdUniteOeuvre());
			ps.executeUpdate();
			ps.close();
	}

	public void update(Connection c) throws Exception {
			PreparedStatement ps = c.prepareStatement("UPDATE Produit SET nom = ?, idUniteOeuvre = ? WHERE id = ?");
			ps.setString(1, getNom());
			ps.setInt(2, getIdUniteOeuvre());
			ps.setInt(3, getId());
			ps.executeUpdate();
			ps.close();
	}

	public void delete(Connection c) throws Exception {
			PreparedStatement ps = c.prepareStatement("DELETE FROM Produit WHERE id = ?");
			ps.setInt(1, getId());
			ps.executeUpdate();
			ps.close();
	}

	public void getById(Connection c, int id) throws Exception {
			PreparedStatement ps = c.prepareStatement("SELECT * FROM Produit WHERE id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
					this.id = rs.getInt("id");
					this.nom = rs.getString("nom");
					this.idUniteOeuvre = rs.getInt("idUniteOeuvre");
			}

			rs.close();
			ps.close();
	}
}

