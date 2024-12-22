package util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaUtil {
	private static EntityManagerFactory factory;
	
	static {
		factory = Persistence.createEntityManagerFactory("dbjpa");
	}
	
	public static EntityManager getEntityManager() {
		return factory.createEntityManager();
	}
	
	public static void close() {
		factory.close();
	}
}
