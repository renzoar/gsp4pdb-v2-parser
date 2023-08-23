
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
public class Test {

    public static void main(String[] args) throws StructureException {
        PsqlDriver driver = new PsqlDriver();
        driver.testConnection();
        driver.OpenConnection();
        PdbReader pdb_reader = new PdbReader();
        long proc_begin = System.currentTimeMillis();
        System.out.println("inicio");
        Protein protein = pdb_reader.ProcessFile("./1pca.pdb");
        if (protein != null) {
            System.out.println("ok");
            driver.ExportProtein(protein);
        }
        long proc_time = System.currentTimeMillis() - proc_begin;
        System.out.println("fin");
        System.out.println("Processed in: " + proc_time + " ms");
        driver.CloseConnection();
    }

}
