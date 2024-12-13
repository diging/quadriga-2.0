package edu.asu.diging.quadriga.core.model.conceptpower;

import java.util.List;

/**
 * Holds the list of concepts retrieved from Conceptpower
 * @author Maulik Limbadiya
 *
 */
public class ConceptpowerResponse {
    
    private List<ConceptEntry> conceptEntries;

    public List<ConceptEntry> getConceptEntries() {
        return conceptEntries;
    }

    public void setConceptEntries(List<ConceptEntry> conceptEntries) {
        this.conceptEntries = conceptEntries;
    }

}
