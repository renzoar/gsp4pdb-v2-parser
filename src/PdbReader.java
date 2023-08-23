
import Protein.AminoStandardList;
import Protein.Aminoacid;
import Protein.AtomAmino;
import Protein.AtomHet;
import Protein.DistanceAminoAmino;
import Protein.DistanceHetAmino;
import Protein.Hetam;
import Protein.Protein;
import Protein.SChain;
import com.csvreader.CsvWriter;
import java.io.BufferedWriter;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.biojava.nbio.structure.Atom;
import static org.biojava.nbio.structure.Calc.centerOfMass;
import org.biojava.nbio.structure.Chain;
import org.biojava.nbio.structure.ExperimentalTechnique;
import org.biojava.nbio.structure.Group;
import org.biojava.nbio.structure.GroupType;
import org.biojava.nbio.structure.Structure;
import org.biojava.nbio.structure.io.PDBFileReader;
//import org.biojava.nbio.structure.StructureException;
//import org.biojava.nbio.structure.io.PDBFileReader;

/**
 *
 * @author Renzo Angles
 */
public class PdbReader {

    Double max_dist = 7.0;

    static int Numero;
    public String idPDB;

    String listAminosChain;
    String idCadena;

    static boolean siHetatm;

    static String tipo;
    static String componente;
    static String formatoarchivo;

    int SizeAminos;
    int SizeHet;
    static int transformados = 0;
    static int descartados = 0;

    int contadorAmino = 0;

    List<Atom> atomsCA;
    List<Group> ligandos;

    BufferedWriter file;

    CsvWriter csvOutputProteinData;
    CsvWriter csvOutputChainData;
    CsvWriter csvOutputAminoacidData;
    CsvWriter csvOutputAtomaminoData;
    CsvWriter csvOutputHetData;
    CsvWriter csvOutputAtomhetData;

    // counters for statistics
    long csv_files_size;
    long rows_protein;
    long rows_chain;
    long rows_amino;
    long rows_het;
    long rows_atom_amino;
    long rows_atom_het;

    private AminoStandardList amino_standard_list;

    public PdbReader() {
        amino_standard_list = new AminoStandardList();
    }

    public Protein ProcessFile(String file_path) {
        PDBFileReader pdbreader = new PDBFileReader();
        pdbreader.setPath("../pdb-files");
        try {
            Structure struc = pdbreader.getStructure(file_path);
            Protein protein = this.processProtein(struc);
            return protein;
        } catch (Exception e) {
            Log.write("PdbReader.java", "ProcessFile", e.getMessage());
        }
        return null;
    }

    private Protein processProtein(Structure structure) {
        Protein new_protein = new Protein();
        new_protein.id = structure.getPDBCode();

        String title = structure.getPDBHeader().getTitle();
        if (title != null) {
            new_protein.title = title.replaceAll("\"", "").replace("'", "");
        } else {
            new_protein.title = "UNDEFINED";
        }

        String classification = structure.getPDBHeader().getClassification();
        if (classification != null) {
            new_protein.classification = classification.replaceAll("\"", "").replaceAll("'", "");
        } else {
            new_protein.classification = "UNDEFINED";
        }

        String organism = structure.getCompounds().get(0).getOrganismScientific();
        if (organism != null) {
            new_protein.organism = organism.replaceAll("\"", "").replaceAll("'", "");
        } else {
            new_protein.organism = "UNDEFINED";
        }

        new_protein.technique = "UNDEFINED";
        Set<ExperimentalTechnique> techniques = structure.getPDBHeader().getExperimentalTechniques();
        if (!techniques.isEmpty()) {
            new_protein.technique = techniques.iterator().next().getName().replaceAll("\"", "");
        }
        SimpleDateFormat fd = new SimpleDateFormat("dd-MM-yyyy");
        try {
            new_protein.dep_date = fd.format(structure.getPDBHeader().getDepDate());
            new_protein.mod_date = fd.format(structure.getPDBHeader().getModDate());
        } catch (Exception ex) {
            System.out.println("Error:" + ex.getMessage());
        }

        int totalCadenas = structure.getChains().size();
        if (totalCadenas == 0) {
            String error = "The protein " + new_protein.id + " has not chains";
            Log.write("PdbReader.java", "PdbReader", error);
            return null;
        }
        
        //process chains
        Chain chain;
        SChain new_chain;
        SChain main_schain = null;
        int max_aminos = 0;
        int max_hetams = 0;
        Iterator<Chain> itc = structure.getChains().iterator();
        while(itc.hasNext()){
            chain = itc.next();
            new_chain = this.processChain(chain, new_protein);
            if(new_chain.aminoacids.size() > max_aminos && new_chain.hetams.size() > max_hetams){
                max_aminos = new_chain.aminoacids.size();
                max_hetams = new_chain.hetams.size();
                main_schain = new_chain;
            }
        }
        
        if (main_schain == null) {
            String error = "The protein " + new_protein.id + " has not main chain";
            Log.write("PdbReader.java", "processProtein", error);
        }else{
            new_protein.main_chain = main_schain; 
        }
        return new_protein;
    }

    private SChain processChain(Chain chain, Protein new_protein) {
        int counter = 1;
        Aminoacid previous_amino = null;
        SChain new_chain = new SChain();
        new_chain.id = new_protein.id + "_" + chain.getChainID();
        new_chain.code = chain.getChainID();
        new_chain.protein_id = new_protein.id;

        //verify that the chain has aminoacids
        if (chain.getAtomGroups(GroupType.AMINOACID).isEmpty()) {
            return new_chain;
        } else {
            List<Group> aminos = chain.getAtomGroups(GroupType.AMINOACID);
            new_chain.num_amino = aminos.size();
            for (int i = 0; i < aminos.size(); i++) {
                Group amino = aminos.get(i);
                Aminoacid new_amino = this.processAmino(amino, new_chain, counter);
                counter++;
                if (previous_amino != null) {
                    previous_amino.next_amino = new_amino;
                }
                previous_amino = new_amino;
                new_chain.seqres = new_chain.seqres + new_amino.symbol;
                new_chain.aminoacids.add(new_amino);
            }
        }

        //verify that the chain has ligands
        if (chain.getAtomGroups(GroupType.HETATM).isEmpty()) {
            return new_chain;
        } else {
            List<Group> hetams = chain.getAtomGroups(GroupType.HETATM);
            new_chain.num_het = hetams.size();
            for (int i = 0; i < hetams.size(); i++) {
                Group hetam = hetams.get(i);
                //Avoid water molecules
                if (!hetam.getPDBName().equals("HOH")) {
                    Hetam new_hetam = this.processHetam(hetam, new_chain, counter);
                    counter++;
                    new_chain.hetams.add(new_hetam);
                }
            }
        }
        //process amino-amino and amino-het distances
        this.ComputeDistances(new_chain);

        return new_chain;
    }

    private Aminoacid processAmino(Group amino, SChain new_chain, int position) {
        Aminoacid new_amino = new Aminoacid();
        new_amino.id = new_chain.id + "_" + position;
        new_amino.number = amino.getResidueNumber().toString(); 
        new_amino.chain_id = new_chain.id;
        new_amino.position = position;
        new_amino.symbol = amino_standard_list.GetSymbolByAbbreviation(amino.getPDBName()).replace(" ", "");
        if (new_amino.symbol.compareTo("UNDEFINED") == 0) {
            new_amino.symbol = "U";
        }
        new_amino.classification = amino_standard_list.GetClassificationByAbbreviation(amino.getPDBName());
        new_amino.class_number = amino_standard_list.GetClassNumberByAbbreviation(amino.getPDBName());
        new_amino.protein_id = new_chain.protein_id;
        //to process the atoms of the amino
        Iterator<Atom> it = amino.iterator();
        while (it.hasNext()) {
            Atom atom = it.next();
            AtomAmino atom_amino = this.processAtomAmino(atom, new_amino);
            new_amino.atoms.add(atom_amino);
            if (atom_amino.symbol.compareTo("CA") == 0) {
                new_amino.x = atom_amino.x;
                new_amino.y = atom_amino.y;
                new_amino.z = atom_amino.z;
            }
        }
        return new_amino;
    }

    private AtomAmino processAtomAmino(Atom atom, Aminoacid new_amino) {
        AtomAmino atom_amino = new AtomAmino();
        atom_amino.id = new_amino.id + "_" + String.valueOf(atom.getPDBserial());
        atom_amino.symbol = atom.getName().replace(" ", "").replace("'", "");
        atom_amino.number = String.valueOf(atom.getPDBserial());
        double[] coord = atom.getCoords();
        atom_amino.x = coord[0];
        atom_amino.y = coord[1];
        atom_amino.z = coord[2];
        atom_amino.element = atom.getElement().toString();
        atom_amino.amino_id = new_amino.id;
        return atom_amino;
    }

    private Hetam processHetam(Group hetam, SChain new_chain, int counter) {
        Hetam new_hetam = new Hetam();
        new_hetam.id = new_chain.id + "_" + counter;
        new_hetam.number = hetam.getResidueNumber().toString();
        new_hetam.chain_id = new_chain.id;
        new_hetam.protein_id = new_chain.protein_id;
        new_hetam.symbol = hetam.getPDBName().replace(" ", "").replace("'", "");
        new_hetam.num_atom = hetam.size();
        Iterator<Atom> it = hetam.iterator();
        Atom[] atoms = new Atom[hetam.size()];
        int i = 0;
        while (it.hasNext()) {
            Atom atom = it.next();
            atoms[i] = atom;
            i++;
            AtomHet atom_het = this.processAtomHet(atom, new_hetam);
            new_hetam.atoms.add(atom_het);
        }
        Atom center = centerOfMass(atoms);
        double[] coord = center.getCoords();
        new_hetam.x = coord[0];
        new_hetam.y = coord[1];
        new_hetam.z = coord[2];
        return new_hetam;
    }

    private AtomHet processAtomHet(Atom atom, Hetam new_hetam) {
        AtomHet atom_het = new AtomHet();
        atom_het.id = new_hetam.id + "_" + atom.getPDBserial();
        atom_het.symbol = atom.getName().replace(" ", "").replace("'", "");
        atom_het.number = atom.getPDBserial();
        double[] coord = atom.getCoords();
        atom_het.x = coord[0];
        atom_het.y = coord[1];
        atom_het.z = coord[2];
        atom_het.element = atom.getElement().toString();
        atom_het.het_id = new_hetam.id;
        return atom_het;
    }

    private double computeDistance(double x1, double y1, double z1, double x2, double y2, double z2) {
        double x = x1 - x2;
        double y = y1 - y2;
        double z = z1 - z2;
        double s = x * x + y * y + z * z;
        double dist = Math.sqrt(s);
        return dist;
    }

    private void ComputeDistances(SChain new_chain) {
        Aminoacid amino1;
        Aminoacid amino2;
        double min_dist = 0.0;
        Hetam het;
        double dist;
        for (int i = 0; i < new_chain.aminoacids.size(); i++) {
            amino1 = new_chain.aminoacids.get(i);
            //compute amino-amino distances
            for (int j = i + 1; j < new_chain.aminoacids.size(); j++) {
                amino2 = new_chain.aminoacids.get(j);
                // compute atom_amino atom_amino distances
                min_dist = 100;                
                Iterator<AtomAmino> it1 = amino1.atoms.iterator();
                while (it1.hasNext()) {
                    AtomAmino atom1 = it1.next();
                    Iterator<AtomAmino> it2 = amino2.atoms.iterator();
                    while (it2.hasNext()) {
                        AtomAmino atom2 = it2.next();
                        dist = computeDistance(atom1.x, atom1.y, atom1.z, atom2.x, atom2.y, atom2.z);
                        if (dist <= min_dist) {
                            min_dist = dist;
                        }
                    }
                }
                if (min_dist <= 7) {
                    DistanceAminoAmino daa = new DistanceAminoAmino(amino1, amino2, min_dist);
                    new_chain.daa_list.add(daa);
                }
            }

            //compute het-amino distances
            for (int k = 0; k < new_chain.hetams.size(); k++) {
                het = new_chain.hetams.get(k);
                // compute atom_het atom_amino distances
                min_dist = 100;
                Iterator<AtomHet> it1 = het.atoms.iterator();
                while (it1.hasNext()) {
                    AtomHet atom_het = it1.next();
                    Iterator<AtomAmino> it2 = amino1.atoms.iterator();
                    while (it2.hasNext()) {
                        AtomAmino atom_amino = it2.next();
                        dist = computeDistance(atom_het.x, atom_het.y, atom_het.z, atom_amino.x, atom_amino.y, atom_amino.z);
                        if (dist <= min_dist) {
                            min_dist = dist;
                        }
                    }
                }
                if (min_dist <= 7) {
                    DistanceHetAmino dha = new DistanceHetAmino(het, amino1, min_dist);
                    new_chain.dha_list.add(dha);
                }
            }
        }
    }

}
