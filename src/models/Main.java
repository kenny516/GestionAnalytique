package models;

import java.sql.Connection;
import java.sql.Date;
import java.util.HashMap;
import java.util.List;

import database.Connect;

public class Main {
    public static void main(String[] args) {
        try {
            Connect connect = new Connect();
            Connection c = connect.getConnection();
            AdministrationAnalytique a = new AdministrationAnalytique(Date.valueOf("2024-08-01"), Date.valueOf("2024-09-21"));
            System.out.println(a.getTotalDepense(c));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
