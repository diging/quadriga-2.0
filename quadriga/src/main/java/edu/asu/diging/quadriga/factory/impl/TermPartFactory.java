package edu.asu.diging.quadriga.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.elements.factory.ITermPartFactory;
import edu.asu.diging.quadriga.model.elements.TermPart;

/**
 * This is the factory class for TermPart element. This is used to instantiate
 * TermPart class.
 * 
 * @author Veena Borannagowda
 *
 */
@Service
public class TermPartFactory implements ITermPartFactory {

    @Override
    public TermPart createTermPart() {
        return new TermPart();
    }
}