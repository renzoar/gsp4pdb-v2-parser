
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.StructureException;
import org.biojava.nbio.structure.align.util.AtomCache;
import org.biojava.nbio.structure.io.PDBFileReader;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author renzo
 */
public class Test2 {

    public static void main(String[] args) throws StructureException {
        
        /*
        AtomCache cache = new AtomCache();
        cache.setPath("./datos");
        try {
            Structure s = cache.getStructure("1afr");
            System.out.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }*/

        
        String filename = "./datos/1afr.pdb";
        PDBFileReader pdbreader = new PDBFileReader();
        try {
            Structure struc = pdbreader.getStructure(filename);
            System.out.println("ok");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
