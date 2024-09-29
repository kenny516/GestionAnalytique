package database;

import java.sql.Date;
import java.sql.PreparedStatement;

public class QueryUtil {
    public static String getFilterQuery(Date startDate, Date endDate, String dateName){
        String query = "";
        
        if (startDate != null && endDate != null) {
            query += " AND "+dateName+" BETWEEN ? AND ? ";
        }  else if (startDate != null) {
            query += " AND "+dateName+" >= ? ";
        } else if (endDate != null) {
            query += " AND "+dateName+" <= ? ";
        }
        return query;
    }

    public static void setStatement(PreparedStatement statement,Date startDate, Date endDate, int count)throws Exception{
        try{
            if (startDate != null && endDate != null) {
                statement.setDate(count, startDate);
                count++;
                statement.setDate(count, endDate);
                count++;
            } else if (startDate != null) {
                statement.setDate(count, startDate);
                count++;
            } else if (endDate != null) {
                statement.setDate(count, endDate);
                count++;
            }
        } catch(Exception e){
            throw e;
        }
        
    }
}
