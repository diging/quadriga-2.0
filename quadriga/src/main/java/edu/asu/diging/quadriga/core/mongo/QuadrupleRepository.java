package edu.asu.diging.quadriga.core.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.quadriga.api.v1.model.Graph;
import edu.asu.diging.quadriga.api.v1.model.Quadruple;

@Repository
public interface QuadrupleRepository extends MongoRepository<Quadruple, String>{

}
