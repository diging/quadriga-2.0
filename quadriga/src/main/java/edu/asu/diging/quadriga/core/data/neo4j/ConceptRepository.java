package edu.asu.diging.quadriga.core.data.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.quadriga.core.model.mapped.Concept;

@Repository
public interface ConceptRepository extends Neo4jRepository<Concept, Long> {
    
    
    public Concept findByMappedTripleGroupId(String mappedTripleGroupId);
    
    
    
    

}
