
/**
 *
 */
public class WebServicesIndexer
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub
		WebServicesCollection wsdlDocs = new WebServicesCollection("wsdl_repository");
		wsdlDocs.getDocuments();
		
		WebServicesClusterer clusterer = new WebServicesClusterer(wsdlDocs.getDocuments());
		clusterer.extractFeatures();
		//clusterer.cluster();
		
		/*GoogleSearch gSearch = new GoogleSearch();
		gSearch.search(new String[]{"horse"});
		gSearch.search(new String[]{"rider"});
		gSearch.search(new String[]{"horse","rider"});*/
		
//		
	//	String[] words = new String[]{"response","port","weather","us","place","string","schema","post","document",
	//			"zip","bound","data","bind","zipcode","forecast","sequence","target","oper","document","temperature"};
		//String[] words = new String[]{"horse","rider"};
//		/*WordDatabase wdb = new WordDatabase(SimilarityType.NGD);
//		double res = wdb.NormalizedGoogleDistance("horse", "rider");
//		System.out.println(res);*/
	//	WebServicesClusterer clusterer = new WebServicesClusterer(wsdlDocs.getDocuments());
		//clusterer.recogniseContentWords(words);
//		
//		AzureSearchCompositeQuery  aq=new AzureSearchCompositeQuery();
//        
//        aq.setSources(new AZURESEARCH_QUERYTYPE[] {
//        	     AZURESEARCH_QUERYTYPE.WEB
//        	});
//        
//       // aq.setBingApi(AZURESEARCH_API.BINGSEARCHWEBONLY);
//        aq.setMarket("el-GR");
//        aq.setAppid("rDmZjn0iKtQ6aJRfb38kKY2Al7CswpekLuCDzJ5xDvo=");
//        aq.setQuery("car");
//        aq.doQuery();
//        AzureSearchResultSet<AbstractAzureSearchResult> ars = aq.getQueryResult();
//       // System.out.println(doc);
//        long num = ars.getWebTotal();
//        aq.setQuery("horse");
//        aq.doQuery();
//        long num2 = aq.getQueryResult().getWebTotal();
//        System.out.println(num);
//        System.out.println(num2);
		//System.out.println(response.getWeb().getResults().size());

		/*for (WebResult result : response.getWeb().getResults()) {
		        System.out.println(result.getTitle());
		        System.out.println(result.getDescription());
		        System.out.println(result.getUrl());
		        System.out.println(result.getDateTime());
		}*/
		
		/*clusterer.extractFeatures();*/
		/*WSDLDocumentHandler wsdlDocHandler=  new WSDLDocumentHandler(wsdlDocs.getDocuments().get(0));
		wsdlDocHandler.getPortsStructures();*/
		/*WSDLDocument doc = new WSDLDocument(wsdlDocs.getDocuments().elementAt(0));
		String[] x =doc.getWebServiceNameWords();
		System.out.println(x);*/
	}

}
