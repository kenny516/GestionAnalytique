package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Production {
	private int id;
	private int idProduit;
	private java.sql.Date date;
	private java.math.BigDecimal quantite;

	// Constructeurs
	public Production() {
	}

	public Production(int id, int idProduit, java.sql.Date date, java.math.BigDecimal quantite) {
		this.id = id;
		this.idProduit = idProduit;
		this.date = date;
		this.quantite = quantite;
	}

	// Getters et Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdProduit() {
		return idProduit;
	}

	public void setIdProduit(int idProduit) {
		this.idProduit = idProduit;
	}

	public java.sql.Date getDate() {
		return date;
	}

	public void setDate(java.sql.Date date) {
		this.date = date;
	}

	public java.math.BigDecimal getQuantite() {
		return quantite;
	}

	public void setQuantite(java.math.BigDecimal quantite) {
		this.quantite = quantite;
	}

	// Méthodes pour CRUD
	public void save(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("INSERT INTO Production (idProduit, date, quantite) VALUES (?, ?, ?)");
		ps.setInt(1, getIdProduit());
		ps.setDate(2, getDate());
		ps.setBigDecimal(3, getQuantite());
		ps.executeUpdate();
		ps.close();
	}

	public void update(Connection c) throws Exception {
		PreparedStatement ps = c
				.prepareStatement("UPDATE Production SET idProduit = ?, date = ?, quantite = ? WHERE id = ?");
		ps.setInt(1, getIdProduit());
		ps.setDate(2, getDate());
		ps.setBigDecimal(3, getQuantite());
		ps.setInt(4, getId());
		ps.executeUpdate();
		ps.close();
	}

	public void delete(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("DELETE FROM Production WHERE id = ?");
		ps.setInt(1, getId());
		ps.executeUpdate();
		ps.close();
	}

	public void getById(Connection c, int id) throws Exception {
		PreparedStatement ps = c.prepareStatement("SELECT * FROM Production WHERE id = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			this.id = rs.getInt("id");
			this.idProduit = rs.getInt("idProduit");
			this.date = rs.getDate("date");
			this.quantite = rs.getBigDecimal("quantite");
		}

		rs.close();
		ps.close();
	}
}
