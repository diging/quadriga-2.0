package edu.asu.diging.quadriga.api.v1.model;

import java.util.List;

public class Network {

	private long id;

	private Context context;

	private List<NodeData> nodeData;

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

	public List<NodeData> getNodeData() {
		return nodeData;
	}

	public void setNodeData(List<NodeData> nodeData) {
		this.nodeData = nodeData;
	}

	public DefaultMapping getDefaultMapping() {
		return defaultMapping;
	}

	public void setDefaultMapping(DefaultMapping defaultMapping) {
		this.defaultMapping = defaultMapping;
	}



}
