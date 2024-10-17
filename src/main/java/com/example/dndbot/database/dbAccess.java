package com.example.dndbot.database;

import java.sql.*;
import java.util.Map;

public class dbAccess {
    private final Connection db;

    public dbAccess() throws SQLException {
        db = DriverManager.getConnection("jdbc:sqlite:charInfo.db");
    }

    public Integer retrieveStat(Long id, String stat) throws SQLException {
        try(Statement s = db.createStatement()) {
            s.setQueryTimeout(30);
            ResultSet rs = s.executeQuery("select " + id + ", " + stat + " from characters");
            if (rs.next()) {
                return rs.getInt(stat);
            } else {
                return 0;
            }
        } catch(SQLException e){
            e.printStackTrace(System.err);
            return 0;
        }
    }

    public String retrieveName(Long id) throws SQLException {
        try(Statement s = db.createStatement()) {
            s.setQueryTimeout(30);
            ResultSet rs = s.executeQuery("select " + id + ", name from characters");
            if (rs.next()) {
                return rs.getString("name");
            } else {
                return "null";
            }
        } catch(SQLException e){
            e.printStackTrace(System.err);
            return "null";
        }
    }

    public void insert(Long id, String name, Map<String, Integer> stats) throws SQLException {
        try(Statement s = db.createStatement()) {
            s.setQueryTimeout(30);
            s.executeUpdate("insert into characters values(" + id + "," + name + "," + stats.get("ranged") + "," + stats.get("melee") + "," + stats.get("cqc") + "," + stats.get("dodge") + ","
                    + stats.get("block") + "," + stats.get("throwables") + "," + stats.get("perception") + "," + stats.get("bigbrain") + "," + stats.get("speech") + "," + stats.get("stealth") + "," + stats.get("technical"));
        } catch(SQLException e){
            e.printStackTrace(System.err);
        }
    }

    public void updateStat(Long id, String stat, Integer newStat){
        try(Statement s = db.createStatement()){
            s.setQueryTimeout(30);
            s.executeUpdate("update characters set "+stat+" = "+newStat+" where id = "+id);
        } catch(SQLException e){
            e.printStackTrace(System.err);
        }
    }

    public void deleteChar(Long id) throws SQLException {
        try(Statement s = db.createStatement()){
            s.setQueryTimeout(30);
            s.executeUpdate("delete from characters where id="+id);
        } catch(SQLException e){
            e.printStackTrace(System.err);
        }
    }
}
