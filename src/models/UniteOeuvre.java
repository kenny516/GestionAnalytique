package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class UniteOeuvre {
	private int id;
	private String nom;

	// Constructeurs
	public UniteOeuvre() {
	}

	public UniteOeuvre(int id, String nom) {
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

	public static ArrayList<UniteOeuvre> getAll(Connection c) throws Exception {
		ArrayList<UniteOeuvre> UniteOeuvres = new ArrayList<>();
		PreparedStatement ps = c.prepareStatement("SELECT * FROM UniteOeuvre");
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			UniteOeuvre UniteOeuvre = new UniteOeuvre();
			UniteOeuvre.setId(rs.getInt("id"));
			UniteOeuvre.setNom(rs.getString("nom"));
			UniteOeuvres.add(UniteOeuvre);
		}

		rs.close();
		ps.close();
		return UniteOeuvres;
	}

	// Méthodes pour CRUD
	public void saveNoError(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("INSERT INTO UniteOeuvre (nom) VALUES (?)");
		ps.setString(1, getNom());
		ps.executeUpdate();
		ps.close();
	}

	public void updateNoError(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("UPDATE UniteOeuvre SET nom = ? WHERE id = ?");
		ps.setString(1, getNom());
		ps.setInt(2, getId());
		ps.executeUpdate();
		ps.close();
	}

	public void deleteNoError(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("DELETE FROM UniteOeuvre WHERE id = ?");
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
		PreparedStatement ps = c.prepareStatement("SELECT * FROM UniteOeuvre WHERE id = ?");
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
