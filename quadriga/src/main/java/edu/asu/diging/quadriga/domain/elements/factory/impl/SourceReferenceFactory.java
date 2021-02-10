package edu.asu.diging.quadriga.domain.elements.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.domain.elements.SourceReference;
import edu.asu.diging.quadriga.domain.elements.factory.ISourceReferenceFactory;

/**
 * This is the factory class for SourceReference element. This is used to
 * instantiate SourceReference class.
 * 
 * @author Veena Borannagowda
 *
 */
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