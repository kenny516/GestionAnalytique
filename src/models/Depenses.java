package models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import database.Connect;
import database.QueryUtil;

public class Depenses {
	private int id;
	private java.sql.Date date;
	private double montant;

	private Rubrique rubrique;
	private List<PartsParCentre> pCentre;

	public Depenses(String date, String montant, Rubrique rubrique) throws Exception {
		setDate(date);
		setMontant(montant);
		this.rubrique = rubrique;
	}

	// Constructeurs
	public Depenses() {
	}

	public Depenses(int id, java.sql.Date date, double montant) {
		this.id = id;
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

	public java.sql.Date getDate() {
		return date;
	}

	public void setDate(java.sql.Date date) {
		this.date = date;
	}

	public void setDate(String dateStr) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date parsedDate = dateFormat.parse(dateStr);
		java.sql.Date sqlDate = new java.sql.Date(parsedDate.getTime());
		this.date = sqlDate;
	}

	public double getMontant() {
		return montant;
	}

	public void setMontant(double montant) {
		this.montant = montant;
	}

	public void setMontant(String montant) {
		this.montant = Double.valueOf(montant);
	}

	public static List<Depenses> getByPeriod(Connection c, Date startDate, Date endDate) throws Exception {
		try {
			if (c == null) {
				c = Connect.getConnection();
			}
			List<Depenses> depenses = new ArrayList<>();
			String query = "SELECT * FROM Depenses WHERE 1=1 ";
			query+=QueryUtil.getFilterQuery(startDate, endDate, "dateDepense");
			PreparedStatement ps = c.prepareStatement( query);
			QueryUtil.setStatement(ps, startDate, endDate, 1);
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
		} catch (Exception e) {
			throw e;
		}
	}
	

	public static List<Depenses> getAll(Connection c) throws Exception {
		List<Depenses> depenses = new ArrayList<>();
		PreparedStatement ps = c.prepareStatement("SELECT * FROM Depenses");
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

	// Méthodes pour CRUD
	public void saveNoError(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("INSERT INTO Depenses (dateDepense, montant) VALUES (?, ?)");
		ps.setDate(1, getDate());
		ps.setDouble(2, getMontant());
		ps.executeUpdate();
		ps.close();
	}

	public void savetsotsa(Connection c) throws Exception {
		try {
			c.setAutoCommit(false);
			saveNoError(c);
			c.commit();
		} catch (Exception e) {
			c.rollback();
			throw e;
		}
	}

	public void save(Connection c, List<PartsParCentre> pc) throws Exception {
		try {
			c.setAutoCommit(false);

			saveNoError(c);
			for (PartsParCentre partsParCentre : pc) {
				AssoDepensesParts p = new AssoDepensesParts(0, getId(), partsParCentre.getId());
				p.saveNoError(c);
			}

			c.commit();
		} catch (Exception e) {
			c.rollback();
			throw e;
		}
	}

	public void save(Connection c) throws Exception {
		try {
			c.setAutoCommit(false);

			saveNoError(c);
			for (PartsParCentre partsParCentre : getpCentre()) {
				AssoDepensesParts p = new AssoDepensesParts(0, getId(), partsParCentre.getId());
				p.saveNoError(c);
			}

			c.commit();
		} catch (Exception e) {
			c.rollback();
			throw e;
		}
	}

	public void updateNoError(Connection c) throws Exception {
		PreparedStatement ps = c.prepareStatement("UPDATE Depenses SET dateDepense = ?, montant = ? WHERE id = ?");
		ps.setDate(1, getDate());
		ps.setDouble(2, getMontant());
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
		PreparedStatement ps = c.prepareStatement("DELETE FROM Depenses WHERE id = ?");
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
		PreparedStatement ps = c.prepareStatement("SELECT * FROM Depenses WHERE id = ?");
		ps.setInt(1, id);
		ResultSet rs = ps.executeQuery();

		if (rs.next()) {
			this.id = rs.getInt("id");
			this.date = rs.getDate("dateDepense");
			this.montant = rs.getDouble("montant");
		}

		rs.close();
		ps.close();
	}

	public List<PartsParCentre> getpCentre() {
		return pCentre;
	}

	public void setpCentre(List<PartsParCentre> pCentre) {
		this.pCentre = pCentre;
	}

	public static List<Depenses> getVariable(Connection c, Date startDate, Date endDate) throws Exception {
		try {
			if (c == null) {
				c = Connect.getConnection();
			}
			List<Depenses> depenses = new ArrayList<>();
			String query = "SELECT distinct Depenses.* FROM Depenses " +
               "JOIN AssoDepensesParts ON Depenses.id = AssoDepensesParts.idDepense " +
               "JOIN PartsParCentre ON AssoDepensesParts.idPart = PartsParCentre.id " +
               "JOIN Rubrique ON Rubrique.id = PartsParCentre.idRubrique WHERE Rubrique.estVariable IS true ";
			query+=QueryUtil.getFilterQuery(startDate, endDate, "dateDepense");
			query+=QueryUtil.getFilterQuery(startDate, endDate, "Rubrique.dateInsertion");
			PreparedStatement ps = c.prepareStatement( query);
			int count = 1;
			count = QueryUtil.setStatement(ps, startDate, endDate, count);
			count = QueryUtil.setStatement(ps, startDate, endDate, count);
			System.out.println(ps.toString());
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
		} catch (Exception e) {
			throw e;
		}
	}

	public static List<Depenses> getInvariable(Connection c, Date startDate, Date endDate) throws Exception {
		try {
			if (c == null) {
				c = Connect.getConnection();
			}
			List<Depenses> depenses = new ArrayList<>();
			String query = "SELECT distinct Depenses.* FROM Depenses " +
               "JOIN AssoDepensesParts ON Depenses.id = AssoDepensesParts.idDepense " +
               "JOIN PartsParCentre ON AssoDepensesParts.idPart = PartsParCentre.id " +
               "JOIN Rubrique ON Rubrique.id = PartsParCentre.idRubrique WHERE Rubrique.estVariable IS False ";
			query+=QueryUtil.getFilterQuery(startDate, endDate, "dateDepense");
			query+=QueryUtil.getFilterQuery(startDate, endDate, "Rubrique.dateInsertion");
			PreparedStatement ps = c.prepareStatement( query);
			int count = 1;
			count = QueryUtil.setStatement(ps, startDate, endDate, count);
			count = QueryUtil.setStatement(ps, startDate, endDate, count);
			System.out.println(ps.toString());

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
		} catch (Exception e) {
			throw e;
		}
	}

	public void setpCentre(Connection c) throws Exception {
		List<PartsParCentre> partsParCentre = new ArrayList<>();
		PreparedStatement ps = c
				.prepareStatement("select * from PartsParCentre where id in (select idPart from AssoDepensesParts where idDepense = ?)");
		ps.setInt(1, getId());
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

	public void setRubrique(Connection c) throws Exception{
		this.rubrique = new Rubrique();
		this.rubrique.getById(c, id);
		getRubrique().setpCentre(c);
	}
	public Rubrique getRubrique() {
		return rubrique;
	}

}
