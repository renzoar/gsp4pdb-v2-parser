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
public class Hetam {
    public String id;
    public String symbol;
    public String number;
    public String chain_id;
    public String protein_id;
    public int num_atom;
    public double x = 0.0;
    public double y = 0.0;
    public double z = 0.0;  
    public ArrayList<AtomHet> atoms = new ArrayList();
}
