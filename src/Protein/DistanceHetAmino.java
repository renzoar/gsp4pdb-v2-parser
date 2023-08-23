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
public class DistanceHetAmino {
    public Hetam het;
    public Aminoacid amino;
    public double dist = 0.0;

    public DistanceHetAmino(Hetam _het, Aminoacid _amino, double distance){
        het = _het;
        amino = _amino;
        dist = distance;
    }
    
}
