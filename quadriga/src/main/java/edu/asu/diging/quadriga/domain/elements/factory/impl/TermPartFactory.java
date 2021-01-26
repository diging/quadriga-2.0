package edu.asu.diging.quadriga.domain.elements.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.domain.elements.TermPart;
import edu.asu.diging.quadriga.domain.elements.factory.ITermPartFactory;

/**
 * This is the factory class for TermPart element. 
 * This is used to instantiate TermPart class.
 * @author Veena Borannagowda
 *
 */
@Service
public class TermPartFactory implements ITermPartFactory {
    @Override
    public TermPart createTermPart()
    {
        return new TermPart();
    }
}