package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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

	public static List<AssoDepensesParts> getAll(Connection c) throws Exception {
		List<AssoDepensesParts> assoDepensesPartsList = new ArrayList<>();
		PreparedStatement ps = c.prepareStatement("SELECT * FROM AssoDepensesParts");
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			AssoDepensesParts assoDepensesParts = new AssoDepensesParts();
			assoDepensesParts.setId(rs.getInt("id"));
			assoDepensesParts.setIdDepense(rs.getInt("idDepense"));
			assoDepensesParts.setIdParts(rs.getInt("idPart"));
			assoDepensesPartsList.add(assoDepensesParts);
		}

		rs.close();
		ps.close();
		return assoDepensesPartsList;
	}

	// Méthodes pour CRUD
	public void saveNoError(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("INSERT INTO AssoDepensesParts (idDepense, idPart) VALUES (?, ?)");
		ps.setInt(1, getIdDepense());
		ps.setInt(2, getIdParts());
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

	public void updateNoError(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("UPDATE AssoDepensesParts SET idDepense = ?, idPart = ? WHERE id = ?");
		ps.setInt(1, getIdDepense());
		ps.setInt(2, getIdParts());
		ps.setInt(3, getId());
		ps.executeUpdate();
		ps.close();
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

	public void deleteNoError(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("DELETE FROM AssoDepensesParts WHERE id = ?");
		ps.setInt(1, getId());
		ps.executeUpdate();
		ps.close();
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
		PreparedStatement ps = c.prepareStatement("SELECT * FROM AssoDepensesParts WHERE id = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			this.id = rs.getInt("id");
			this.idDepense = rs.getInt("idDepense");
			this.idParts = rs.getInt("idPart");
		}

		rs.close();
		ps.close();
	}

	public void getByIdDepenseIdParts(Connection c, int idDep, int idP) throws Exception {
		PreparedStatement ps = c.prepareStatement("SELECT * FROM AssoDepensesParts WHERE idDepense = ? and idPart = ?");
		ps.setInt(1, idDep);
		ps.setInt(2, idP);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			this.id = rs.getInt("id");
			this.idDepense = rs.getInt("idDepense");
			this.idParts = rs.getInt("idPart");
		}

		rs.close();
		ps.close();
	}

	// get

	public static List<AssoDepensesParts> getByDepense(Connection c, Depenses d) throws Exception {
		List<AssoDepensesParts> result = new ArrayList<>();
		PreparedStatement ps = c.prepareStatement("SELECT * FROM AssoDepensesParts WHERE idDepense = ?");
		ps.setInt(1, d.getId());
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			AssoDepensesParts ass = new AssoDepensesParts();

			ass.setId(rs.getInt("id"));
			ass.setIdDepense(d.getId());
			ass.setIdParts(rs.getInt("idPart"));

			result.add(ass);
		}

		rs.close();
		ps.close();

		return result;
	}
}
