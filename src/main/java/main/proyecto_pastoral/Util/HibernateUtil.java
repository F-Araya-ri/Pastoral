package main.proyecto_pastoral.Util;

import main.proyecto_pastoral.Model.*;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            sessionFactory = new Configuration()
                    .configure("hibernate.cfg.xml")
                    .addAnnotatedClass(Parroquia.class)
                    .addAnnotatedClass(Sector.class)
                    .addAnnotatedClass(Entrevistador.class)
                    .addAnnotatedClass(Registro.class)
                    .addAnnotatedClass(Vivienda.class)
                    .addAnnotatedClass(Persona.class)
                    .addAnnotatedClass(IngresoFamiliar.class)
                    .addAnnotatedClass(Asistencia.class)
                    .buildSessionFactory();
        }
        return sessionFactory;
    }

    public static void cerrar() {
        if (sessionFactory != null && !sessionFactory.isClosed()) {
            sessionFactory.close();
        }
    }
}
