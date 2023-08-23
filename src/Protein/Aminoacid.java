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
public class Aminoacid {
    
    public String id;
    public String number;
    public String symbol;
    public int position;
    public Aminoacid next_amino = null;
    public String classification;
    public String chain_id;
    public String protein_id;
    public int class_number;
    public double x = 0.0;
    public double y = 0.0;
    public double z = 0.0;
    public ArrayList<AtomAmino> atoms = new ArrayList();
}
