package edu.asu.diging.quadriga.core.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import edu.asu.diging.quadriga.network.model.Network;

@Repository
public interface NetworkRepository extends MongoRepository<Network, String>{

}
