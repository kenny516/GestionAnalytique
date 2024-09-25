package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class NatureCentre {
	private int id;
	private String nom;

	// Constructeurs
	public NatureCentre() {}

	public NatureCentre(int id, String nom) {
			this.id = id;
			this.nom = nom;
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

	// Méthodes pour CRUD
	public void save(Connection c) throws Exception {
			PreparedStatement ps = c.prepareStatement("INSERT INTO NatureCentre (nom) VALUES (?)");
			ps.setString(1, getNom());
			ps.executeUpdate();
			ps.close();
	}

	public void update(Connection c) throws Exception {
			PreparedStatement ps = c.prepareStatement("UPDATE NatureCentre SET nom = ? WHERE id = ?");
			ps.setString(1, getNom());
			ps.setInt(2, getId());
			ps.executeUpdate();
			ps.close();
	}

	public void delete(Connection c) throws Exception {
			PreparedStatement ps = c.prepareStatement("DELETE FROM NatureCentre WHERE id = ?");
			ps.setInt(1, getId());
			ps.executeUpdate();
			ps.close();
	}

	public void getById(Connection c, int id) throws Exception {
			PreparedStatement ps = c.prepareStatement("SELECT * FROM NatureCentre WHERE id = ?");
			ps.setInt(1, id);
			ResultSet rs = ps.executeQuery();

			if (rs.next()) {
					this.id = rs.getInt("id");
					this.nom = rs.getString("nom");
			}

			rs.close();
			ps.close();
	}
}

