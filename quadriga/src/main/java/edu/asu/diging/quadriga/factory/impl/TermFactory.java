package edu.asu.diging.quadriga.factory.impl;

import org.springframework.stereotype.Service;

import edu.asu.diging.quadriga.elements.factory.ITermFactory;
import edu.asu.diging.quadriga.model.elements.Term;

/**
 * This is the factory class for Term element. This is used to instantiate Term
 * class.
 * 
 * @author Veena Borannagowda
 *
 */
@Service
public class TermFactory implements ITermFactory {

    @Override
    public Term createTerm() {
        return new Term();
    }
}