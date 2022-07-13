package edu.asu.diging.quadriga.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.conceptpower.ConceptpowerConnector;
import edu.asu.diging.quadriga.core.model.conceptpower.ConceptEntry;
import edu.asu.diging.quadriga.core.model.conceptpower.ConceptpowerResponse;
import edu.asu.diging.quadriga.core.service.ConceptFinder;

@Service
@PropertySource("classpath:config.properties")
public class ConceptFinderImpl implements ConceptFinder {

    @Value("${conceptpower_id_keyword}")
    private String conceptpowerKeyword;

    @Autowired
    private ConceptpowerConnector conceptpowerConnector;

    /* (non-Javadoc)
     * @see edu.asu.diging.quadriga.core.service.ConceptFinder#getConcept(java.lang.String)
     */
    @Override
    public ConceptEntry getConcept(String uri) {
        if (uri.contains(conceptpowerKeyword)) {
            return conceptpowerConnector.getConceptEntry(uri);
        } else {
            String currentUri = normalizeUri(uri);
            String nextUri = getNextFormat(currentUri);
            do {
                ConceptpowerResponse similarEntries = conceptpowerConnector.findConceptEqualTo(currentUri);
                if (similarEntries != null && similarEntries.getConceptEntries() != null
                        && !similarEntries.getConceptEntries().isEmpty()) {
                    return similarEntries.getConceptEntries().get(0);
                }
                currentUri = nextUri;
                nextUri = getNextFormat(nextUri);
            } while (!nextUri.equals(currentUri));
        }

        return null;
    }

    private String normalizeUri(String uri) {
        uri = uri.replaceFirst("https", "http");
        uri = uri.endsWith("/") ? uri.substring(0, uri.length() - 1) : uri;
        return uri;
    }

    private String getNextFormat(String uri) {
        boolean containsHttps = uri.startsWith("https");
        boolean endsWithSeparator = uri.endsWith("/");

        if (!endsWithSeparator) {
            return uri + "/";
        } else if (!containsHttps) {
            uri = uri.replaceFirst("http", "https");
            uri = uri.endsWith("/") ? uri.substring(0, uri.length() - 1) : uri;
        }

        return uri;
    }

}
