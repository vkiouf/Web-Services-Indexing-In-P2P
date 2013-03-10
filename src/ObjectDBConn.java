import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * Provides connection and handling of objectdb database
 *
 */
public class ObjectDBConn
{
	/*=========================================================================
	 *					Fields
	 *=========================================================================*/
	
	// Object database
	public static String databasePath = "database/objectdb/web_services_clusterer.odb";
	public static EntityManagerFactory emf = Persistence.createEntityManagerFactory(databasePath);
	public static EntityManager em =emf.createEntityManager();
}
