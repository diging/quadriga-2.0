package edu.asu.diging.quadriga.core.data.neo4j;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.asu.diging.quadriga.core.model.mapped.Predicate;

@Repository
public interface PredicateRepository extends Neo4jRepository<Predicate, Long> {

    @Query("MATCH (p1:Concept)-[r]->(p2:Concept)"
            + "WHERE r.mappedTripleGroupId = $mappedTripleGroupId AND (p1.uri=$uri OR p2.uri=$uri) AND NOT p1.uri IN $ignoreList AND NOT p2.uri IN $ignoreList "
            + "RETURN r;")
    List<Predicate> findBySourceUriOrTargetUriAndMappedTripleGroupId(@Param("uri") String uri, @Param("ignoreList") List<String> ignoreList,
            @Param("mappedTripleGroupId") String mappedTripleGroupId);

    List<Predicate> findBySourceUriAndMappedTripleGroupIdOrTargetUriAndMappedTripleGroupId(String sourceUri,
            String mappedTripleGroupId1, String targetUri, String mappedTripleGroupId2);

    public Optional<List<Predicate>> findByMappedTripleGroupId(String mappedTripleGroupId);
    
    public Optional<Page<Predicate>> findByMappedTripleGroupId(String mappedTripleGroupId, Pageable paging);
    

    @Query("MATCH (p{mappedTripleGroupId:$mappedTripleGroupId})-[r:PREDICATE]->() RETURN COUNT(p)")
    public int countPredicatesByMappedTripleGroup(@Param("mappedTripleGroupId") String mappedTripleGroupId);
}
