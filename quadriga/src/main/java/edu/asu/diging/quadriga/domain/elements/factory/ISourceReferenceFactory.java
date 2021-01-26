package edu.asu.diging.quadriga.domain.elements.factory;

import edu.asu.diging.quadriga.domain.elements.SourceReference;

/**
 * This is the interface class for SourceReferenceFactory class
 * which has the following methods:
 * createSourceReference()
 * @author Veena Borannagowda
 *
 */
public interface ISourceReferenceFactory {

    SourceReference createSourceReference();

    SourceReference createSourceReference(String sourceUri);

}