package com.company;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;

public class transferDAO {
    public boolean transfer(int account_no1, int account_no2, BigDecimal amount){
        if(amount.compareTo(BigDecimal.ZERO)<=0){
            System.out.println("enter positive value");
        }
        Connection con=null;
        try{
            con=databaseConnection.getConnection();
            con.setAutoCommit(false);

            return true;
        }
        catch (SQLException e) {

            e.printStackTrace();

            try {
                if (con != null) {
                    con.rollback();
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

            return false;

        } finally {

            try {
                if (con != null) {
                    con.setAutoCommit(true);
                    con.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
