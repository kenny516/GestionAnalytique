package models;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Centre {
	private int id;
	private String nom;
	private NatureCentre nature;

	// Constructeurs
	public Centre() {
	}

	public Centre(int id, String nom, int idNature , Connection c) throws Exception {
		this.id = id;
		this.nom = nom;
		setNature(idNature);
		setNature(c);
	}

	public Centre(int id, String nom, int idNature){
		this.id = id;
		this.nom = nom;
		setNature(idNature);
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
		return nature.getId();
	}

	public NatureCentre getNature() {
		return nature;
	}

	public void setNature(int idNature) {
		this.nature = new NatureCentre();
		nature.setId(idNature);
	}

	public void setNature(Connection c) throws Exception {
		nature.getById(c, nature.getId());
	}

	public static List<Centre> getAll(Connection c) throws Exception {
		List<Centre> centres = new ArrayList<>();
		PreparedStatement ps = c.prepareStatement("SELECT * FROM Centre");
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Centre centre = new Centre();
			centre.setId(rs.getInt("id"));
			centre.setNom(rs.getString("nom"));
			centre.setNature(rs.getInt("idNature"));
			centre.setNature(c);
			centres.add(centre);
		}

		rs.close();
		ps.close();
		return centres;
	}

	// Méthodes pour CRUD
	public void saveNoError(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("INSERT INTO Centre (nom, idNature) VALUES (?, ?)");
		ps.setString(1, getNom());
		ps.setInt(2, getIdNature());
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
		PreparedStatement ps = c.prepareStatement("UPDATE Centre SET nom = ?, idNature = ? WHERE id = ?");
		ps.setString(1, getNom());
		ps.setInt(2, getIdNature());
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
		PreparedStatement ps = c.prepareStatement("DELETE FROM Centre WHERE id = ?");
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
		PreparedStatement ps = c.prepareStatement("SELECT * FROM Centre WHERE id = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			this.id = rs.getInt("id");
			this.nom = rs.getString("nom");
			setNature(rs.getInt("idNature"));
		}

		rs.close();
		ps.close();
	}

	public void getByIdAll(Connection c, int id) throws Exception {
		getById(c, id);
		setNature(c);
	}

	public List<Depenses> getDepenses(Connection c, Date dMin , Date dMax ) throws Exception {
		List<Depenses> depenses = new ArrayList<>();
		PreparedStatement ps = c.prepareStatement("select * from Depenses where id in ( select idDepense from AssoDepensesParts where idPart in (select id from PartsParCentre where idCentre = ? )) and dateDepense between ? and ?");
		ps.setInt(1, id);
		ps.setDate(2, dMin);
		ps.setDate(3, dMax);
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Depenses depense = new Depenses();
			depense.setId(rs.getInt("id"));
			depense.setDate(rs.getDate("dateDepense"));
			depense.setMontant(rs.getDouble("montant"));
			depenses.add(depense);
		}

		rs.close();
		ps.close();
		return depenses;
	}
}