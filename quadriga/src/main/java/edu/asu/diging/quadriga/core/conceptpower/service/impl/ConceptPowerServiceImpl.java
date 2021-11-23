package edu.asu.diging.quadriga.core.conceptpower.service.impl;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public ConceptCache getConceptByUri(String uri) {

        ConceptCache conceptCache = conceptCacheService.getConceptByUri(uri);

        if (conceptCache == null || ChronoUnit.DAYS.between(conceptCache.getLastUpdated(), LocalDateTime.now()) >= 2) {
            saveConceptCacheFromConceptPowerReply(conceptCache, conceptPowerConnectorService.getConceptPowerReply(uri), uri);
        }
        return conceptCache;
    }

    /**
     * This method first saves the ConceptCache entry in the database which is
     * mapped from ConceptPowerReply
     * 
     * Then it saves the corresponding ConceptType entry in the database, if one
     * exists
     * 
     * After that, it checks if there are any concepts in the database which have
     * the same URI as this concept's alternativeURIs. If such concepts exist, they
     * will be deleted
     * 
     * E.g.: Consider => Concepts => {AlternativeURIs}
     * 
     * Existing Concepts in the database: C2 => {C2, C4}, C3 => {C3, C5}
     * 
     * New Concept to be added to the database: C1 => {C1, C2, C3, C5}
     * 
     * In this case, delete C2 and C3 from the database as C1 has listed C2 and C3
     * as alternatives If in the future, the concepts C2 or C3 are searched in the
     * database, C1 will be returned
     * 
     * @param conceptPowerReply is the object used to generate a ConceptCache object
     * @param uri               to be used for logging in case no ConceptCache entry
     *                          was generated
     */
    private void saveConceptCacheFromConceptPowerReply(ConceptCache conceptCacheOld, ConceptPowerReply conceptPowerReply, String uri) {
        ConceptCache conceptCache = mapConceptPowerReplyToConceptCache(conceptPowerReply);

        if (conceptCache != null || (conceptCacheOld != null && conceptCacheOld.compareTo(conceptCache) != 0)) {
            conceptCacheService.saveConceptCache(conceptCache);

            ConceptType conceptTypeOld = conceptCacheOld.getConceptType();
            ConceptType conceptType = conceptCache.getConceptType();
            
            if (conceptType != null || (conceptTypeOld != null && conceptTypeOld.compareTo(conceptType) != 0)) {
                conceptTypeService.saveConceptType(conceptType);
            }

            conceptCache.getAlternativeUris().stream()
                    .filter(alternativeUri -> alternativeUri != null && !alternativeUri.equals(conceptCache.getUri()))
                    .forEach(alternativeUriValue -> conceptCacheService.deleteConceptCacheByUri(alternativeUriValue));
        } else {
            logger.error("ConceptPower did not return any concept entries for uri: " + uri);
        }
    }

    /**
     * This method maps the ConceptPowerReply object returned from ConceptPower to a
     * ConceptCache object that would be stored in the database
     * 
     * @param conceptPowerReply is the object used to generate a ConceptCache object
     * @return the generated ConceptCache object
     */
    private ConceptCache mapConceptPowerReplyToConceptCache(ConceptPowerReply conceptPowerReply) {
        // If we get multiple ConceptPower entries in the reply, we use the first one
        List<ConceptEntry> conceptEntries = conceptPowerReply.getConceptEntries();
        ConceptCache conceptCache = null;

        if (conceptEntries != null && !conceptEntries.isEmpty()) {
            ConceptEntry conceptEntry = conceptEntries.get(0);
            conceptCache = new ConceptCache();
            conceptCache.setUri(conceptEntry.getConceptUri());
            conceptCache.setConceptList(conceptEntry.getConceptList());
            conceptCache.setDescription(conceptEntry.getDescription());
            conceptCache.setPos(conceptEntry.getPos());

            String typeId = "";
            if (conceptEntry.getType() != null) {
                if (conceptEntry.getType().getTypeUri() != null) {
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

            if (conceptEntry.getEqualTo() != null && !conceptEntry.getEqualTo().trim().equals("")) {
                conceptCache.setEqualTo(Arrays.asList(conceptEntry.getEqualTo().split(",")));
            } else {
                conceptCache.setEqualTo(new ArrayList<>());
            }

            conceptCache.setAlternativeUris(
                    conceptEntry.getAlternativeIds().stream().map(alternativeId -> alternativeId.getConceptUri())
                            .filter(alternativeUri -> alternativeUri != null && !alternativeUri.equals(""))
                            .collect(Collectors.toList()));

            if (conceptEntry.getType() != null) {
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
