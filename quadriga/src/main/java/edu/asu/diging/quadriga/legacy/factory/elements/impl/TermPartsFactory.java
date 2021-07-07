package edu.asu.diging.quadriga.legacy.factory.elements.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.model.elements.TermParts;
import edu.asu.diging.quadriga.legacy.factory.elements.ITermPartsFactory;

/**
 * This is the factory class for TermParts element. This is used to instantiate
 * TermParts class.
 * 
 * @author Veena Borannagowda
 *
 */
@Deprecated
@Service
public class TermPartsFactory implements ITermPartsFactory {

    @Override
    public TermParts createTermParts() {
        return new TermParts();
    }
}