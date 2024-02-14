package edu.asu.diging.quadriga.api.v1.model;

import java.util.List;


/*
 * edu.asu.diging.quadriga.api.v1.MapGraphToTripleController requires a list of patterns from the user of type @PatternMapping 
 */
public class PatternMappingList {

    private List<PatternMapping> patternMappings;

    public List<PatternMapping> getPatternMappings() {
        return patternMappings;
    }

    public void setPatternMappings(List<PatternMapping> patternMappings) {
        this.patternMappings = patternMappings;
    }
    
}
