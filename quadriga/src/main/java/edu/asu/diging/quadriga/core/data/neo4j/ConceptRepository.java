package edu.asu.diging.quadriga.core.data.neo4j;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import edu.asu.diging.quadriga.core.model.mapped.Concept;

public interface ConceptRepository extends Neo4jRepository<Concept, Long> {

    public Optional<List<Concept>> findByMappingTypeAndMappedCollectionIdAndUri(String mappingType, String mappedCollectionId, String uri);
}
