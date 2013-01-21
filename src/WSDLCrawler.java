import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Vector;

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
	
	private Vector<String> wsdlUrls;		// Urls of wsdl documents
	//private String[] webServiceProviders;	// Web service providers or repositories to get wsdl documents
	private String repository = "http://www.webservicex.net/WS/wscatlist.aspx";
	private String repositoryPrefix = "http://www.webservicex.net/WS/";
	private String wsdlUrlsFile = "webservices.dat";	// File to write and read online wsdl documents
	
	public static String localRepository = "wsdl_repository";	// Directory to store wsdl documents
	
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
	 * @return the wsdlUrls
	 */
	public Vector<String> getWsdlUrls()
	{
		return wsdlUrls;
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
		Vector<String> categories;	// web services categories
		Vector<String> webServicePages;	// pages contain each web service info
		Document htmlDoc;			// html document
		Elements links;				// links in document
		String wsdl;				// Online wsdl document
		int numLinks;				// number of links in documenet
		int numCategories;			// number of categories
		int numWSDLPages;			// Number of web service description pages
		int i=0,j=0;
		URL url;					// Wsdl url
		
		try
		{
			FileWriter fstream = new FileWriter(wsdlUrlsFile);
			BufferedWriter out = new BufferedWriter(fstream);

			
			// get document
			htmlDoc = Jsoup.connect(this.repository).data("query","Java").userAgent("Mozilla").timeout(3000).post();
			links = htmlDoc.select("a[href]");
			numLinks = links.size();
			
			// categories
			categories=new Vector<String>(numLinks);
			for(i=0;i<numLinks;i++)
				if(links.get(i).toString().contains("CATs.aspx"))
					categories.add(repositoryPrefix+links.get(i).attr("href"));
			
			// iterate through categories to get web services pages
			numCategories = categories.size();
			webServicePages = new Vector<String>(numCategories*5);
			for(i=0;i<numCategories;i++)
			{
				htmlDoc = Jsoup.connect(categories.get(i).replace(" ", "%20")).data("query","Java").userAgent("Mozilla").timeout(3000).post();
				links = htmlDoc.select("a[href]");
				numLinks = links.size();
				
				for(j=0;j<numLinks;j++)
					if(links.get(j).toString().contains("WSDetails.aspx"))
						webServicePages.add(repositoryPrefix+links.get(j).attr("href"));
			}
			
			// get online wsdl document from each web service description page
			numWSDLPages = webServicePages.size();
			this.wsdlUrls = new Vector<String>(numWSDLPages);
			for(i=0;i<numWSDLPages;i++)
			{
				htmlDoc = Jsoup.connect(webServicePages.get(i)).data("query","Java").userAgent("Mozilla").timeout(3000).post();
				links = htmlDoc.select("iframe[src]");
				wsdl = links.get(0).attr("src");
				if(i>0)	// ignore first document because it is disabled
				if(!this.wsdlUrls.contains(wsdl))
				{
					url = new URL(wsdl.replace("\r\n", ""));
					FileUtils.copyURLToFile(url,new File("./"+localRepository+"/"+url.getFile().substring(1,url.getFile().length()-5)), 3000, 3000);
					out.write(wsdl);
					out.newLine();
				}

			}
			
			out.close();
			
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return this.wsdlUrls;
	}
	
	/**
	 * Read online wsdl documents from file
	 */
	public Vector<String> readFromFile()
	{
		String wsdl;				// Online wsdl document
		
		try
		{
			FileInputStream fstream = new FileInputStream(wsdlUrlsFile);

			// Get the object of DataInputStream
		   DataInputStream in = new DataInputStream(fstream);
		   BufferedReader br = new BufferedReader(new InputStreamReader(in));
		   
		   this.wsdlUrls = new Vector<String>();
		   
		   //Read File Line By Line
		   while ((wsdl = br.readLine()) != null)   
		   {
			   if(!this.wsdlUrls.contains(wsdl))
				 this.wsdlUrls.add(wsdl);
		   }
		   
		   //Close the input stream
		   in.close();
		}
		catch (FileNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return this.wsdlUrls;
	}
}
