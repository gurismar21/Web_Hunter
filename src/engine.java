import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

public class engine {

	private static final String url = "https://www.javatpoint.com/";
	private static final String RegularExprWord = "[[ ]*|[,]*|[)]*|[(]*|[\"]*|[;]*|[-]*|[:]*|[']*|[’]*|[\\.]*|[:]*|[/]*|[!]*|[?]*|[+]*]+";
	
	public static void main(String [] args) throws IOException {
				
		SearchEngine se = new SearchEngine();
		System.out.println("Welcome to WEB HUNTER - Crawling The Website for you !!!!!");
		System.out.println("\n");
		HashSet<String> ht = se.createTrie(url);//create hash table using trie
		
	
		
		boolean bool = true;
		Scanner sc = new Scanner(System.in);
		String serword;
		while(bool)
		{
			System.out.println("Please Enter the word You want to search for : ");
			System.out.println("\n");
			serword = sc.next();//take the input from the user
			if(!serword.equals(null)) 
			{
				String [] sp = serword.split(RegularExprWord);//spilt the regular expression word and the entered word
				String[] allsearchpages = se.search(sp);
				try {
					if (allsearchpages == null)
					{//to suggest the similar words
					}
					else 
					{
						Map<String, Integer> ul = null;//for unsorted Links
						ul = new HashMap<>();
						
						for (String url : allsearchpages) {
							
							ul.put(url, PageRanking.WordOcuurence(url, serword));//store all the links found in the hashmap
						}
					LinkedHashMap<String, Integer> reverseMap = new LinkedHashMap<>();//to reverse the map , in accordance with the frequency
						
				    ul.entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).forEachOrdered(x -> reverseMap.put(x.getKey(), x.getValue()));
			        System.out.println("");
			     // to print the page ranks and corresponding web page links
				        System.out.println(" Page ranked according to the number of words found in a single webpage : ");	
				        System.out.println("\n");
				        int count = 1;
				        for (Map.Entry<String, Integer> putin : reverseMap.entrySet()) {
				        	if(count > 10)
				        		break;
				            System.out.println(" Number of occurences of the word which you searched for is  :  "+putin.getValue() + " in website " + putin.getKey()+" ");
				            
				            count++;
				        }
				        System.out.println("\n\n");
				        // prompt to choose to continue or exit the web search engine
				        System.out.println("If you want to continue to search ? -------- Press '1' \n  If you want to Exit? -------- Press '2' ");
				        System.out.println("\n");
				        while(true) {
				        	String inp = sc.next();
				        	if(inp.equals("1")) {
					        	break;
					        }
				        	else if(inp.equals("2")) {
				        		bool = false;
					        	System.out.println("Thanks For using our Search Engine !!!");
					        	System.exit(1);
					        	sc.close();
				        	}
				        	else 
				        	{
				        		System.out.println("It was a wrong Choice,Please enter a valid choice 1 or 0.  \n If you want to continue to search ? -------- Press '1' \\n  If you want to Exit? -------- Press '2' ");
				        	}
				        }
					}
				
				}
				catch(Exception e) {
					System.out.println(e);
				}
		}
		else 
		{
			System.out.println("Kindly enter a valid word, "+serword+" is not a valid word.");
			continue;
		}	
		}
	}
}
