/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import Protein.Aminoacid;
import Protein.AtomAmino;
import Protein.AtomHet;
import Protein.DistanceAminoAmino;
import Protein.DistanceHetAmino;
import Protein.Hetam;
import Protein.Protein;
import Protein.SChain;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Renzo Angles
 */
public class PsqlDriver {

    String driver = "org.postgresql.Driver";
    String connectString = "jdbc:postgresql://localhost:5432/pdb_norm";
    String user = "#######!";
    String password = "#######!";

    Connection conn;
    ArrayList<String> batch;

    public PsqlDriver() {

    }

    public boolean testConnection() {
        Log.writeAndShow("PsqlDriver.jar", "testConnection", "Testing connection with database");
        Log.writeAndShow("PsqlDriver.jar", "testConnection", "Connection: " + connectString);
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(connectString, user, password);
            Statement stmt = conn.createStatement();
            //test existence of tables
            ResultSet rs;
            rs = stmt.executeQuery("SELECT * FROM protein limit 1;");
            rs.close();
            stmt.close();
        } catch (Exception e) {
            Log.writeAndShow("PsqlDriver.java", "testConnection", e.getMessage());
            return false;
        }
        Log.writeAndShow("PsqlDriver.java", "RunBatch", "The database is ready");
        return true;
    }

    public ArrayList<String> getProteinList() {
        ArrayList<String> list = new ArrayList();
        String expr = "";
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(connectString, user, password);
            Statement stmt = conn.createStatement(); //abrimos la conexion
            ResultSet rs = stmt.executeQuery("SELECT id FROM protein;");
            Log.writeAndShow("PsqlDriver.java", "getProteinList", "Obtaining list of proteins in database");
            while (rs.next()) {
                list.add(rs.getString("id"));
            }
            String msg = "Number of proteins in database: " + list.size();
            Log.writeAndShow("PsqlDriver.java", "getProteinList", msg);
            rs.close();
            stmt.close();
        } catch (Exception e) {
            Log.writeAndShow("PsqlDriver.java", "getProteinList", e.getMessage());
        }
        return list;
    }

    public void OpenConnection() {
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(connectString, user, password);
        } catch (Exception e) {
            Log.write("PsqlDriver.java", "BeginBatch", e.getMessage());
        }
    }

    public void CloseConnection() {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (Exception e) {
            Log.write("PsqlDriver.java", "EndBatch", e.getMessage());
        }
    }

    private void BeginBatch() {
        batch = new ArrayList();
    }

    private void AddToBatch(String expression) {
        //System.out.println(expression);
        batch.add(expression);
    }

    private void EndBatch() {
        try {
            Statement stmt = conn.createStatement();
            Iterator<String> it = batch.iterator();
            while (it.hasNext()) {
                String expr = it.next();
                stmt.addBatch(expr);
            }
            stmt.executeBatch();
            stmt.close();
        } catch (Exception e) {
            Log.write("PsqlDriver.java", "RunBatch", e.getMessage());
        }
    }

    public void ExportProtein(Protein protein) {
        // insert "protein" data
        this.BeginBatch();
        String expr;
        expr = "insert into protein values ("
                + "'" + protein.id + "',"
                + "'" + protein.title + "',"
                + "'" + protein.classification + "',"
                + "'" + protein.organism + "',"
                + "'" + protein.dep_date + "',"
                + "'" + protein.technique + "',"
                + "'" + protein.mod_date + "')";
        this.AddToBatch(expr);

        SChain chain = protein.main_chain;
        if (chain != null) {
            //insert "chain" data
           
            expr = "insert into chain values ("
                    + "'" + chain.id + "',"
                    + "'" + chain.protein_id + "',"
                    + "'" + chain.seqres + "',"
                    + chain.num_het + ","
                    + chain.num_amino + ","
                    + "'" + chain.code+ "')";
            this.AddToBatch(expr);

            Iterator<Aminoacid> ita = chain.aminoacids.iterator();
            while (ita.hasNext()) {
                Aminoacid amino = ita.next();
                String next_amino_id = "NULL";
                String next_amino_symbol = "NULL";
                String next_amino_number = "NULL";
                int next_amino_class = 0;
                if (amino.next_amino != null) {
                    next_amino_id = amino.next_amino.id;
                    next_amino_symbol = amino.next_amino.symbol;
                    next_amino_class = amino.next_amino.class_number;
                    next_amino_number = amino.next_amino.number;
                }

                //insert "aminoacid" data
                
                expr = "insert into aminoacid values ("
                        + "'" + amino.id + "'" + ","
                        + "'" + amino.chain_id+ "'" + ","
                        + "'" + amino.symbol + "'" + ","
                        + "'" + amino.number + "')" ;
                this.AddToBatch(expr);

                //insert "atom_amino" data
                
                Iterator<AtomAmino> itaa = amino.atoms.iterator();
                while (itaa.hasNext()) {
                    AtomAmino atoma = itaa.next();
                    expr = "insert into atom_amino values ("
                            + "'" + atoma.id + "','"
                            + atoma.symbol + "',"
                            + atoma.number + ","
                            + atoma.x + ","
                            + atoma.y + ","
                            + atoma.z + ",'"
                            + atoma.element + "','"
                            + atoma.amino_id + "')";
                    this.AddToBatch(expr);
                }

                if (next_amino_id != "NULL") {
                    //insert "next_amino_amino" data
                    expr = "insert into next_amino_amino values ("
                            + "'" + amino.id + "'" + ","
                            + "'" + next_amino_id + "')";
                    this.AddToBatch(expr);
                }
            }

            // insert "het" data (ligands)
            
            Iterator<Hetam> ith = chain.hetams.iterator();
            while (ith.hasNext()) {
                Hetam het = ith.next();
                expr = "insert into het values ("
                        + "'" + het.id + "',"
                        + "'" + het.chain_id+ "',"
                        + "'" + het.symbol + "',"
                        + "'" + 0+ "')";
                this.AddToBatch(expr);

                // insert "atom_het" data
                Iterator<AtomHet> itah = het.atoms.iterator();
                while (itah.hasNext()) {
                    AtomHet atomh = itah.next();
                    expr = "insert into atom_het values ("
                            + "'" + atomh.id + "','"
                            + atomh.symbol + "',"
                            + atomh.number + ","
                            + atomh.x + ","
                            + atomh.y + ","
                            + atomh.z + ",'"
                            + atomh.element + "','"
                            + atomh.het_id + "')";
                    this.AddToBatch(expr);
                }
            }

            // insert "distance_amino_amino" data
            Iterator<DistanceAminoAmino> itdaa = chain.daa_list.iterator();
            while (itdaa.hasNext()) {
                DistanceAminoAmino dist = itdaa.next();
                expr = "insert into distance_amino_amino values ("
                        + "'" + dist.amino1.id + "'" + ","
                        + "'" + dist.amino2.id + "'" + ","
                        + dist.dist + ")";
                this.AddToBatch(expr);
            }

            // insert "distance_het_amino" data
            Iterator<DistanceHetAmino> itdha = chain.dha_list.iterator();
            while (itdha.hasNext()) {
                DistanceHetAmino dist = itdha.next();
                expr = "insert into distance_het_amino values ("
                        + "'" + dist.amino.id + "'" + ","
                        + "'" + dist.het.id+ "'" + ","
                        + dist.dist + ")";
                this.AddToBatch(expr);
            }

        }
        this.EndBatch();

    }

}
