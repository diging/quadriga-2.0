package edu.asu.diging.quadriga.core.citesphere;

import java.util.List;

import org.springframework.web.client.HttpClientErrorException;

import edu.asu.diging.quadriga.core.model.citesphere.CitesphereAppInfo;

public interface ICitesphereConnector {

//    boolean hasAccess(String documentId, String username);

//    <T> T sendRequest(String endpoint, Map<String, String> parameters,
//            Class<T> responseType) throws HttpClientErrorException;

    List<CitesphereAppInfo> getCitesphereApps() throws HttpClientErrorException;
}