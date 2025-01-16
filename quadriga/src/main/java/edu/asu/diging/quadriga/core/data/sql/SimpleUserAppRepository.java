package edu.asu.diging.quadriga.core.data.sql;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import edu.asu.diging.quadriga.core.model.users.SimpleUserApp;


@Repository
public interface SimpleUserAppRepository extends PagingAndSortingRepository<SimpleUserApp, String> {
    
    public List<SimpleUserApp> findByUsername(String username);
    
    public SimpleUserApp findByUsernameAndAppClientId(String username, String appClientId);
    
    public void deleteByUsernameAndAppClientId(String username, String appClientId);
    
    @Query(value = "SELECT * FROM SimpleUserApp WHERE username = :username ORDER BY id LIMIT :pageSize OFFSET :offset", nativeQuery = true)
    public List<SimpleUserApp> findByUsernameWithPagination(@Param("username") String username, @Param("offset") int offset, @Param("pageSize") int pageSize);
    
    @Query(value="SELECT appClientId FROM SimpleUserApp WHERE username = :username",nativeQuery = true)
    public List<String> findAppClientIdsByUsername(@Param("username") String username);

}
