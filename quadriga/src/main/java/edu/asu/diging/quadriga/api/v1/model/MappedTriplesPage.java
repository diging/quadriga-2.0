package edu.asu.diging.quadriga.api.v1.model;

import java.util.List;

import edu.asu.diging.quadriga.core.model.DefaultMapping;

public class MappedTriplesPage {

    private List<DefaultMapping> triples;
    private int currentPage;
    private int totalPages;

    public List<DefaultMapping> getTriples() {
        return triples;
    }

    public void setTriples(List<DefaultMapping> triples) {
        this.triples = triples;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

}
