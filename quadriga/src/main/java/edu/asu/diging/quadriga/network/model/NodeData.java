package edu.asu.diging.quadriga.network.model;

import java.util.List;

public class NodeData {

    private String nodeName;

    private String interpretation;

    private String term;
    
    private List<String> certainity;

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public String getInterpretation() {
		return interpretation;
	}

	public void setInterpretation(String interpretation) {
		this.interpretation = interpretation;
	}

	public String getTerm() {
		return term;
	}

	public void setTerm(String term) {
		this.term = term;
	}

	public List<String> getCertainity() {
		return certainity;
	}

	public void setCertainity(List<String> certainity) {
		this.certainity = certainity;
	}

}
