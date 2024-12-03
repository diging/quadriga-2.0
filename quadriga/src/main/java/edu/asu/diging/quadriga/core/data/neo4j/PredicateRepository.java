package edu.asu.diging.quadriga.core.data.neo4j;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.data.repository.query.Param;

import edu.asu.diging.quadriga.core.model.mapped.Predicate;

public interface PredicateRepository extends Neo4jRepository<Predicate, Long> {

    public Optional<List<Predicate>> findByMappedTripleGroupId(String mappedTripleGroupId);
    
    public Optional<Page<Predicate>> findByMappedTripleGroupId(String mappedTripleGroupId, Pageable paging);

    @Query("MATCH (p{mappedTripleGroupId:$mappedTripleGroupId})-[r:PREDICATE]->() RETURN COUNT(p)")
    public int countPredicatesByMappedTripleGroup(@Param("mappedTripleGroupId") String mappedTripleGroupId);
}
