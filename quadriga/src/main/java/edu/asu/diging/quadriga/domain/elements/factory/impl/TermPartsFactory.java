package edu.asu.diging.quadriga.domain.elements.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.domain.elements.TermParts;
import edu.asu.diging.quadriga.domain.elements.factory.ITermPartsFactory;

/**
 * This is the factory class for TermParts element. This is used to instantiate
 * TermParts class.
 * 
 * @author Veena Borannagowda
 *
 */
@Service
public class TermPartsFactory implements ITermPartsFactory {
    @Override
    public TermParts createTermParts() {
        return new TermParts();
    }
}