package edu.asu.diging.quadriga.api.v1.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown=true)
public class TermPart {

    private long position;
    private String expression;
    private String normalization;
    private String formattedPointer;
    private String format;
    
    public long getPosition() {
        return position;
    }
    public void setPosition(long position) {
        this.position = position;
    }
    public String getExpression() {
        return expression;
    }
    public void setExpression(String expression) {
        this.expression = expression;
    }
    public String getNormalization() {
        return normalization;
    }
    public void setNormalization(String normalization) {
        this.normalization = normalization;
    }
    public String getFormattedPointer() {
        return formattedPointer;
    }
    public void setFormattedPointer(String formatted_pointer) {
        this.formattedPointer = formatted_pointer;
    }
    public String getFormat() {
        return format;
    }
    public void setFormat(String format) {
        this.format = format;
    }
    
}
