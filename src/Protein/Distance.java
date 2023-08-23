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
public class Distance {
    public String sourceId;
    public String targetId;
    public Double dist;
    
    public Distance(String source_id, String target_id, Double distance){
        this.sourceId = source_id;
        this.targetId = target_id;
        this.dist = distance;
    }
}
