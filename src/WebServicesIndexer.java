import java.io.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

import javax.persistence.TypedQuery;

import org.apache.commons.collections.*;

import com.google.common.collect.ArrayTable;
import com.google.common.collect.HashBasedTable;

import com.aliasi.cluster.*;

/**
 *	Main class to cluster wsdl documents
 */
public class WebServicesIndexer
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.out.println("Init...");
		
		WSDLDocumentCollection wsdlDocs = new WSDLDocumentCollection(WSDLCrawler.webServiceXName,WSDLCrawler.localRepository);
		wsdlDocs.extractContent(false, true, false, false, false);
		wsdlDocs.createVectorSpaceModel();
		
		System.out.println("Running features clustering algorithm...\n");
		double[] thresholds =  {0.9,0.8,0.7,0.6,0.5,0.4,0.3,0.29,0.28,0.27,0.26,0.25,0.24,0.23,0.22,0.21,0.2,0.1,0.05,0.01};
		WebServicesFeaturesClusterer featuresClusterer = new WebServicesFeaturesClusterer(wsdlDocs,thresholds,false);
		featuresClusterer.cluster();
		System.out.println("Features Clustering ended");
		
		System.out.println("Running multidimensional angles clustering algorithm...\n");
		WebServicesMultidimensionalAnglesClusterer clusterer = new WebServicesMultidimensionalAnglesClusterer(wsdlDocs,0,5,true,false,false);
		clusterer.cluster();
		System.out.println("Multidimensional Angles Clustering ended");
		
		// Close connections in object db
		ObjectDBConn.em.close();
		ObjectDBConn.emf.close();
		
		//WSDLCrawler crawler = new WSDLCrawler();
		//crawler.fetchFromSoaTrader();
		//crawler.fetchFromWebServiceX();
		/*crawler.saveCategoriesDatabase();*/
		//System.out.println("Get documennts from "+WSDLCrawler.localRepository+"...");
		// Read all wsdl files from local repository
		//WebServicesCollection wsdlDocs = new WebServicesCollection(WSDLCrawler.localRepository);
		//Vector<String> wsdlDocuments = wsdlDocs.getDocuments();
		
		/*double[] thresholds =  {0.9,0.8,0.7,0,6,0,5,0.4,0.3,0.2,0.1,0.05,0.01};
		
		for(double threshold:thresholds)
		{
			WebServicesFeaturesClusterer featuresClusterers = new WebServicesFeaturesClusterer(wsdlDocuments,threshold);
			featuresClusterers.cluster();
		}*/
		
//		TypedQuery<WebServicesClusters> query =  ObjectDBConn.em.createQuery(
//			      "SELECT cat FROM WebServicesClusters cat ",WebServicesClusters.class);
//		
//		for(int i=0;i<query.getResultList().size();i++)
//		{
//			ObjectDBConn.em.getTransaction().begin();
//			query.getResultList().get(i).setRepository(WSDLCrawler.soaTraderName);
//			ObjectDBConn.em.getTransaction().commit();
//		}
		

	/*	long startTime = System.nanoTime();

		WebServicesMultidimensionalAnglesClusterer clusterer = new WebServicesMultidimensionalAnglesClusterer(wsdlDocuments,0,10);
		clusterer.cluster(false);
		
		long estimatedTime = System.nanoTime() - startTime;
		System.out.println(TimeUnit.NANOSECONDS.toSeconds(estimatedTime));*/
		
		
		/*TypedQuery<WebServicesClusters> query =  ObjectDBConn.em.createQuery(
			      "SELECT WebServ FROM WebServicesClusters As WebServ ",WebServicesClusters.class);
		
		FileWriter fw;
		try
		{
			fw = new FileWriter("output.txt");
			BufferedWriter bw = new BufferedWriter(fw);
			
			for(int i=0;i<query.getResultList().size();i++)
			{
				WebServicesClusters clusters = query.getResultList().get(i);
				bw.write(clusters.getNumber()+"\t"+clusters.getThreshold()+"\t"+clusters.getPrecision()+"\t"+clusters.getRecall()+"\t"+clusters.getfMeasure());
				bw.newLine();
			}
			
			bw.close();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/

		
		/*Vector<WSDLDocument> WSDLDocumentsC = new Vector<WSDLDocument>();
		for(String WSDLDocument:wsdlDocuments)
		{
			WSDLDocumentsC.add(new WSDLDocument(WSDLDocument));
		}*/

//		Set<Set<String>> ref = new HashSet<Set<String>>();
//		Set<String> set1 = new HashSet<String>( Arrays.asList(new String[]{"1","2","3","4","5"}));
//		Set<String> set2 = new HashSet<String>( Arrays.asList(new String[]{"6","7"}));
//		Set<String> set3 = new HashSet<String>(Arrays.asList(new String[]{"8","9","A","B","C"}));
//		ref.add(set1); 
//		ref.add(set2);
//		ref.add(set3);
//		
//		Set<Set<String>> resp = new HashSet<Set<String>>();
//		resp.add(new HashSet<String>( Arrays.asList(new String[]{"1","2","3","4","5","8","9","A","B","C"})));
//		//resp.add(new HashSet<WSDLDocument>( Arrays.asList(new WSDLDocument[]{WSDLDocumentsC.get(1)})));
//		//resp.add(new HashSet<WSDLDocument>( Arrays.asList(new WSDLDocument[]{WSDLDocumentsC.get(2)})));
//		resp.add(new HashSet<String>( Arrays.asList(new String[]{"6","7"})));
//		
//		ClusterScore<String> clScore = new ClusterScore<String>(ref,resp);
//		double prec = clScore.equivalenceEvaluation().precision();
//		double reca = clScore.equivalenceEvaluation().recall();
//		double fMeas = clScore.equivalenceEvaluation().fMeasure();
//		
//		int x=5;
//		
		/*HashMap<String,Vector<String>> categories = new HashMap<String,Vector<String>>();
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("database/objectdb/web_services_clusterer.odb");
		EntityManager em =emf.createEntityManager(); 
		  
		TypedQuery<WSDLCategory> query = em.createQuery(
			      "SELECT WSDLCat FROM WSDLCategory AS WSDLCat ", WSDLCategory.class);
		
		int num=0;
		for(int i=0;i<query.getResultList().size();i++)
		{
			categories.put(query.getResultList().get(i).getCategory(), query.getResultList().get(i).getDocuments());

			Vector<String> docs = new Vector<String>();
			for(int k=0;k<query.getResultList().get(i).getDocuments().size();k++)
				docs.add(new WSDLDocument(query.getResultList().get(i).getDocuments().get(k)).getNameWithoutExtension());
			
			categories.put(query.getResultList().get(i).getCategory(),docs);
				
		}
		
		TypedQuery<WebServicesCluster> clQuery = em.createQuery("SELECT WSDLCl FROM WebServicesCluster AS WSDLCl ", WebServicesCluster.class);
		
		WebServicesCluster cluster ;
		for(int i=0;i<clQuery.getResultList().size();i++)
		{
			cluster = clQuery.getResultList().get(i);
			
			em.getTransaction().begin();
			cluster.findCategory(categories);
			cluster.measurePrecision(categories);
			cluster.measureRecall(categories);
			em.getTransaction().commit();

		}
			
	  
	  em.close();
	  emf.close();*/
		

		
		/*Hashtable<String,Vector<String>> categories = crawler.getCategories();
		
		  EntityManagerFactory emf = Persistence.createEntityManagerFactory("database/objectdb/web_services_clusterer.odb");
		  EntityManager em =emf.createEntityManager(); 
		  
		  Set<String> catNames =  categories.keySet();
		  for(String name:catNames)
		  {
			  num+= categories.get(name).size();
			  em.getTransaction().begin();
			  em.persist(new WSDLCategory(name,categories.get(name)));
			  em.getTransaction().commit();
			  em.clear();
		  }
		  
		  em.close();
		  emf.close();
		  
		  System.out.println(num);*/
		
//		try
//		{
//			FileWriter writer = new FileWriter("wsdlfiles.dat");
//			BufferedWriter out = new BufferedWriter(writer);
//			
//			for(int i=0;i<wsdlDocuments.size();i++)
//			{
//				out.write(new WSDLDocument(wsdlDocuments.get(i)).getNameWithoutExtension());
//				out.newLine();
//			}
//			
//			out.close();
//			
//		}
//		catch(IOException ex)
//		{
//			ex.printStackTrace();
//		}
//		


	}
}
