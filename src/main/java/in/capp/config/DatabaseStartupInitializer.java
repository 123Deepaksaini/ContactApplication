package in.capp.config;

import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;
import in.capp.dao.Db;

@Component
public class DatabaseStartupInitializer {

    @PostConstruct
    public void init() {
        Db.initializeSchemaIfRequired();
    }
}
