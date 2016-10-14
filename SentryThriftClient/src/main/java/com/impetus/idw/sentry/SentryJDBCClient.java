package com.impetus.idw.sentry;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;

public class SentryJDBCClient {

    private static String driverName = "org.apache.hive.jdbc.HiveDriver";

    /**
     * @param args
     * @throws SQLException
     */
    public static void main(String[] args) throws SQLException {
        try {
            Class.forName(driverName);
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.exit(1);
        }
        System.setProperty("java.security.auth.login.config","gss-jaas.conf");
        System.setProperty("sun.security.jgss.debug","true");
        System.setProperty("javax.security.auth.useSubjectCredsOnly","false");
        System.setProperty("java.security.krb5.conf","krb5.conf");

        Connection con = DriverManager.getConnection("jdbc:hive2://quickstart.cloudera:10000/;principal=hive/quickstart.cloudera@IDWREALM.COM");
        Statement stmt = con.createStatement();

        String table1="table1", table2="table2";
        String role1="role1", role2="role2";
        String group1="group1", group2="group2";
        String database = "idw";

        stmt.execute("drop database if exists " + database);
        stmt.execute("use " + database);

        stmt.execute("drop table if exists " + table1);
        stmt.execute("create table " + table1 + " (col1 string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ','");
        stmt.execute("drop table if exists " + table2);
        stmt.execute("create table " + table2 + " (col1 string) ROW FORMAT DELIMITED FIELDS TERMINATED BY ','");

        stmt.execute("drop role if exists " + role1);
        stmt.execute("drop role if exists " + role2);

        stmt.execute("grant select on table " + table1 + " to role " + role1);
        stmt.execute("grant select on table " + table2 + " to role " + role2);

        stmt.execute("grant role " + role1 + " to group " + group1);
        stmt.execute("grant role " + role2 + " to group " + group2);

        // show roles
        String sql = "show roles";
        System.out.println("Running: " + sql);
        ResultSet res = stmt.executeQuery(sql);
        while (res.next()) {
            System.out.println(res.getString(1));
        }

        // show grant on roles
        sql = "show grant role " + role1;
        System.out.println("\nRunning: " + sql);
        res = stmt.executeQuery(sql);
        while (res.next()) {
            System.out.println(res.getString(1));
        }

        sql = "show grant role " + role2;
        System.out.println("\nRunning: " + sql);
        res = stmt.executeQuery(sql);
        while (res.next()) {
            System.out.println(res.getString(1));
        }

        sql = "show role grant group " + group1;
        System.out.println("\nRunning: " + sql);
        res = stmt.executeQuery(sql);
        while (res.next()) {
            System.out.println(res.getString(1));
        }

        sql = "show role grant group " + group2;
        System.out.println("\nRunning: " + sql);
        res = stmt.executeQuery(sql);
        while (res.next()) {
            System.out.println(res.getString(1));
        }
    }
}
