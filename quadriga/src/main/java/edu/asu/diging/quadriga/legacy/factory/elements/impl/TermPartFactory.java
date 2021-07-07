package edu.asu.diging.quadriga.legacy.factory.elements.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.model.elements.TermPart;
import edu.asu.diging.quadriga.legacy.factory.elements.ITermPartFactory;

/**
 * This is the factory class for TermPart element. This is used to instantiate
 * TermPart class.
 * 
 * @author Veena Borannagowda
 *
 */
@Deprecated
@Service
public class TermPartFactory implements ITermPartFactory {

    @Override
    public TermPart createTermPart() {
        return new TermPart();
    }
}