package edu.asu.diging.quadriga.legacy.factory.elements.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.core.model.elements.Term;
import edu.asu.diging.quadriga.legacy.factory.elements.ITermFactory;

/**
 * This is the factory class for Term element. This is used to instantiate Term
 * class.
 * 
 * @author Veena Borannagowda
 *
 */
@Deprecated
@Service
public class TermFactory implements ITermFactory {

    @Override
    public Term createTerm() {
        return new Term();
    }
}