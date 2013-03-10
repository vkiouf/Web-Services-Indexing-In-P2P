import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;


/**
 * Fetches online wsdl documents from a web service providers and stores them locally
 * (For now, wsdl documents extracted from http://www.webservicex.net)
 */
public class WSDLCrawler
{
	/*=========================================================================
	 *					Fields
	 *=========================================================================*/
	
	public static String localRepository = "wsdl_repository";	// Local directory contains wsdl documents for testing
	
	// web service x
	public static String webServiceXName = "webservicex";
	private String webServiceXRepository = "http://www.webservicex.net/WS/wscatlist.aspx";
	private String webServiceXRepositoryPrefix = "http://www.webservicex.net/WS/";
	private String webServiceXUrlsFile = "wsdl_repositories/webservicex.dat";	// File to write and read online wsdl documents
	public static String localWebServiceXRepository = "wsdl_repositories/webservicex";	// Directory to store wsdl documents
	
	// x methods
	public static  String xMethodsName = "xmethods";
	private String xmethodsRepository = "http://www.xmethods.net/ve2/Directory.po";
	private String xmethodsPrefix = "http://www.xmethods.net";
	private String xmethodsUrlsFile = "wsdl_repositories/xmethods.dat";	// File to write and read online wsdl documents
	public static String localxMethodsRepository = "wsdl_repositories/xmethods";	// Directory to store wsdl documents
	
	// soa trader
	public static  String soaTraderName = "soatrader";
	private String soaTraderRepository = "http://www.soatrader.com/web-services/";
	private String soaTraderPrefix = "http://www.soatrader.com/web-services";
	private String soaTraderUrlsFile = "wsdl_repositories/soatrader.dat";	// File to write and read online wsdl documents
	public static String localSoaTraderRepository = "wsdl_repositories/soatrader";	// Directory to store wsdl documents
	
	private Hashtable<String,Vector<String>> categories;	// categories and included documents
	Vector<String> tempdocs = new Vector<String>();		// Hold a list of temporary documents
	
	/*=========================================================================
	 *					Constructors
	 *=========================================================================*/

	public WSDLCrawler()
	{

	}
	
	/*=========================================================================
	 *					Getters
	 *=========================================================================*/
	
	/**
	 * @return the categories
	 */
	public Hashtable<String, Vector<String>> getCategories()
	{
		return categories;
	}
	
	
	/*=========================================================================
	 *					Methods
	 *=========================================================================*/
	
	/**
	 * Fetches wsdl documents from http://www.webservicex.net repository
	 * and stores them in directory of wsdl documents
	 */
	public Vector<String> fetchFromWebServiceX()
	{
		Vector<String> wsdlUrls=new Vector<String>();		// Urls of wsdl documents
		HashMap<String,String> categories;		// Web services categories ( Key:name, Value:link)
		Vector<String> wsdlFiles;		// Online url files
		Document htmlDoc;				// html document
		Elements links;					// links in html document
		int numLinks;					// number of links in html documenet
		int i=0,j=0;
		
		this.categories.clear();	// clear categories before adding new documents
		
		try
		{
			//FileWriter fstream = new FileWriter(webServiceXUrlsFile);
			//BufferedWriter out = new BufferedWriter(fstream);

			// get document
			htmlDoc = Jsoup.connect(this.webServiceXRepository).data("query","Java").userAgent("Mozilla").timeout(3000).post();
			links = htmlDoc.select("a[href]");
			numLinks = links.size();
			
			// categories
			this.categories = new  Hashtable<String,Vector<String>>(numLinks);
			categories=new HashMap<String,String>(numLinks);
			for(i=0;i<numLinks;i++)
				if(links.get(i).toString().contains("CATs.aspx"))
					categories.put(links.get(i).text(),webServiceXRepositoryPrefix+links.get(i).attr("href"));
			
			// iterate through categories to get web services pages
			for(Map.Entry<String, String> categoryEntry : categories.entrySet())
			{
				// get the links of web services information pages from each category page
				htmlDoc = Jsoup.connect(categoryEntry.getValue().replace(" ", "%20")).data("query","Java").userAgent("Mozilla").timeout(3000).post();
				links = htmlDoc.select("a[href]");
				numLinks = links.size();
				
				// get the online wsdl file from each web service info page
				wsdlFiles = new Vector<String>(numLinks);
				for(j=0;j<numLinks;j++)
					if(links.get(j).toString().contains("WSDetails.aspx"))
					{
						htmlDoc = Jsoup.connect(links.get(j).attr("abs:href")).data("query","Java").userAgent("Mozilla").timeout(3000).post();
						wsdlFiles.add(htmlDoc.select("iframe[src]").get(0).attr("src"));
					}
			
				// Save wsdl files in local repository and add them to category
				saveWSDLFiles(wsdlFiles,wsdlUrls,localWebServiceXRepository,categoryEntry.getKey());

				// get online wsdl document from each web service description page
				/*numWSDLPages = webServicePages.size();
				wsdlUrls = new Vector<String>(numWSDLPages);
				for(i=0;i<numWSDLPages;i++)
				{
					htmlDoc = Jsoup.connect(webServicePages.get(i)).data("query","Java").userAgent("Mozilla").timeout(3000).post();
					links = htmlDoc.select("iframe[src]");
					wsdl = links.get(0).attr("src");
					if(i>0)	// ignore first document because it is disabled
					if(!wsdlUrls.contains(wsdl))
					{
						url = new URL(wsdl.replace("\r\n", ""));
						FileUtils.copyURLToFile(url,new File("./"+localWebServiceXRepository+"/"+url.getFile().substring(1,url.getFile().length()-5)), 3000, 3000);
						//out.write(wsdl);
						//out.newLine();
					}
				}*/
			}
			
			saveCategoriesInDatabase(webServiceXName);
			
			//out.close();
			
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return wsdlUrls;
	}
	
	/**
	 * Fetches wsdl documents from http://www.soatrader.com repository
	 * and stores them in directory of wsdl documents
	 */
	public Vector<String> fetchFromSoaTrader()
	{
		Vector<String> wsdlUrls=new Vector<String>();		// Urls of wsdl documents
		Vector<String> pagesLinks = new Vector<String>();	// Links of pages
		Vector<String> categoriesLinks;						// Links of web services categories
		Vector<String> wsdlFiles;							// Url of online wsdl documents
		
		Document htmlDoc;									// Html document
		Elements pages;										// Pages links
		Elements links;										// Links in document
		Elements wsdlLinks;									// Links of wsdl urls
		
		int numLinks;										// Number of links in documenet
		int numCategories;									// Number of categories
		int numPages;										// Number of pages in category
	
		int i=0,j=0,k=0;

		this.categories.clear();	// clear categories before adding new documents
		try
		{
		//	FileWriter fstream = new FileWriter(soaTraderUrlsFile);	// write all wsdl documents urls to a file
			//BufferedWriter out = new BufferedWriter(fstream);

			// get document of soatrader index page
			htmlDoc = Jsoup.connect(this.soaTraderRepository).data("query","Java").userAgent("Mozilla").timeout(3000).post();
			links = htmlDoc.select("div.sub-categories a");
			numLinks = links.size();
			
			// Index page contains categories
			System.out.println("Get categories...");
			categoriesLinks=new Vector<String>(numLinks);
			
			// Add categories urls to categories list
			this.categories = new  Hashtable<String,Vector<String>>(categoriesLinks.size());
			for(i=0;i<numLinks;i++)
				categoriesLinks.add(links.get(i).attr("abs:href"));
			
			// Iterate through categories to get web services pages
			numCategories = categoriesLinks.size();
			for(i=0;i<numCategories;i++)
			{	
				// Get all pages links ( page 1, page 2...) from each category
				Thread.sleep(1000);
				wsdlFiles = new Vector<String>(50);
				htmlDoc = Jsoup.connect(categoriesLinks.get(i).replace(" ", "%20")).data("query","Java").userAgent("Mozilla").timeout(3000).post();
				pages = htmlDoc.select("ul.pagination a[href]");
				numPages = pages.size();
				pagesLinks.clear();
				
				for(j=0;j<numPages;j++)
					if(!pagesLinks.contains(pages.get(j).attr("abs:href")))
						pagesLinks.add(pages.get(j).attr("abs:href"));

				if(pages.size()==0)	// Category contains only single page
					numPages = 1;
				else				
					numPages= pagesLinks.size();
				
				// From each page, collect wsdl documents urls
				for(j=0;j<numPages;j++)
				{
					try
					{
						Thread.sleep(500);
						
						if(pages.size()>0)
						{
							System.out.println("Parse web services from "+pagesLinks.get(j));
							htmlDoc = Jsoup.connect(pagesLinks.get(j).replace(" ", "%20")).data("query","Java").userAgent("Mozilla").timeout(3000).post();
						}
						else
							System.out.println("Parse web services from "+categoriesLinks.get(i));
						
						// Get all links from each page
						wsdlLinks = htmlDoc.select("a[href]");
						numLinks = wsdlLinks.size();
						
						// Add to wsdl files list the wsdl document link. 
						// Each wsdl document link ends with "wsdl"
						for(k=0;k<numLinks;k++)
							if(wsdlLinks.get(k).attr("href").toLowerCase().endsWith("wsdl"))
								wsdlFiles.add(wsdlLinks.get(k).attr("abs:href"));		
					}
					catch(  IOException   ex)
					{
						System.out.println("Problem");
						continue;
					}
				}
				
				// Save wsdl files in local repository and add them to category
				saveWSDLFiles(wsdlFiles,wsdlUrls,localSoaTraderRepository,links.get(i).text());
			}
			
			saveCategoriesInDatabase(soaTraderName);
			
			//out.close();
		}
		catch(  IOException | InterruptedException  ex)
		{
			System.out.println("Problem");
			ex.printStackTrace();
		}

		
		return wsdlUrls;
	}

	
	/**
	 * Fetches wsdl documents from http://www.xmethods.net repository
	 * and stores them in directory of wsdl documents
	 */
	public Vector<String> fetchFromXMethods()
	{
		Vector<String> wsdlUrls=new Vector<String>();		// Urls of wsdl documents
		Vector<String> webServicePages;	// pages contain each web service info
		Document htmlDoc;			// html document
		Elements links;				// links in document
		String wsdl;				// Online wsdl document
		int numLinks;				// number of links in documenet
		int numWSDLPages;			// Number of web service description pages
		int i=0;
		URL url;					// Wsdl url
		 String urlFile;
		File wsdlFile;
		
		try
		{
			FileWriter fstream = new FileWriter(xmethodsUrlsFile);
			BufferedWriter out = new BufferedWriter(fstream);

			// get document
			htmlDoc = Jsoup.connect(this.xmethodsRepository).data("query","Java").userAgent("Mozilla").timeout(3000).post();
			links = htmlDoc.select("a[href]");
			numLinks = links.size();
			
			webServicePages = new Vector<String>(numLinks);
			for(i=0;i<numLinks;i++)
				if(links.get(i).toString().contains("/ve2/ViewListing.po"))	// page that contain wsdl file 
					webServicePages.add(xmethodsPrefix+"/ve2/ViewListing.po"+links.get(i).attr("abs:href").substring(links.get(i).attr("abs:href").indexOf("?key")));
			
			// get online wsdl document from each web service description page
			numWSDLPages = webServicePages.size();
			wsdlUrls = new Vector<String>(numWSDLPages);
			for(i=0;i<numWSDLPages;i++)
			{
				System.out.print(i+1+" "+webServicePages.get(i)+" ... ");
				
				try
				{
					Thread.sleep(500);
					htmlDoc = Jsoup.connect(webServicePages.get(i)).data("query","Java").userAgent("Mozilla").timeout(3000).post();
					links = htmlDoc.select("a#WSDLURL");
					wsdl = links.get(0).attr("href");
					if(!wsdlUrls.contains(wsdl))
					{
						url = new URL(wsdl.replace("\r\n", ""));
						
						urlFile = url.getFile();
						System.out.print("URLFile: "+urlFile+" ... ");
						if(urlFile.contains(".") && urlFile.lastIndexOf('.')>urlFile.lastIndexOf('/'))
							urlFile = urlFile.substring(urlFile.lastIndexOf('/')+1,urlFile.lastIndexOf('.'));
						else if(urlFile.contains("?") && urlFile.lastIndexOf('?')>urlFile.lastIndexOf('/'))
							urlFile = urlFile.substring(urlFile.lastIndexOf('/')+1,urlFile.lastIndexOf('?'));
						else
							urlFile = urlFile.substring(urlFile.lastIndexOf('/'));
						urlFile = urlFile+".wsdl";
						System.out.print("Save File: "+urlFile+" ... ");
						
						wsdlFile = new File("./"+localxMethodsRepository+"/"+urlFile);
						if(wsdlFile.exists())
						{
							System.out.println("Exists");
							continue;
						}
						FileUtils.copyURLToFile(url,wsdlFile, 3000, 3000);
						System.out.println("OK");
						out.write(wsdl);
						out.newLine();
					}
				}
				catch(  IOException  ex)
				{
					System.out.println("Problem");
					continue;
				}
				catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
			
			out.close();
			
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return wsdlUrls;
	}
	

	/**
	 * Save categories with included document names in object db database
	 * @param repository Repository name of documents source
	 */
	public void saveCategoriesInDatabase(String repository)
	{
		WSDLCategory.create(repository,categories);
	}
	
	/**
	 * Save a list of online wsdl documents in a local repository and put them on category
	 * @param wsdlFiles	 List of online wsdl documents
	 * @param wsdlUrls List holding the urls of stored files
	 * @param localRepository  Local repository folder to store files
	 * @param category	Category where document belongs to
	 */
	private void saveWSDLFiles(Vector<String> wsdlFiles,Vector<String> wsdlUrls,String localRepository,String category)
	{
		Vector<String> categoryDocuments;		// Documents belong to a category
		int numWSDLs;							// Number of wsdl documents
		URL wsdlUrl;							// Url of wsdl document											
		String wsdlUrlFile;						// WSDl filename of url extracted from url
		File wsdlFile;							// Local file									
		String wsdl;							// Online wsdl document
		int j=0;
		
		// Download content from each wsdl document to a local file
		numWSDLs = wsdlFiles.size();
		categoryDocuments = new Vector<String>(numWSDLs);
		for(j=0;j<numWSDLs;j++)
		{
			wsdl = wsdlFiles.get(j);
			System.out.print(j+1+" "+wsdl+" ... ");

			try
			{
				if(!wsdlUrls.contains(wsdl))
				{
					wsdlUrl = new URL(wsdl.replace("\r\n", ""));	// Create a url object with wsdl document link as parameter
																// If url contains return characters, replace them with empty space
					
					// Get only the file name from url to save the wsdl document locally with same name and .wsdl extensio
					// If url filename contains ends with ?WSDL remove ?WSDL part
					// Also, if filename has different extension than .wsdl remove it
					wsdlUrlFile = wsdlUrl.getFile();
					System.out.print("URLFile: "+wsdlUrlFile+" ... ");
					if(wsdlUrlFile.contains(".") && wsdlUrlFile.lastIndexOf('.')>wsdlUrlFile.lastIndexOf('/'))
						wsdlUrlFile = wsdlUrlFile.substring(wsdlUrlFile.lastIndexOf('/')+1,wsdlUrlFile.lastIndexOf('.'));
					else if(wsdlUrlFile.contains("?") && wsdlUrlFile.lastIndexOf('?')>wsdlUrlFile.lastIndexOf('/'))
						wsdlUrlFile = wsdlUrlFile.substring(wsdlUrlFile.lastIndexOf('/')+1,wsdlUrlFile.lastIndexOf('?'));
					else
						wsdlUrlFile = wsdlUrlFile.substring(wsdlUrlFile.lastIndexOf('/'));
					wsdlUrlFile = wsdlUrlFile+".wsdl";	// Add .wsdl extension in end of each file
					System.out.print("Save File: "+wsdlUrlFile+" ... ");
					
					wsdlFile = new File("./"+localRepository+"/"+wsdlUrlFile);
					if(wsdlFile.exists())
					{
						/*if(!tempdocs.contains(wsdlUrlFile.toLowerCase()))
						{
							tempdocs.add(wsdlUrlFile.toLowerCase());
							// in category documents add filename without extension
							categoryDocuments.add(wsdlUrlFile.substring(0,wsdlUrlFile.indexOf('.')));
						}*/
						
						System.out.println("Exists");
						continue;
					}
					
					//System.out.println(wsdlFile);
					categoryDocuments.add(wsdlUrlFile.substring(0,wsdlUrlFile.indexOf('.')));
					FileUtils.copyURLToFile(wsdlUrl,wsdlFile, 3000, 3000);
					System.out.println("OK");
				//	out.write(wsdl);
				//	out.newLine();
				}
			}
			catch(  IOException   ex)
			{
				System.out.println("Error");
				continue;
			}
		}
		
		// Add in categories the name of category contained in links[i].text of index page and the list 
		// of document contained to this
		this.categories.put(category,categoryDocuments);			
	}
}
