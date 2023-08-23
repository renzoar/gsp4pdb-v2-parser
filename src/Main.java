
import Protein.Protein;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.biojava.nbio.structure.StructureException;

/**
 *
 * @author Renzo Angles
 */
public class Main {

    public static void main(String[] args) throws StructureException {
        if (args.length == 1) {
            String dir = args[0];
            Parser parser = new Parser();
            parser.Run(dir);
        } else if (args.length == 2) {
            String dir = args[0];
            String pdb_list_file = args[1];
            Parser parser = new Parser();
            parser.Run2(dir,pdb_list_file);
        } else {
            System.out.println("Execute with:");
            System.out.println("java -jar gsp4pdb-parser.jar [ListFile]");
            System.out.println("[inputDirectory] (mandatory) : the directory where the input PDB files are located.");
            System.out.println("[ListFile] (optional) : a file containing a list of PDB-ID's to be loaded.");            
            return;
        }
    }

}
