package edu.asu.diging.quadriga.core.model.elements;

import java.util.HashSet;
import java.util.Set;


/**
 * This file contains the definition of Term class.
 *
 */
public class Term extends Element {

    private Concept interpretation;

    private VocabularyEntry normalized_representation;

    private TermParts printedRepresentation;

    private Boolean certain;

    private String datatype;

    private Set<Term> referencedTerms;

    private SourceReference source_reference;

    public Term() {
        referencedTerms = new HashSet<Term>();
    }

    public Concept getInterpretation() {
        return interpretation;
    }

    public void setInterpretation(Concept concept) {
        this.interpretation = concept;
    }

    public VocabularyEntry getNormalizedRepresentation() {
        return normalized_representation;
    }

    public void setNormalizedRepresentation(VocabularyEntry entry) {
        this.normalized_representation = entry;
    }

    public TermParts getPrintedRepresentation() {
        return printedRepresentation;
    }

    public void setPrintedRepresentation(TermParts parts) {
        this.printedRepresentation = parts;
    }

    public Boolean isCertain() {
        return certain;
    }

    /**
     * Getter method for hibernate.
     **/
    public Boolean getCertain() {
        return certain;
    }

    public void setIsCertain(Boolean certainty) {
        this.certain = certainty;
    }

    /**
     * Setter method for hibernate.
     **/
    public void setCertain(boolean certainty) {
        this.certain = certainty;
    }

    public Set<Term> getReferencedTerms() {
        return referencedTerms;
    }

    public void setReferencedTerms(Set<Term> terms) {
        this.referencedTerms = terms;
    }

    public SourceReference getSourceReference() {
        return source_reference;
    }

    public void setSourceReference(SourceReference reference) {
        this.source_reference = reference;
    }

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

}