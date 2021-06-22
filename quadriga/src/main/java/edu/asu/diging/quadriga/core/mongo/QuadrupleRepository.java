package edu.asu.diging.quadriga.core.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import edu.asu.diging.quadriga.network.model.Network;
import edu.asu.diging.quadriga.network.model.Quadruple;

@Repository
public interface QuadrupleRepository extends MongoRepository<Quadruple, String>{

}
