package edu.asu.diging.quadriga.api.v1.model;

import edu.asu.diging.quadriga.api.v1.model.Context;

public class NodeData {

	private String label;
	private NodeMetadata metadata;
	private Context context;
	
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    public NodeMetadata getMetadata() {
        return metadata;
    }
    public void setMetadata(NodeMetadata metadata) {
        this.metadata = metadata;
    }
    public Context getContext() {
        return context;
    }
    public void setContext(Context context) {
        this.context = context;
    }
	
}
