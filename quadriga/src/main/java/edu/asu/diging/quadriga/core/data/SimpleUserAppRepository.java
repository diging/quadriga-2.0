package edu.asu.diging.quadriga.core.data;

import java.util.List;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.quadriga.core.model.users.SimpleUserApp;


@Repository
public interface SimpleUserAppRepository extends PagingAndSortingRepository<SimpleUserApp, String> {
    
    public List<SimpleUserApp> findByUsername(String username);
    public SimpleUserApp findByUsernameAndAppClientId(String username, String appClientId);
    public void deleteByUsernameAndAppClientId(String username, String appClientId);

}
