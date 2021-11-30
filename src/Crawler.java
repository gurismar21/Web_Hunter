import java.io.*;

import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler {
	 
	private static final int Max_Depth = 3;							// maximum depth limit for crawler to search in webpages
	private static final int MaxPage = 100;							// maximum pages crawler is allowed to fetched
	private static HashSet<String> urls;	
	
	public Crawler()
	{
		urls = new HashSet<String>();
	}
	
	public static String HTMLtoText(String ConstUrl)
	{
		try {
			Document doc = Jsoup.connect(ConstUrl).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
		               .referrer("http://www.google.com").timeout(1000*8).ignoreHttpErrors(true).get();
			String text = doc.body().text();
			
			return text;
			
		}
		catch(Exception e)
		{
			return "Couldn't convert to Text";
			
		}
	}
	
	public Elements getUrlsFromPage(String url) throws IOException
	{
		Document doc = Jsoup.connect(url).userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64; rv:25.0) Gecko/20100101 Firefox/25.0")
	               .referrer("http://www.google.com").timeout(1000*8).ignoreHttpErrors(true).get();
		Elements urlsFromPage= doc.select("a[href]");
		
		crawlPages(url);
		
		
			return urlsFromPage;		
	}
	
	public static String savePages(String url) throws IOException
	{
		File f = new File("WebPages//"+System.nanoTime()+".txt");
		f.createNewFile();			
		PrintWriter pw = new PrintWriter(f);
		pw.println(HTMLtoText(url));
		pw.close();
		
		return HTMLtoText(url);	
	}
	


	public HashSet<String> getPageUrls(String url,int dep)
	{
		// to check if url is present and the depth is within limit(50)
		if((!urls.contains(url) && (dep <= Max_Depth)))
		{
			try
			{
				urls.add(url);
	
				if(urls.size() >= MaxPage)			// to check that the number of pages are within limit(100)
				return urls;
				
			Elements urlsFromPage = getUrlsFromPage(url);
			
			dep++;
			
			for(Element e: urlsFromPage)
			{
				getPageUrls(e.attr("abs:href"), dep);
			}			
		}
			catch(IOException IOex)
			{
			}		
		}
		return urls;
	}
	
	
	
public static String crawl(String url, int depth) {
		
		
		String html = urlToHTML(url);
		
		Document doc = Jsoup.parse(html);
		String text = doc.text();
		String title = doc.title();
		//System.out.print(text);
		createFile(title,text,url);
		
		Elements e = doc.select("a");
		String urls = "";
		
		for(Element e2 : e) {
			String href = e2.attr("abs:href");
			if(href.length()>3 && (depth <= Max_Depth))
			{
				urls = urls+"\n"+href;
				depth ++;
				
			}
		}
		return urls;
	}
	public static void createFile(String title,String text,String url) {
		try {
			String[] titlesplit = title.split("\\|");
			String newTitle = "";
			for(String s : titlesplit) {
				newTitle = newTitle+" "+s;
			}
			File f = new File("WebPages//"+newTitle+".txt");
			f.createNewFile();			
			PrintWriter pw = new PrintWriter(f);
			pw.println(url);
			pw.println(text);
			pw.close();
			
		} catch (FileNotFoundException e) {e.getMessage();} catch (IOException e) {e.getMessage();}
		
	}
	
	public static String urlToHTML(String url){
		try {
			URL url1 = new URL(url);
			URLConnection conn = url1.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(5000);
			Scanner sc = new Scanner(conn.getInputStream());
			StringBuffer sb = new StringBuffer();
			while(sc.hasNext()) {
				sb.append(" "+sc.next());
			}
			
			String result = sb.toString();
			sc.close();
			return result;
		}
		catch(IOException e) {System.out.println(e);} 
		return url;
	}
	
	public static void crawlPages(String urls) {
		
		try {
			File f = new File("CrawledPages.txt");
			f.createNewFile();
			FileWriter fwt = new FileWriter(f);
			fwt.close();
						
			String urls2 = "";
			for(String url: urls.split("\n")) {				
				urls2 = urls2 + crawl(url, 6);
					
				FileWriter fw = new FileWriter(f,true);
				fw.write(url + "\n");
				fw.close();
				
			}
			
			String urls3 = "";
			for(String url: urls2.split("\n")) {
				In in = new In(f);
				String urlsRead = in.readAll();
				if(!urlsRead.contains(url) ) {
					urls3 = urls3 + crawl(url, 6);
					
					FileWriter fw = new FileWriter(f,true);
					fw.write(url + "\n");
					fw.close();
				}
				//System.out.println(url);				
				
			}
			
			for(String url: urls3.split("\n")) {
				In in = new In(f);
				String urlsRead = in.readAll();
				if(!urlsRead.contains(url)  ) {
					crawl(url, 3);
					
					FileWriter fw = new FileWriter(f,true);
					fw.write(url + "\n");
					fw.close();
				}
				//System.out.println(url);				
				
			}
		
		}
		catch(Exception e) {e.printStackTrace();}
	}
}
