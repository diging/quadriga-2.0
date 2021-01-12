package edu.asu.diging.quadriga.domain.elements;

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

//import org.neo4j.ogm.annotation.GraphId;
//import org.neo4j.ogm.annotation.NodeEntity;
//import org.neo4j.ogm.annotation.Property;
//import org.neo4j.ogm.annotation.Relationship;
//import org.neo4j.ogm.annotation.typeconversion.Convert;
//
//import edu.asu.qstore4s.db.neo4j.converters.ConceptConverter;
//import edu.asu.qstore4s.db.neo4j.converters.SourceReferenceConverter;
//import edu.asu.qstore4s.db.neo4j.converters.VocabularyEntryConverter;

/**
 * This file contains the definition of Term class.
 *
 */
@XmlRootElement
public class Term extends Element {

    Long graphId;

    //@Property(name = "interpretation")
    //@Convert(ConceptConverter.class)
    private Concept interpretation;

    //@Property(name = "normalized_representation")
    //@Convert(VocabularyEntryConverter.class)
    private VocabularyEntry normalized_representation;

    //@Relationship(type = "hasTermParts", direction = Relationship.OUTGOING)
    private TermParts printedRepresentation;

    //@Property(name = "certain")
    private Boolean certain;

    //@Property(name = "datatype")
    private String datatype;

    //@Relationship(type = "referencedTerm", direction = Relationship.OUTGOING)
    private Set<Term> referencedTerms;

    //@Property(name = "source_reference")
    //@Convert(SourceReferenceConverter.class)
    private SourceReference source_reference;

    public Term() {
        referencedTerms = new HashSet<Term>();
    }

    public Concept getInterpretation() {
        return interpretation;
    }

    @XmlElement(type = Concept.class)
    public void setInterpretation(Concept concept) {
        this.interpretation = concept;
    }

    @XmlElement(type = VocabularyEntry.class)
    public VocabularyEntry getNormalizedRepresentation() {
        return normalized_representation;
    }

    public void setNormalizedRepresentation(VocabularyEntry entry) {
        this.normalized_representation = entry;
    }

    @XmlElement(type = TermParts.class)
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

    @XmlElement(type = Term.class)
    public Set<Term> getReferencedTerms() {
        return referencedTerms;
    }

    public void setReferencedTerms(Set<Term> terms) {
        this.referencedTerms = terms;
    }

    @XmlElement(type = SourceReference.class)
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