package hibernate.Util;


        import org.hibernate.SessionFactory;
        import org.hibernate.boot.MetadataSources;
        import org.hibernate.boot.registry.StandardServiceRegistry;
        import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
        import org.hibernate.cfg.Configuration;
        import org.hibernate.service.ServiceRegistry;

public class HibernateUtil {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            // loads configuration and mappings
            Configuration configuration = new Configuration().addFile("C:\\Users\\rask\\IdeaProjects\\Catalog\\src\\main\\resources\\hibernate.cfg.xml")
                    .addFile("C:\\Users\\rask\\IdeaProjects\\Catalog\\src\\main\\resources\\models\\FilesEntity.hbm.xml")
                    .addFile("C:\\Users\\rask\\IdeaProjects\\Catalog\\src\\main\\resources\\models\\UsersEntity.hbm.xml").configure();
            ServiceRegistry serviceRegistry
                    = new StandardServiceRegistryBuilder()
                    .applySettings(configuration.getProperties()).build();

            // builds a session factory from the service registry
            sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        }

        return sessionFactory;
    }
}

