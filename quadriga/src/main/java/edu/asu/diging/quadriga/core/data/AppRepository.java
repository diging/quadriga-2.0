package edu.asu.diging.quadriga.core.data;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import edu.asu.diging.quadriga.core.model.impl.App;
import edu.asu.diging.quadriga.web.forms.AppForm;

@Repository
public interface AppRepository extends PagingAndSortingRepository<App, String> {

    default App saveApp(AppForm appForm) {
        App app = new App();
        app.setName(appForm.getName());
        app.setDescription(appForm.getDescription());
        this.save(app);
        return app;

    }

}
