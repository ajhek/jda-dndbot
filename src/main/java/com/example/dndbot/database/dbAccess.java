package com.example.dndbot.database;

import java.sql.*;

public class dbAccess {
    private Connection db;

    public dbAccess() throws SQLException {
        db = DriverManager.getConnection("jdbc:sqlite:charInfo.db");
    }

    public void connect() {
        // NOTE: Connection and Statement are AutoCloseable.
        //       Don't forget to close them both in order to avoid leaks.
        try
                (
                        // create a database connection
                        Statement statement = db.createStatement();
                )
        {
            statement.setQueryTimeout(30);  // set timeout to 30 sec.

            statement.executeUpdate("drop table if exists person");
            statement.executeUpdate("create table person (id integer, name string)");
            statement.executeUpdate("insert into person values(1, 'leo')");
            statement.executeUpdate("insert into person values(2, 'yui')");
            ResultSet rs = statement.executeQuery("select * from person");
            while(rs.next())
            {
                // read the result set
                System.out.println("name = " + rs.getString("name"));
                System.out.println("id = " + rs.getInt("id"));
            }
        }
        catch(SQLException e)
        {
            // if the error message is "out of memory",
            // it probably means no database file is found
            e.printStackTrace(System.err);
        }
    }
}
