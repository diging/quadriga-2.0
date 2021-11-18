package edu.asu.diging.quadriga.core.conceptpower.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.conceptpower.model.ConceptCache;
import edu.asu.diging.quadriga.core.conceptpower.model.ConceptType;
import edu.asu.diging.quadriga.core.conceptpower.reply.model.ConceptEntry;
import edu.asu.diging.quadriga.core.conceptpower.reply.model.ConceptPowerReply;
import edu.asu.diging.quadriga.core.conceptpower.reply.model.Type;
import edu.asu.diging.quadriga.core.conceptpower.service.ConceptCacheService;
import edu.asu.diging.quadriga.core.conceptpower.service.ConceptPowerConnectorService;
import edu.asu.diging.quadriga.core.conceptpower.service.ConceptPowerService;
import edu.asu.diging.quadriga.core.conceptpower.service.ConceptTypeService;

@Service
public class ConceptPowerServiceImpl implements ConceptPowerService {
	
	@Autowired
	private ConceptCacheService conceptCacheService;
	
	@Autowired
	private ConceptTypeService conceptTypeService;
	
	@Autowired
	private ConceptPowerConnectorService conceptPowerConnectorService;

	@Override
	public ConceptCache getConceptByUri(String uri) {
		// Step 1. Check if Concept is present in DB (Done)
		// Step 2: If concept is present in DB & updated within last 2 days return concept
		// Step 3: If concept is present in DB & not updated within last 2 days:
		//			1. Call ConceptPower
		//			2. Update DB
		//			3. Return concept
		// Step 3: If concept is not present in DB (Done)
		//			1. Call ConceptPower
		//			2. Create entry in DB
		//			3. Return concept
		ConceptCache conceptCache = getConceptCacheEntry(uri);
		if(conceptCache == null) {
			ConceptPowerReply conceptPowerReply = conceptPowerConnectorService.getConceptPowerReply(uri);
			System.out.println("ConceptPower reply requested");
			conceptCache = mapConceptPowerReplyToConceptCache(conceptPowerReply);
			if(conceptCache != null) {
				conceptCacheService.saveConceptCache(conceptCache);
			}
			if(conceptCache.getConceptType() != null) {
				ConceptType conceptType = conceptCache.getConceptType();
				if(conceptType != null) {
					conceptTypeService.saveConceptType(conceptType);
				}
			}
		}
		return conceptCache;
	}
	
	/**
	 * This method gets a ConceptCache entry from the database
	 * If it is not present, it checks the alternative URIs to get a conceptCache entry
	 * 
	 * @param uri used to check in alternativeUris
	 * @return a conceptCache entry
	 */
	private ConceptCache getConceptCacheEntry(String uri) {
		ConceptCache conceptCache = conceptCacheService.getConceptByUri(uri);
		if(conceptCache == null) {
			
		}
		return conceptCache;
	}

	private ConceptCache mapConceptPowerReplyToConceptCache(ConceptPowerReply conceptPowerReply) {
		// If we get multiple ConceptPower entries in the reply, we use the first one
		List<ConceptEntry> conceptEntries = conceptPowerReply.getConceptEntries();
		ConceptCache conceptCache = null;
		
		if(conceptEntries != null && !conceptEntries.isEmpty()) {
			ConceptEntry conceptEntry = conceptEntries.get(0);
			conceptCache = new ConceptCache();
			conceptCache.setUri(conceptEntry.getConceptUri());
			conceptCache.setConceptList(conceptEntry.getConceptList());
			conceptCache.setDescription(conceptEntry.getDescription());
			conceptCache.setPos(conceptEntry.getPos());
			
			String typeId = "";
			if(conceptEntry.getType() != null) {
				if(conceptEntry.getType().getTypeUri() != null) {
					typeId = conceptEntry.getType().getTypeUri();
				}
			}
			conceptCache.setTypeId(typeId);
			conceptCache.setDeleted(conceptEntry.getDeleted());
			
			// get last part of URI = id
	        int index = conceptEntry.getConceptUri().lastIndexOf("/");
	        if (index != -1) {
	            conceptCache.setId(conceptEntry.getConceptUri().substring(index + 1));
	        }
	        conceptCache.setCreatorId(conceptEntry.getCreatorId());
	        conceptCache.setWord(conceptEntry.getLemma());
	        
	        if (conceptEntry.getWordnetId() != null && !conceptEntry.getWordnetId().trim().equals("")) {
	        	conceptCache.setWordNetIds(Arrays.asList(conceptEntry.getWordnetId().split(",")));
	        } else {
	        	conceptCache.setWordNetIds(new ArrayList<>());
	        }
	        
	        if(conceptEntry.getEqualTo() != null && !conceptEntry.getEqualTo().trim().equals("")) {
	        	conceptCache.setEqualTo(Arrays.asList(conceptEntry.getEqualTo().split(",")));
	        } else {
	        	conceptCache.setEqualTo(new ArrayList<>());
	        }
			
			conceptCache.setAlternativeUris(conceptEntry.getAlternativeIds()
					.stream()
					.map(alternativeId -> alternativeId.getConceptUri())
					.filter(alternativeUri -> alternativeUri != null && !alternativeUri.equals(""))
					.collect(Collectors.toList()));
			
			if(conceptEntry.getType() != null) {
				Type type = conceptEntry.getType();
				ConceptType conceptType = new ConceptType();
				conceptType.setUri(type.getTypeUri());
				conceptType.setId(type.getTypeId());
				conceptType.setName(type.getTypeName());
				conceptType.setDescription("");
				conceptCache.setConceptType(conceptType);
			}
		}
		return conceptCache;
	}

}
