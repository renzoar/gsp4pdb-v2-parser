/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Protein;

import java.util.ArrayList;

/**
 *
 * @author renzo
 */
public class SChain {
    public String id;
    public String code;
    public String protein_id;
    public String seqres = "";
    public int num_het;
    public int num_amino;
    //list of amino acids
    public ArrayList<Aminoacid> aminoacids = new ArrayList<>();
    //list of ligands
    public ArrayList<Hetam> hetams = new ArrayList<>();
    //list of amino-amino distances
    public ArrayList<DistanceAminoAmino> daa_list = new ArrayList<>();
    //list of het-amino distances
    public ArrayList<DistanceHetAmino> dha_list = new ArrayList<>();
    
}
