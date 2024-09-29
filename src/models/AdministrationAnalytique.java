package models;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import database.Connect;
import database.QueryUtil;

public class AdministrationAnalytique {
    private java.sql.Date  dateDebut;
    private java.sql.Date  dateFin;
    private List<Rubrique> rubriques;

    public AdministrationAnalytique(Date dateDebut, Date dateFin) throws Exception{
        try {
            this.setDateDebut(dateDebut);
            this.setDateFin(dateFin);
            this.setRubriques();
        } catch (Exception e) {
            throw e;
        }
    }
    public void setRubriques() throws Exception{
        try {
            this.rubriques = Rubrique.getByPeriod(null, dateDebut, dateFin);
        } catch (Exception e) {
            throw e;
        }
    }
    public java.sql.Date getDateDebut() {
        return dateDebut;
    }public java.sql.Date getDateFin() {
        return dateFin;
    }public List<Rubrique> getRubriques() {
        return rubriques;
    } public void setDateDebut(java.sql.Date dateDebut) {
        this.dateDebut = dateDebut;
    }public void setDateFin(java.sql.Date dateFin) {
        this.dateFin = dateFin;
    }


    
    public HashMap<Centre, double[]> getPartCentreParRubrique(Connection c, int idRubrique) throws Exception{
        Date starDate = this.getDateDebut();
        Date endDate = this.getDateFin();
        HashMap<Centre, double[]> result = new HashMap<Centre, double[]>();
        String query = "SELECT Centre.*, PartsParCentre.valeur as part, " +
        "SUM(depenses.montant * (PartsParCentre.valeur / 100)) as total " +
        "FROM Depenses " +
        "JOIN AssoDepensesParts ON Depenses.id = AssoDepensesParts.idDepense " +
        "JOIN PartsParCentre ON AssoDepensesParts.idPart = PartsParCentre.id " +
        "JOIN Centre ON Centre.id = PartsParCentre.idCentre " +
        "JOIN Rubrique ON Rubrique.id = PartsParCentre.idRubrique " +
        "WHERE Rubrique.id = ? ";
        
        query+=QueryUtil.getFilterQuery(starDate, endDate, "dateDepense");
        query+=QueryUtil.getFilterQuery(starDate, endDate, "Rubrique.dateInsertion");
        query+=" GROUP BY Centre.id, PartsParCentre.idRubrique";
        // System.out.println(query);
        try {
            if(c==null){
                c = Connect.getConnection();
            }
            PreparedStatement ps = c.prepareStatement( query);
            int count = 2;
            ps.setInt(1, idRubrique);
            count = QueryUtil.setStatement(ps, starDate, this.dateFin, count);
            // System.out.println(count);
            count = QueryUtil.setStatement(ps, starDate, this.dateFin, count);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Centre centre = new Centre(rs.getInt("id"), rs.getString("nom"), rs.getInt("idNature"));
                double part = rs.getDouble("part");
                double total = rs.getDouble("total");
                result.put(centre, new double[]{part, total});
            }  
            ps.close(); 
            rs.close();
        } catch (Exception e) {
            throw e;
        } 
        return result;  
    }

    public List<HashMap<Centre, double[]>> getPartCentreAllRubrique(Connection c) throws Exception{
        List<HashMap<Centre, double[]>> result = new ArrayList<>();
        try {
            if(c==null){
                c = Connect.getConnection();
            }
            for (Rubrique r : rubriques) {
                result.add(this.getPartCentreParRubrique(c, r.getId()));
            }
        }catch (Exception e) {
            throw e;
        }
        return result;
    }

    public HashMap<Centre, Double> getTotalDepenseParCentre(Connection c) throws Exception {
        HashMap<Centre, Double> result = new HashMap<Centre, Double>();
    
        String query = "SELECT Centre.*, " +
        "SUM(depenses.montant * (PartsParCentre.valeur / 100)) as total " +
        "FROM Depenses " +
        "JOIN AssoDepensesParts ON Depenses.id = AssoDepensesParts.idDepense " +
        "JOIN PartsParCentre ON AssoDepensesParts.idPart = PartsParCentre.id " +
        "JOIN Centre ON Centre.id = PartsParCentre.idCentre " +
        "JOIN Rubrique ON Rubrique.id = PartsParCentre.idRubrique " +
        "WHERE 1=1 ";

        query+=QueryUtil.getFilterQuery(this.dateDebut, this.dateFin, "dateDepense");
        query+=QueryUtil.getFilterQuery(this.dateDebut, this.dateFin, "Rubrique.dateInsertion");
        query+=" GROUP BY Centre.id";
        try {
            if(c==null){
                c = Connect.getConnection();
            }
            PreparedStatement ps = c.prepareStatement( query);
            int count = 1;
            count = QueryUtil.setStatement(ps, this.dateDebut, this.dateFin, count);
            count = QueryUtil.setStatement(ps, this.dateDebut, this.dateFin, count);
            System.err.println(ps.toString());
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Centre centre = new Centre(rs.getInt("id"), rs.getString("nom"), rs.getInt("idNature"));
                double total = rs.getDouble("total");
                result.put(centre, total);
            }  
            ps.close(); 
            rs.close();
        } catch (Exception e) {
            throw e;
        } 
        return result;  
    }

    public double getTotalDepense(Connection c) throws Exception {
        double sum = 0;
        try {
            List<Depenses> liste = Depenses.getByPeriod(c, this.dateDebut, this.dateFin);
            for (Depenses depenses : liste) {
                sum+=depenses.getMontant();
            }
        } catch (Exception e) {
            throw e;
        }
        return sum;
    }
    public double getTotalDepenseVariable(Connection c) throws Exception {
        double sum = 0;
        try {
            List<Depenses> liste = Depenses.getVariable(c, this.dateDebut, this.dateFin);
            for (Depenses depenses : liste) {
                System.out.println(depenses.getMontant());
                sum+=depenses.getMontant();
            }
        } catch (Exception e) {
            throw e;
        }
        return sum;
    }

    public double getTotalDepenseInvariable(Connection c) throws Exception {
        double sum = 0;
        try {
            List<Depenses> liste = Depenses.getInvariable(c, this.dateDebut, this.dateFin);
            for (Depenses depenses : liste) {
                sum+=depenses.getMontant();
            }
        } catch (Exception e) {
            throw e;
        }
        return sum;
    }
}
