package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Depenses {
	private int id;
	private String nom;
	private java.sql.Date date;
	private double montant;

	// Constructeurs
	public Depenses() {
	}

	public Depenses(int id, String nom, java.sql.Date date, double montant) {
		this.id = id;
		this.nom = nom;
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

	public String getNom() {
		return nom;
	}

	public void setNom(String nom) {
		this.nom = nom;
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

	// Méthodes pour CRUD
	public void save(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("INSERT INTO Depenses (nom, date, montant) VALUES (?, ?, ?)");
		ps.setString(1, getNom());
		ps.setDate(2, getDate());
		ps.setDouble(3, getMontant());
		ps.executeUpdate();
		ps.close();
	}

	public void update(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("UPDATE Depenses SET nom = ?, date = ?, montant = ? WHERE id = ?");
		ps.setString(1, getNom());
		ps.setDate(2, getDate());
		ps.setDouble(3, getMontant());
		ps.setInt(4, getId());
		ps.executeUpdate();
		ps.close();
	}

	public void delete(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("DELETE FROM Depenses WHERE id = ?");
		ps.setInt(1, getId());
		ps.executeUpdate();
		ps.close();
	}

	public void getById(Connection c, int id) throws Exception {
		PreparedStatement ps = c.prepareStatement("SELECT * FROM Depenses WHERE id = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			this.id = rs.getInt("id");
			this.nom = rs.getString("nom");
			this.date = rs.getDate("date");
			this.montant = rs.getDouble("montant");
		}

		rs.close();
		ps.close();
	}
}
