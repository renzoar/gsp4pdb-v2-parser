/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Protein;

import java.util.HashMap;

/**
 *
 * @author renzo
 */
public class AminoStandardList {

    private HashMap<String, AminoStandard> aminos;

    public AminoStandardList() {
        //symbol, abbreviation, name, classification, class_number
        aminos = new HashMap();
        aminos.put("ALA", new AminoStandard("A", "ALA", "ALANINE", "NON_POLAR", 1));
        aminos.put("ARG", new AminoStandard("R", "ARG", "ARGININE", "POSITIVELY CHARGED", 3));
        aminos.put("ASN", new AminoStandard("N", "ASN", "ASPARAGINE", "POLAR", 2));
        aminos.put("ASP", new AminoStandard("D", "ASP", "ASPARTIC ACID", "NEGATIVELY CHARGED", 4));
        aminos.put("CYS", new AminoStandard("C", "CYS", "CYSTEINE", "POLAR", 2));
        aminos.put("GLN", new AminoStandard("Q", "GLN", "GLUTAMINE", "POLAR", 2));
        aminos.put("GLU", new AminoStandard("E", "GLU", "GLUTAMIC ACID", "NEGATIVELY CHARGED", 4));
        aminos.put("GLY", new AminoStandard("G", "GLY", "GLYCINE", "POLAR", 2));
        aminos.put("HIS", new AminoStandard("H", "HIS", "HISTIDINE", "POSITIVELY CHARGED", 3));
        aminos.put("ILE", new AminoStandard("I", "ILE", "ISOLEUCINE", "NON-POLAR", 1));
        aminos.put("LEU", new AminoStandard("L", "LEU", "LEUCINE", "NON-POLAR", 1));
        aminos.put("LYS", new AminoStandard("K", "LYS", "LYSINE", "POSITIVELY CHARGED", 3));
        aminos.put("MET", new AminoStandard("M", "MET", "METHIONINE", "NON-POLAR", 1));
        aminos.put("PHE", new AminoStandard("F", "PHE", "PHENYLALANINE", "NON-POLAR", 1));
        aminos.put("PRO", new AminoStandard("P", "PRO", "PROLINE", "NON-POLAR", 1));
        aminos.put("SER", new AminoStandard("S", "SER", "SERINE", "POLAR", 2));
        aminos.put("THR", new AminoStandard("T", "THR", "THREONINE", "POLAR", 2));
        aminos.put("TRP", new AminoStandard("W", "TRP", "TRYPTOPHAN", "NON-POLAR", 1));
        aminos.put("TYR", new AminoStandard("Y", "TYR", "TYROSINE", "POLAR", 2));
        aminos.put("VAL", new AminoStandard("V", "VAL", "VALINE", "NON-POLAR", 1));
    }
    
    public String GetSymbolByAbbreviation(String abbreviation){
        AminoStandard amino = aminos.get(abbreviation);
        if(amino == null){
            System.out.println("Undefined ligand: " + abbreviation + " (registered as [U]NDEFINED)");
            return "UNDEFINED";
        }else{
            return amino.symbol;
        }
    }
    
    public String GetClassificationByAbbreviation(String abbreviation){
        AminoStandard amino = aminos.get(abbreviation);
        if(amino == null){
            return "UNDEFINED";
        }else{
            return amino.classification;
        }
    }    
    
    public int GetClassNumberByAbbreviation(String abbreviation){
        AminoStandard amino = aminos.get(abbreviation);
        if(amino == null){
            return 0;
        }else{
            return amino.class_number;
        }
    }    

}
