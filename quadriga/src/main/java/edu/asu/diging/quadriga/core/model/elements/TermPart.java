package edu.asu.diging.quadriga.core.model.elements;

public class TermPart extends Element {

    private String formatted_pointer;
    private String format;
    private String expression;

    private Long position;

    private VocabularyEntry normalization;

    private SourceReference source_reference;

    public Long getPosition() {
        return position;
    }

    public String getExpression() {
        return expression;
    }

    public void setPosition(Long position) {
        this.position = position;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public void setNormalization(VocabularyEntry normalization) {
        this.normalization = normalization;
    }

    public VocabularyEntry getNormalization() {
        return normalization;
    }

    public void setFormattedPointer(String formattedPointer) {
        this.formatted_pointer = formattedPointer;
    }

    public String getFormattedPointer() {
        return formatted_pointer;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public SourceReference getSourceReference() {
        return source_reference;
    }

    public void setSourceReference(SourceReference reference) {
        this.source_reference = reference;
    }

}
