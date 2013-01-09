import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class WebSearch
{
	SearchEngine searchEngine ;
	
	public WebSearch(SearchEngine searchEngine)
	{
		this.searchEngine = searchEngine;
	}
	
	/**
     * The main entry point of the program.
     *
     * @param args
     *            The command-line arguments. These arguments are encoded as a
     *            Google search query.
     */
    public long search(final String[] args) 
    {
        // Check for usage errors.
        if (args.length == 0) {
            System.out.println("usage: GoogleSearch query ...");
            return -1;
        }
        
        String resultStart;
        String resultEnd;
        
        if(searchEngine == SearchEngine.GOOGLE)
        {
        	resultStart = "About";
        	resultEnd = "results";
        }
        else
    	{
    		 resultStart = "<span class=\"sb_count\" id=\"count\">";
             resultEnd = "results";
    	}
        
        int startIndx;
        int endIndx;
        String numHitsS;
        long numHits =0;

        // Catch IO errors that may occur while encoding the query, downloading
        // the results, or parsing the downloaded content.
        try {
            // Encode the command-line arguments as a Google search query.
            final URL url = encodeGoogleQuery(args);

            // Download the content from Google.
            final String html = downloadString(url);

            startIndx = html.indexOf(resultStart,0);
            endIndx=html.indexOf(resultEnd,startIndx);
            numHitsS=html.substring(startIndx+resultStart.length(), endIndx).replace(",","").trim();
            numHits = Long.parseLong(numHitsS);
            
            return numHits;

        } catch (final IOException e) {
            // Display an error if anything fails.
            System.err.println(e.getMessage());
            System.exit(1);
            return 0;
        }
        catch(final NumberFormatException nfe)
        {
        	return 0;
        }
 
    }
   
    /**
     * Reads all contents from an input stream and returns a string from the
     * data.
     *
     * @param stream
     *            The input stream to read.
     *
     * @return A string built from the contents of the input stream.
     *
     * @throws IOException
     *             Thrown if there is an error reading the stream.
     */
    private static String downloadString(final InputStream stream)
            throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        int ch;
        while (-1 != (ch = stream.read()))
        {
            out.write(ch);
            out.flush();
        }
        return out.toString();
    }

    /**
     * Downloads the contents of a URL as a String. This method alters the
     * User-Agent of the HTTP request header so that Google does not return
     * Error 403 Forbidden.
     *
     * @param url
     *            The URL to download.
     *
     * @return The content downloaded from the URL as a string.
     *
     * @throws IOException
     *             Thrown if there is an error downloading the content.
     */
    private static String downloadString(final URL url) throws IOException {
        final String agent = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:17.0) Gecko/20100101 Firefox/17.0";
        final URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", agent);
       // connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        //connection.setRequestProperty("Accept-Language", "el-gr,el;q=0.8,en-us;q=0.5,en;q=0.3");
        final InputStream stream = connection.getInputStream();
        return downloadString(stream);
    }

    /**
     * Encodes a string of arguments as a URL for a Google search query.
     *
     * @param args
     *            The array of arguments to pass to Google's search engine.
     *
     * @return A URL for a Google search query based on the arguments.
     */
    private  URL encodeGoogleQuery(final String[] args) {
        try {
            final StringBuilder localAddress = new StringBuilder();
            localAddress.append("/search?hl=en&q=");

            for (int i = 0; i < args.length; i++) {
                final String encoding = URLEncoder.encode(args[i], "UTF-8");
                localAddress.append(encoding);
                if (i + 1 < args.length)
                    localAddress.append("+");
            }
    
            if(searchEngine == SearchEngine.GOOGLE)
            	return new URL("http", "www.google.com", localAddress.toString());
            else
            	return new URL("http", "www.bing.com", localAddress.toString());

        } catch (final IOException e) {
            // Errors should not occur under normal circumstances.
            throw new RuntimeException(
                    "An error occurred while encoding the query arguments.");
        }
    }
   
}