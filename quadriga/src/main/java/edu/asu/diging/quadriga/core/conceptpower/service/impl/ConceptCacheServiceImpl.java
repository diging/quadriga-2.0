package edu.asu.diging.quadriga.core.conceptpower.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.conceptpower.data.ConceptCacheRepository;
import edu.asu.diging.quadriga.core.conceptpower.model.ConceptCache;
import edu.asu.diging.quadriga.core.conceptpower.service.ConceptCacheService;

@Service
public class ConceptCacheServiceImpl implements ConceptCacheService {
	
	@Autowired
	private ConceptCacheRepository conceptCacheRepository;

	@Override
	public ConceptCache getConceptByUri(String uri) {
		return conceptCacheRepository.findById(uri).orElse(null);
	}

	@Override
	public List<ConceptCache> getConceptsByAlternativeUri(String uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveConceptCache(ConceptCache conceptCache) {
		conceptCacheRepository.save(conceptCache);
	}

	@Override
	public ConceptCache updateConceptCache(String uri) {
		// TODO Auto-generated method stub
		return null;
	}

}
