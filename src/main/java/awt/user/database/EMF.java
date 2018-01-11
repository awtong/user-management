package awt.user.database;

import javax.persistence.*;
import javax.servlet.*;
import javax.servlet.annotation.WebListener;

@WebListener
public class EMF implements ServletContextListener {

    private static EntityManagerFactory FACTORY;

    @Override
    public void contextInitialized(final ServletContextEvent event) {
    }

    @Override
    public void contextDestroyed(final ServletContextEvent event) {
	FACTORY.close();
    }

    public static CloseableEntityManager createEntityManager() {
	if (FACTORY == null) {
	    FACTORY = Persistence.createEntityManagerFactory("users");
	}

	return new CloseableEntityManager(FACTORY.createEntityManager());
    }
}
