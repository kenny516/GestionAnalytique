package models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import database.*;
public class Rubrique {
	private int id;
	private String nom;
	private boolean estVariable;
	private UniteOeuvre uOeuvre;
	private java.sql.Date dateInsertion;

	private List<PartsParCentre> pCentre;
	private List<Depenses> deps;

	// Constructeurs
	public Rubrique() {
	}

	public Rubrique(int id, String nom, boolean estVariable, int idUniteOeuvre, java.sql.Date dateInsertion, Connection c)
			throws Exception {
		this.id = id;
		this.nom = nom;
		this.estVariable = estVariable;
		this.setUniteOeuvre(idUniteOeuvre);
		this.setUniteOeuvre(c);
		this.dateInsertion = dateInsertion;
	}


	public Rubrique(int id, String nom, boolean estVariable, int idUniteOeuvre, java.sql.Date dateInsertion, Connection c,
			boolean tout) throws Exception {
		this.id = id;
		this.nom = nom;
		this.estVariable = estVariable;
		this.setUniteOeuvre(idUniteOeuvre);
		this.setUniteOeuvre(c);
		this.dateInsertion = dateInsertion;
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

	public boolean isEstVariable() {
		return estVariable;
	}

	public void setEstVariable(boolean estVariable) {
		this.estVariable = estVariable;
	}

	public int getIdUniteOeuvre() {
		return uOeuvre.getId();
	}

	public UniteOeuvre getUniteOeuvre() {
		return uOeuvre;
	}

	public void setUniteOeuvre(int idUniteOeuvre) {
		this.uOeuvre = new UniteOeuvre();
		this.uOeuvre.setId(idUniteOeuvre);
	}

	public void setUniteOeuvre(Connection c) throws Exception {
		uOeuvre.getById(c, getIdUniteOeuvre());
	}

	public java.sql.Date getDateInsertion() {
		return dateInsertion;
	}

	public void setDateInsertion(java.sql.Date dateInsertion) {
		this.dateInsertion = dateInsertion;
	}

	public List<Rubrique> getAll(Connection c) throws Exception {
		List<Rubrique> result = new ArrayList<>();
		PreparedStatement ps = c.prepareStatement("select * from Rubrique");
		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Rubrique rq = new Rubrique();
			rq.setId(rs.getInt("id"));
			rq.setNom(rs.getString("nom"));
			rq.setEstVariable(rs.getBoolean("estVariable"));
			rq.setUniteOeuvre(rs.getInt("idUniteOeuvre"));
			rq.setUniteOeuvre(c);
			rq.setDateInsertion(rs.getDate("dateInsertion"));

			result.add(rq);
		}

		return result;
	}

	// Méthodes pour CRUD
	public void saveNoError(Connection c) throws Exception {
		PreparedStatement ps = c
				.prepareStatement("INSERT INTO Rubrique (nom, estVariable, idUniteOeuvre, dateInsertion) VALUES (?, ?, ?, ?)");
		ps.setString(1, getNom());
		ps.setBoolean(2, isEstVariable());
		ps.setInt(3, getIdUniteOeuvre());
		ps.setDate(4, getDateInsertion());
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
		PreparedStatement ps = c.prepareStatement(
				"UPDATE Rubrique SET nom = ?, estVariable = ?, idUniteOeuvre = ?, dateInsertion = ? WHERE id = ?");
		ps.setString(1, getNom());
		ps.setBoolean(2, isEstVariable());
		ps.setInt(3, getIdUniteOeuvre());
		ps.setDate(4, getDateInsertion());
		ps.setInt(5, getId());
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
		PreparedStatement ps = c.prepareStatement("DELETE FROM Rubrique WHERE id = ?");
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
		PreparedStatement ps = c.prepareStatement("SELECT * FROM Rubrique WHERE id = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			this.id = rs.getInt("id");
			this.nom = rs.getString("nom");
			this.estVariable = rs.getBoolean("estVariable");
			this.setUniteOeuvre(rs.getInt("idUniteOeuvre"));
			this.dateInsertion = rs.getDate("dateInsertion");
		}

		rs.close();
		ps.close();
	}

	public static List<Rubrique> getByPeriod(Connection c, Date startDate, Date endDate) throws Exception {
		try {
			if (c == null) {
				c = Connect.getConnection();
			}
			List<Rubrique> result = new ArrayList<>();
			String query = "SELECT * FROM Rubrique WHERE 1=1 ";
			query+=QueryUtil.getFilterQuery(startDate, endDate, "dateInsertion");
			PreparedStatement ps = c.prepareStatement( query);
			QueryUtil.setStatement(ps, startDate, endDate, 1);
			ResultSet rs = ps.executeQuery();
	
			while (rs.next()) {
				Rubrique rq = new Rubrique();
				rq.setId(rs.getInt("id"));
				rq.setNom(rs.getString("nom"));
				rq.setEstVariable(rs.getBoolean("estVariable"));
				rq.setUniteOeuvre(rs.getInt("idUniteOeuvre"));
				rq.setUniteOeuvre(c);
				rq.setDateInsertion(rs.getDate("dateInsertion"));
	
				result.add(rq);
			}
			rs.close();
			ps.close();
			return result;
		} catch (Exception e) {
			throw e;
		}
	}

	

	public void getByIdAll(Connection c, int id) throws Exception {
		getById(c, id);
		setUniteOeuvre(c);
		setpCentre(pCentre);
	}

	public List<PartsParCentre> getpCentre() {
		return pCentre;
	}

	public List<Depenses> getDeps() {
		return deps;
	}

	public void setDeps(List<Depenses> deps) {
		this.deps = deps;
	}

	public void setpCentre(List<PartsParCentre> pCentre) {
		this.pCentre = pCentre;
	}

	public void setpCentre(Connection c) throws Exception {
		List<PartsParCentre> partsParCentre = new ArrayList<>();
		PreparedStatement ps = c.prepareStatement(
				"select * from PartsParCentre having dateInsertion = max(dateInsertion) and idRubrique = ? group by idCentre");
		ps.setInt(1, id);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			PartsParCentre parts = new PartsParCentre();
			parts.setId(rs.getInt("id"));
			parts.setRubrique(rs.getInt("idRubrique"));

			parts.setCentre(rs.getInt("idCentre"));
			parts.setCentre(c);

			parts.setValeur(rs.getBigDecimal("valeur"));
			parts.setDateInsertion(rs.getDate("dateInsertion"));
		}

		rs.close();
		ps.close();

		setpCentre(partsParCentre);
	}

	public void setDeps(java.sql.Date dateMin, java.sql.Date dateMax, Connection c) throws Exception {
		List<Depenses> deps = new ArrayList<>();
		PreparedStatement ps = c.prepareStatement(
				"select * from Depenses where id in ( select idDepense from AssoDepensesParts where idPart in (select id from PartsParCentre where idRubrique = ? )) and dateDepense between ? and ?");
		ps.setInt(1, getId());
		ps.setDate(2, dateMin);
		ps.setDate(3, dateMax);

		ResultSet rs = ps.executeQuery();

		while (rs.next()) {
			Depenses depense = new Depenses();
			depense.setId(rs.getInt("id"));
			depense.setDate(rs.getDate("dateDepense"));
			depense.setMontant(rs.getDouble("montant"));
			deps.add(depense);
		}

		rs.close();
		ps.close();

		setDeps(deps);
	}


}
