//package edu.asu.diging.quadriga.core.service;
//
//import edu.asu.diging.quadriga.core.exceptions.CollectionNotFoundException;
//import edu.asu.diging.quadriga.core.exceptions.InvalidObjectIdException;
//import edu.asu.diging.quadriga.core.exceptions.MappedCollectionNotFoundException;
//import edu.asu.diging.quadriga.core.model.MappedCollection;
//
///**
// * 
// * This is a service that uses the MappedCollectionRepository to manage a
// * MappedCollection in the database
// * 
// * @author poojakulkarni
// *
// */
//public interface MappedCollectionService {
//
//    public MappedCollection addMappedCollection(String collectionId)
//            throws InvalidObjectIdException, CollectionNotFoundException;
//
//    public MappedCollection findMappedCollectionByCollectionId(String collectionId)
//            throws InvalidObjectIdException, CollectionNotFoundException;
//
//    public MappedCollection findMappedCollectionById(String mappedCollectionId) throws InvalidObjectIdException;
//
//    public MappedCollection updateMappedCollectionNameById(String mappedCollectionId, String name)
//            throws InvalidObjectIdException, MappedCollectionNotFoundException;
//
//    public MappedCollection findOrAddMappedCollectionByCollectionId(String collectionId)
//            throws InvalidObjectIdException, CollectionNotFoundException;
//
//}
