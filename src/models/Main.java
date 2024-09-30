package models;

import java.sql.Connection;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;

import database.Connect;
import out.PrixProduit;

public class Main {
    public static void main(String[] args) {
        try {
            Connection c = Connect.getConnection();
            AdministrationAnalytique a = new AdministrationAnalytique(Date.valueOf("2024-08-01"), Date.valueOf("2024-09-21"));
						List<PrixProduit> pp = PrixProduit.getPrixProduits(a, c);

						for (PrixProduit prixProduit : pp) {
							System.out.println("Produit " + prixProduit.getSujet().getNom());
							System.out.println("prix unitaire " + prixProduit.getPrix_unitaire());
							System.out.println("-----");
						}

						c.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
