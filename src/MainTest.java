
import Protein.Protein;
import java.io.File;
import org.biojava.nbio.structure.StructureException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author renzo
 */
public class MainTest {

    public static void main(String[] args) throws StructureException {
        String dir = "/Users/renzo/Experimentos/gsp4pdb-parser/input-error"; //input directory
        String filename = "";
        String filepath = "";
        File file;
        PdbReader pdb_reader = new PdbReader();
        try {
            File directory = new File(dir);
            String[] subfiles = directory.list();
            for (int i = 0; i < subfiles.length; i++) {
                filename = subfiles[i];
                filepath = directory.getPath() + "/" + filename;
                file = new File(filepath);
                if (filename.toLowerCase().endsWith(".pdb")
                        || filename.toLowerCase().endsWith(".pdb.gz")
                        || filename.toLowerCase().endsWith(".ent")
                        || filename.toLowerCase().endsWith(".ent.gz")) {
                    System.out.println("Processing: " + filename);
                    Protein protein = pdb_reader.ProcessFile(filepath);
                    if (protein != null) {
                        System.out.println("OK");
                    } else {
                        System.out.println("NO PROCESSED");
                    }
                }
            }

        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }
    }

}
