
import Protein.Protein;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author renzo
 */
public class Parser {

    String msg = "";
    HashMap<String, String> pdb_files;

    public void Run(String dir) {
        pdb_files = new HashMap();
        ArrayList<String> files2process = new ArrayList();
        try {
            msg = "Reading directory " + dir;
            Log.writeAndShow("Parser.java", "Run", msg);
            this.ExploreDirectory(dir);
            msg = "Number of files in directory: " + pdb_files.size();
            Log.writeAndShow("Parser.java", "Run", msg);

            PsqlDriver driver = new PsqlDriver();
            if (!driver.testConnection()) {
                return;
            }
            driver.OpenConnection();

            //remove pdbs available in the database
            ArrayList pdbs_db = driver.getProteinList();
            msg = "Number of PDBs in database: " + pdbs_db.size();
            Log.writeAndShow("Parser.java", "Run", msg);
            Iterator<String> it1 = pdbs_db.iterator();
            while (it1.hasNext()) {
                pdb_files.remove(it1.next());
            }

            Map.Entry mentry;
            String filepath;
            Set set = pdb_files.entrySet();
            Iterator iterator = set.iterator();
            while (iterator.hasNext()) {
                mentry = (Map.Entry) iterator.next();
                filepath = mentry.getValue().toString();
                files2process.add(filepath);
            }

            int totalFiles = files2process.size();
            msg = "PDB files to process: " + totalFiles;
            Log.writeAndShow("Parser.java", "Run", msg);

            long proc_begin = System.currentTimeMillis();
            int processed = 0;
            int unprocessed = 0;
            int cnt = 0;
            PdbReader pdb_reader = new PdbReader();
            Iterator<String> it3 = files2process.iterator();
            while (it3.hasNext()) {
                filepath = it3.next();
                System.out.println("Processing file: " + filepath);
                Protein protein = pdb_reader.ProcessFile(filepath);
                if (protein != null) {
                    driver.ExportProtein(protein);
                    processed++;
                } else {
                    unprocessed++;
                    msg = "Unprocessed: " + filepath;
                    Log.write("Parser.java", "Run", msg);
                }
                cnt++;
                System.out.println(cnt + "/" + totalFiles);
            }

            long proc_time = System.currentTimeMillis() - proc_begin;
            msg = "Processing time: " + proc_time + " ms";
            Log.writeAndShow("Parser.java", "Run", msg);
            msg = "Number of processed files: " + processed;
            Log.writeAndShow("Parser.java", "Run", msg);
            msg = "Number of unprocessed files: " + unprocessed;
            Log.writeAndShow("Parser.java", "Run", msg);
            driver.CloseConnection();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public void Run2(String dir, String pdbs_list_file) {
        pdb_files = new HashMap();
        ArrayList<String> pdbs_file = new ArrayList();
        ArrayList<String> files2process = new ArrayList();
        try {
            msg = "Reading file: " + pdbs_list_file;
            Log.writeAndShow("Parser.java", "Run", msg);
            BufferedReader br = new BufferedReader(new FileReader(pdbs_list_file));
            String pdbid;
            while ((pdbid = br.readLine()) != null) {
                pdbs_file.add(pdbid);
            }
            msg = "Number of PDB-ID's in file: " + pdbs_file.size();
            Log.writeAndShow("Parser.java", "Run", msg);

            PsqlDriver driver = new PsqlDriver();
            if (!driver.testConnection()) {
                return;
            }
            driver.OpenConnection();

            //remove pdbids available in the database
            ArrayList pdbs_db = driver.getProteinList();
            msg = "Number of PDBs in database: " + pdbs_db.size();
            Log.writeAndShow("Parser.java", "Run", msg);
            Iterator<String> it1 = pdbs_db.iterator();
            while (it1.hasNext()) {
                pdbs_file.remove(it1.next());
            }

            msg = "Reading directory " + dir;
            Log.writeAndShow("Parser.java", "Run", msg);
            this.ExploreDirectory(dir);
            msg = "Number of files in directory: " + pdb_files.size();
            Log.writeAndShow("Parser.java", "Run", msg);

            Iterator<String> it2 = pdbs_file.iterator();
            while (it2.hasNext()) {
                String filename = pdb_files.get(it2.next());
                if (filename != null) {
                    files2process.add(filename);
                }
            }
            int totalFiles = files2process.size();
            msg = "PDB files to process: " + totalFiles;
            Log.writeAndShow("Parser.java", "Run", msg);

            long proc_begin = System.currentTimeMillis();
            int processed = 0;
            int unprocessed = 0;
            int cnt = 0;
            PdbReader pdb_reader = new PdbReader();
            //transform the rest of PDB files
            Iterator<String> it3 = files2process.iterator();
            while (it3.hasNext()) {
                String filepath = it3.next();
                Protein protein = pdb_reader.ProcessFile(filepath);
                if (protein != null) {
                    driver.ExportProtein(protein);
                    processed++;
                } else {
                    unprocessed++;
                    msg = "Unprocessed: " + filepath;
                    Log.write("Parser.java", "Run", msg);
                }
                cnt++;
                System.out.println(cnt + "/" + totalFiles);
            }
            long proc_time = System.currentTimeMillis() - proc_begin;
            msg = "Processing time: " + proc_time + " ms";
            Log.writeAndShow("Parser.java", "Run", msg);
            msg = "Number of processed files: " + processed;
            Log.writeAndShow("Parser.java", "Run", msg);
            msg = "Number of unprocessed files: " + unprocessed;
            Log.writeAndShow("Parser.java", "Run", msg);
            driver.CloseConnection();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Method to return the list of files inside a directory (recursively)
     */
    private void ExploreDirectory(String directoryPath) {
        String pdbid;
        String filename;
        String filepath;
        try {
            File directory = new File(directoryPath);
            if (directory.isDirectory()) {
                String[] subfiles = directory.list();
                for (int i = 0; i < subfiles.length; i++) {
                    filename = subfiles[i];
                    filepath = directory.getPath() + "/" + filename;
                    File subfile = new File(filepath);

                    if (subfile.isDirectory()) {
                        this.ExploreDirectory(filepath);
                    } else {
                        if (filename.toLowerCase().endsWith(".pdb")
                                || filename.toLowerCase().endsWith(".pdb.gz")
                                || filename.toLowerCase().endsWith(".ent")
                                || filename.toLowerCase().endsWith(".ent.gz")) {

                            pdbid = subfiles[i].toLowerCase()
                                    .replace(".pdb", "")
                                    .replace(".gz", "")
                                    .replace(".ent", "");
                            if (pdbid.startsWith("pdb")) {
                                pdbid = pdbid.substring(3);
                            }
                            pdbid = pdbid.toUpperCase();
                            pdb_files.put(pdbid, filepath);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Log.write("Parser.java", "ExploreDirectory", ex.getMessage());
        }
    }

}
