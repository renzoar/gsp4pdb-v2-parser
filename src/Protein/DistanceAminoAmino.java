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
public class DistanceAminoAmino {
    public Aminoacid amino1;
    public Aminoacid amino2;
    public double dist = 0.0;
    
    public DistanceAminoAmino(Aminoacid aminoacid1, Aminoacid aminoacid2, Double distance){
        amino1 = aminoacid1;
        amino2 = aminoacid2;
        dist = distance;
    }
    
}
