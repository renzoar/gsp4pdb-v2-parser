/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Protein;

/**
 *
 * @author renzo
 */
public class AminoStandard {
    public String symbol;
    public String abbreviation;
    public String name;
    public String classification;
    public int class_number;

    public AminoStandard(String amino_symbol, String amino_abrv,String amino_name, String amino_class, int amino_class_number){
        symbol = amino_symbol;
        abbreviation = amino_abrv;
        name = amino_name;
        classification = amino_class;
        class_number = amino_class_number;
    }
    
}
