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
		ConceptCache conceptCache = conceptCacheRepository.findById(uri).orElse(null);
		if(conceptCache == null) {
			conceptCache = getConceptByAlternativeUri(uri);
		}
		return conceptCache;
	}

	@Override
	public ConceptCache getConceptByAlternativeUri(String uri) {
		List<ConceptCache> conceptCacheList = conceptCacheRepository.findConceptByAlternativeURI(uri);
		if(conceptCacheList != null && !conceptCacheList.isEmpty()) {
			return conceptCacheList.get(0);
		}
		return null;
	}

	@Override
	public void saveConceptCache(ConceptCache conceptCache) {
		conceptCacheRepository.save(conceptCache);
	}

	@Override
	public void deleteConceptCacheByUri(String uri) {
		if(uri != null && !uri.isBlank()) {
			conceptCacheRepository.deleteById(uri);
		}
	}

}
