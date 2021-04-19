package edu.asu.diging.quadriga.core.data;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.quadriga.core.model.impl.App;

@Repository
public interface AppRepository extends PagingAndSortingRepository<App, String>{


}
