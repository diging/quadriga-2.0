package edu.asu.diging.quadriga.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.elements.factory.ITermPartsFactory;
import edu.asu.diging.quadriga.model.elements.TermParts;

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