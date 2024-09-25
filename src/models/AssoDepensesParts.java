package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AssoDepensesParts {
	private int id;
	private int idDepense;
	private int idParts;

	// Constructeurs
	public AssoDepensesParts() {
	}

	public AssoDepensesParts(int id, int idDepense, int idParts) {
		this.id = id;
		this.idDepense = idDepense;
		this.idParts = idParts;
	}

	// Getters et Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIdDepense() {
		return idDepense;
	}

	public void setIdDepense(int idDepense) {
		this.idDepense = idDepense;
	}

	public int getIdParts() {
		return idParts;
	}

	public void setIdParts(int idParts) {
		this.idParts = idParts;
	}

	// Méthodes pour CRUD
	public void save(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("INSERT INTO AssoDepensesParts (idDepense, idParts) VALUES (?, ?)");
		ps.setInt(1, getIdDepense());
		ps.setInt(2, getIdParts());
		ps.executeUpdate();
		ps.close();
	}

	public void update(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("UPDATE AssoDepensesParts SET idDepense = ?, idParts = ? WHERE id = ?");
		ps.setInt(1, getIdDepense());
		ps.setInt(2, getIdParts());
		ps.setInt(3, getId());
		ps.executeUpdate();
		ps.close();
	}

	public void delete(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("DELETE FROM AssoDepensesParts WHERE id = ?");
		ps.setInt(1, getId());
		ps.executeUpdate();
		ps.close();
	}

	public void getById(Connection c, int id) throws Exception {
		PreparedStatement ps = c.prepareStatement("SELECT * FROM AssoDepensesParts WHERE id = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			this.id = rs.getInt("id");
			this.idDepense = rs.getInt("idDepense");
			this.idParts = rs.getInt("idParts");
		}

		rs.close();
		ps.close();
	}
}
