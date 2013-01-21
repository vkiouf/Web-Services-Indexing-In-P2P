import java.util.Vector;

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
		System.out.println("Get documenents from "+WSDLCrawler.localRepository+"...");
		// Read all wsdl files from local repository
		WebServicesCollection wsdlDocs = new WebServicesCollection(WSDLCrawler.localRepository);
		Vector<String> wsdlDocuments = wsdlDocs.getDocuments();
		
		// Cluster collection of wsdl documents
		WebServicesClusterer clusterer = new WebServicesClusterer(wsdlDocuments,0.7);
		clusterer.cluster();
		
		System.out.print("Clustering ended");
	}

}
