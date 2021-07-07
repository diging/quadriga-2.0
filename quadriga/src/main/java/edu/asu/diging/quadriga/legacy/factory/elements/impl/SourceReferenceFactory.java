package edu.asu.diging.quadriga.legacy.factory.elements.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.model.elements.SourceReference;
import edu.asu.diging.quadriga.legacy.factory.elements.ISourceReferenceFactory;

/**
 * This is the factory class for SourceReference element. This is used to
 * instantiate SourceReference class.
 * 
 * @author Veena Borannagowda
 *
 */
@Deprecated
@Service
public class SourceReferenceFactory implements ISourceReferenceFactory {
    @Override
    public SourceReference createSourceReference() {
        return new SourceReference();
    }

    @Override
    public SourceReference createSourceReference(String sourceUri) {
        SourceReference sourceReferenceObject = new SourceReference();
        if (sourceUri == null) {
            sourceReferenceObject.setSourceURI("");
        } else {
            sourceReferenceObject.setSourceURI(sourceUri);
        }
        return sourceReferenceObject;
    }
}