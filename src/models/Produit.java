package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class Produit {
	private int id;
	private String nom;
	private UniteOeuvre uOeuvre;

	// Constructeurs
	public Produit() {
	}

	public Produit(int id, String nom, int idUniteOeuvre, Connection c) throws Exception {
		this.id = id;
		this.nom = nom;
		setUniteOeuvre(idUniteOeuvre, c);
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
		return uOeuvre.getId();
	}

	public UniteOeuvre getuOeuvre() {
		return uOeuvre;
	}

	public void setUniteOeuvre(int idUniteOeuvre, Connection c) throws Exception {
		this.uOeuvre = new UniteOeuvre();
		if (c != null) {
			uOeuvre.getById(c, idUniteOeuvre);
			return;
		}
		uOeuvre.setId(idUniteOeuvre);
	}

	public List<Produit> getAll(Connection c) throws Exception {
		List<Produit> produits = new ArrayList<>();
		PreparedStatement ps = c.prepareStatement("SELECT * FROM Produit");
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Produit p = new Produit();
			p.setId(rs.getInt("id"));
			p.setNom(rs.getString("nom"));
			p.setUniteOeuvre(rs.getInt("idUniteOeuvre"), c);

			produits.add(p);
		}

		rs.close();
		ps.close();

		return produits;
	}

	// Méthodes pour CRUD
	public void saveNoError(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("INSERT INTO Produit (nom, idUniteOeuvre) VALUES (?, ?)");
		ps.setString(1, getNom());
		ps.setInt(2, getIdUniteOeuvre());
		ps.executeUpdate();
		ps.close();
	}

	public void updateNoError(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("UPDATE Produit SET nom = ?, idUniteOeuvre = ? WHERE id = ?");
		ps.setString(1, getNom());
		ps.setInt(2, getIdUniteOeuvre());
		ps.setInt(3, getId());
		ps.executeUpdate();
		ps.close();
	}

	public void deleteNoError(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("DELETE FROM Produit WHERE id = ?");
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
		PreparedStatement ps = c.prepareStatement("SELECT * FROM Produit WHERE id = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			this.id = rs.getInt("id");
			this.nom = rs.getString("nom");
			setUniteOeuvre(rs.getInt("idUniteOeuvre"), c);
		}

		rs.close();
		ps.close();
	}
}
