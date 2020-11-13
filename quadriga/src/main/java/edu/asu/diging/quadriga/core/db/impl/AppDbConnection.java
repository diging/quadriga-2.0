package edu.asu.diging.quadriga.core.db.impl;

import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.stereotype.Component;

import edu.asu.diging.quadriga.core.db.IAppDbConnection;
import edu.asu.diging.quadriga.core.exceptions.UnstorableObjectException;
import edu.asu.diging.quadriga.core.model.IApp;
import edu.asu.diging.quadriga.core.model.impl.App;

@Component
@Transactional
public class AppDbConnection implements IAppDbConnection {

    @PersistenceContext
    private EntityManager em;

    @Override
    public IApp getById(String id) {
        return em.find(App.class, id);
    }

    @Override
    public IApp store(IApp app) throws UnstorableObjectException {
        if (app.getId() == null) {
            throw new UnstorableObjectException("App does not have an id.");
        } else {
            em.persist(app);
        }
        em.flush();
        return app;
    }

    @Override
    public String generateId() {
        String id = null;
        while (true) {
            id = "APP" + generateUniqueId();
            Object existingFile = getById(id);
            if (existingFile == null) {
                break;
            }
        }
        return id;
    }

    /**
     * This methods generates a new 6 character long id. Note that this method does
     * not assure that the id isn't in use yet.
     * 
     * Adapted from
     * http://stackoverflow.com/questions/9543715/generating-human-readable
     * -usable-short-but-unique-ids
     * 
     * @return 12 character id
     */
    protected String generateUniqueId() {
        char[] chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();

        Random random = new Random();
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 12; i++) {
            builder.append(chars[random.nextInt(62)]);
        }

        return builder.toString();
    }

}
