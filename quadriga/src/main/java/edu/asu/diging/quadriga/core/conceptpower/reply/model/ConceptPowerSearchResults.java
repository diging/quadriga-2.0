package edu.asu.diging.quadriga.core.conceptpower.reply.model;

import java.util.List;

public class ConceptPowerSearchResults {

    private List<ConceptEntry> conceptEntries;

    private PaginationInfo pagination;

    public List<ConceptEntry> getConceptEntries() {
        return conceptEntries;
    }

    public void setConceptEntries(List<ConceptEntry> conceptEntries) {
        this.conceptEntries = conceptEntries;
    }

    public PaginationInfo getPagination() {
        return pagination;
    }

    public void setPagination(PaginationInfo pagination) {
        this.pagination = pagination;
    }

    class PaginationInfo {

        private int pageNumber;
        private int totalNumberOfRecords;
        
        public PaginationInfo() {
            
        }

        public int getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(int pageNumber) {
            this.pageNumber = pageNumber;
        }

        public int getTotalNumberOfRecords() {
            return totalNumberOfRecords;
        }

        public void setTotalNumberOfRecords(int totalNumberOfRecords) {
            this.totalNumberOfRecords = totalNumberOfRecords;
        }

    }
}
