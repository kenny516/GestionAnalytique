package models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import database.Connect;
import database.QueryUtil;

public class Production {
	private int id;
	private Produit produit;
	private java.sql.Date date;
	private java.math.BigDecimal quantite;

	// Constructeurs
	public Production() {
	}

	public Production(int id, int idProduit, java.sql.Date date, java.math.BigDecimal quantite, Connection c)
			throws Exception {
		this.id = id;
		setProduit(idProduit);
		setProduit(c);
		this.date = date;
		this.quantite = quantite;
	}

	public Production(int id, int idProduit, java.sql.Date date, java.math.BigDecimal quantite) {
		setProduit(idProduit);
		setProduit(idProduit);
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
		return produit.getId();
	}

	public void setProduit(int idProduit) {
		this.produit = new Produit();
		produit.setId(idProduit);
	}

	public void setProduit(Connection c) throws Exception {
		produit.getById(c, produit.getId());
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

	public List<Production> getAll(Connection c) throws Exception {
		List<Production> all = new ArrayList<>();
		PreparedStatement ps = c.prepareStatement("select * from Production");
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Production prod = new Production();
			prod.setId(rs.getInt("id"));
			prod.setProduit(rs.getInt("idProduit"));
			prod.setProduit(c);
			prod.setDate(rs.getDate("date"));
			prod.setQuantite(rs.getBigDecimal("quantite"));
			all.add(prod);
		}

		rs.close();
		ps.close();

		return all;
	}

	// Méthodes pour CRUD
	public void saveNoError(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("INSERT INTO Production (idProduit, date, quantite) VALUES (?, ?, ?)");
		ps.setInt(1, getIdProduit());
		ps.setDate(2, getDate());
		ps.setBigDecimal(3, getQuantite());
		ps.executeUpdate();
		ps.close();
	}

	public void updateNoError(Connection c) throws Exception {
		PreparedStatement ps = c
				.prepareStatement("UPDATE Production SET idProduit = ?, date = ?, quantite = ? WHERE id = ?");
		ps.setInt(1, getIdProduit());
		ps.setDate(2, getDate());
		ps.setBigDecimal(3, getQuantite());
		ps.setInt(4, getId());
		ps.executeUpdate();
		ps.close();
	}

	public void deleteNoError(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("DELETE FROM Production WHERE id = ?");
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
		PreparedStatement ps = c.prepareStatement("SELECT * FROM Production WHERE id = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			this.id = rs.getInt("id");
			setProduit(rs.getInt("idProduit"));
			this.date = rs.getDate("date");
			this.quantite = rs.getBigDecimal("quantite");
		}

		rs.close();
		ps.close();
	}

	public void getByIdAll(Connection c, int id) throws Exception {
		getById(c, id);
		setProduit(c);
	}

	public static List<Production> getByPeriodByProduit(Connection c, int idProduit, Date startDate, Date endDate) throws Exception {
		try {
			if (c == null) {
				c = Connect.getConnection();
			}
			List<Production> result = new ArrayList<>();
			String query = "SELECT * FROM Production WHERE idProduit = ? ";
			query += QueryUtil.getFilterQuery(startDate, endDate, "date");
			PreparedStatement ps = c.prepareStatement(query);
			ps.setInt(1, idProduit);
			QueryUtil.setStatement(ps, startDate, endDate, 2);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				Production prod = new Production();
				prod.setId(rs.getInt("id"));
				prod.setProduit(rs.getInt("idProduit"));
				prod.setProduit(c);
				prod.setDate(rs.getDate("date"));
				prod.setQuantite(rs.getBigDecimal("quantite"));
				result.add(prod);
			}
			rs.close();
			ps.close();
			return result;
		} catch (Exception e) {
			throw e;
		}
	}
}
