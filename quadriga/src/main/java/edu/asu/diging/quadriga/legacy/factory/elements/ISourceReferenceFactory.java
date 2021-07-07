package edu.asu.diging.quadriga.legacy.factory.elements;

import edu.asu.diging.quadriga.core.model.elements.SourceReference;

/**
 * This is the interface class for SourceReferenceFactory class which has the
 * following methods: createSourceReference()
 * 
 * @author Veena Borannagowda
 *
 */
@Deprecated
public interface ISourceReferenceFactory {

    SourceReference createSourceReference();

    SourceReference createSourceReference(String sourceUri);

}