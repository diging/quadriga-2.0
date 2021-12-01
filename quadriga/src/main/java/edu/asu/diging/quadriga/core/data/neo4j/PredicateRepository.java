package edu.asu.diging.quadriga.core.data.neo4j;

import java.util.List;

import org.springframework.data.neo4j.repository.Neo4jRepository;

import edu.asu.diging.quadriga.core.model.mapped.Predicate;

public interface PredicateRepository extends Neo4jRepository<Predicate, Long> {

    List<Predicate> findByMappedCollectionId(String mappedCollectionId);
    
    List<Predicate> findBySourceUriOrTargetUriAndMappedCollectionId(String uri, String mappedCollectionId);
    
}
