package edu.asu.diging.quadriga.network.model;

public class Network {
	
	private long id;
	
	private Context context;
	
	private NodeData nodeData;

    private String interpretationCreator;

    private String relationCreator;

    private DefaultMapping defaultMapping;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Context getContext() {
		return context;
	}

	public void setContext(Context context) {
		this.context = context;
	}

	public NodeData getNodeData() {
		return nodeData;
	}

	public void setNodeData(NodeData nodeData) {
		this.nodeData = nodeData;
	}

	public String getInterpretationCreator() {
		return interpretationCreator;
	}

	public void setInterpretationCreator(String interpretationCreator) {
		this.interpretationCreator = interpretationCreator;
	}

	public String getRelationCreator() {
		return relationCreator;
	}

	public void setRelationCreator(String relationCreator) {
		this.relationCreator = relationCreator;
	}

	public DefaultMapping getDefaultMapping() {
		return defaultMapping;
	}

	public void setDefaultMapping(DefaultMapping defaultMapping) {
		this.defaultMapping = defaultMapping;
	}

    

}
