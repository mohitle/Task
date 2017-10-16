import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UniqueDomains {

	public static ArrayList<String> getAllLinks() {
		ArrayList<String> finalList = new ArrayList<String>();
		ArrayList<String> tempList = new ArrayList<String>();
		String readString;
		Scanner sc = new Scanner(System.in);
		System.out.println("please type");
		String input = sc.next();
		try {
			URL url = new URL(input);
			BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
			while ((readString = in.readLine()) != null) {
				if (!readString.contains("<a") && !readString.contains("<link") && readString.contains("href=")) {
					finalList.add(readString);
				}
				tempList = extractTagAText(readString);	
				for (String u : tempList) {
					finalList.add(u);
				}
			}
			in.close();
		}
		catch (Exception e) {
			if (e instanceof IOException) {
				System.out.println("no access to URL: " + input);
			} else if (e instanceof MalformedURLException) {
				System.out.println("URL " + input + "malformed.");
			} else {
			}
		}

		int counter = 0;
		for (String u : finalList) {
			String link = extractLink(u);
			finalList.set(counter, link);
			counter++;
		}

		System.out.println("Links for the URL \"" + input + "\": " + (finalList.size()) + ".");
		
		sc.close();

		finalList.add(input);
		
		return finalList;
	}

	public static HashMap<String, Integer> listUniqueDomains(ArrayList<String> urls) {
		HashMap<String, Integer> domains = new HashMap<String, Integer>();

		String mainUrl = urls.get(urls.size()-1);
		urls.remove(urls.size()-1);
		String mainUrlDomain = getDomain(mainUrl);
		int counterMainUrl = 0;
		
		String invalidUrls = "Invalid URLs";
		int invalidUrlsCounter = 0;

		int counter = 0;
		for (String u : urls) {
			if (u.equals("#") || u.startsWith("javascript") || u.equals("") || u.equals(" ")) {
				if (!domains.containsKey(invalidUrls)) {
					domains.put(invalidUrls, invalidUrlsCounter + 1);
				} else {
					domains.put(invalidUrls, domains.get(invalidUrls) + 1);
				}				
			} else if (!u.startsWith("http")) {
				counterMainUrl++;
			} else {
				String domain = getDomain(u);
				if (!domains.containsKey(domain)) {
					int counterUrl = 0;
					domains.put(domain, counterUrl + 1);
				} else {
					if (domain.equals(mainUrlDomain)) {
						counterMainUrl++;
					} else {
						int val = domains.get(domain) + 1;
						domains.put(domain, val);
					}
				}
			}
		}	


		domains.put(mainUrlDomain, counterMainUrl);

		System.out.println("Unique domains (" + domains.size() + ") - Number of links each");

		for (String key : domains.keySet()) {
			System.out.println(key + " - " + domains.get(key));
		}

		return domains;
	}

	public static String getDomain(String url) {
		String uniqueDomain = "";
		try {
			URL u = new URL(url);
			uniqueDomain = u.getHost().toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return uniqueDomain;
	}

	public static ArrayList<String> extractTagAText(String text) {
		ArrayList<String> listOfElemnts = new ArrayList<String>();
		String regex = "(<a [^>]*href=)(.*?)(>|\\s|$)";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);

		while (matcher.find()) {
			String element = matcher.group();
			//			System.out.println(str);
			listOfElemnts.add(element);
		}
		//		System.out.println("extract " + urls.toString());
		return listOfElemnts;
	}

	public static String extractLink(String text) {
		String link = "";
		String[] partsOfLink = new String[2];

		/* Splitting text at the start */
		if (text.contains("href=\"")) {
			partsOfLink = text.split("href=\"");
			link = partsOfLink[1];
		} else if (text.contains("href='")) {
			partsOfLink = text.split("href='");
			link = partsOfLink[1];
		} else {
			partsOfLink = text.split("href=");
			link = partsOfLink[1];
		}

		/* Splitting text at the end */
		if (link.contains("\"")) {
			partsOfLink = link.split("\"");
			link = partsOfLink[0];
		} else if (link.contains("'")) {
			partsOfLink = link.split("'");
			link = partsOfLink[0];
		} else {
			partsOfLink = link.split(">");
			link = partsOfLink[0];
		}

		return link;
	}

	public static void main(String[] args){
		listUniqueDomains(getAllLinks());

		String text = "<a class=\"js-gps-track -link\" data-gps-track=\"footer.click({ location: 2, link:4 })\" href=\"/stackoverflow.blog?blb=1\">";
		extractLink(text);


		//		String text1 = "<a asa href=\"//www.youtube.com/yt/about/it/\"> Informazioni</a> <a href=\"//www.youtube.com/yt/about/it/\"> Informazioni</a>";
		//		extract(text);
		//		extractUrls(text);
	}
}
// http://www.vogella.com/tutorials/JavaRegularExpressions/article.html
//
//public static ArrayList<String> extractUrls(String text) {
//	String tagA = "(<a [^>]+>)"; // (<a [^>]+>+?)  (<a)( href|[.+]*)[^>]+>+?
//	Pattern pattern = Pattern.compile(tagA);
//	Matcher matcher = pattern.matcher(text);
//
//	ArrayList<String> urls = new ArrayList<String>();
//
//	String[] array = new String[2];
//	while (matcher.find()) {
//		String str = matcher.group();
//		if (str.contains("href")) {
//			String result = str.substring(str.indexOf("href=") + 1, str.indexOf(">"));
//			if (result.contains(" ")) {
//				array = result.split(" ");
//				result = array[0];
//			}
//			urls.add(result);
//		}
//	}
//			System.out.println("extractUrls " + urls.toString());
//	return urls;
//}




//public static ArrayList<String> extractUrl(String text) {
//	ArrayList<String> urls = new ArrayList<String>();
//	String rex = "(<a [^>]*href=)(.*?)(>|\\s|$)";
//	Pattern pattern = Pattern.compile(rex);
//	Matcher matcher = pattern.matcher(text);
//
//	while (matcher.find()) {
//		String str = matcher.group();
//		//			System.out.println(str);
//		urls.add(str);
//	}
//	//		System.out.println("extract " + urls.toString());
//	return urls;
//}
//
//public static String extractDomain(String text) {
//	
//	
//	String domain = "";
//	String[] a = new String[2];
//
//	/* Splitting text at the start */
//	if (text.contains("href=\"")) {
//		a = text.split("href=\"");
//		domain = a[1];
//	} else if (text.contains("href='")) {
//		a = text.split("href='");
//		domain = a[1];
//	} else {
//		a = text.split("href=");
//		domain = a[1];
//	}
//			
//	/* Splitting text at the end */
//	if (domain.contains("\"")) {
//		a = domain.split("\"");
//		domain = a[0];
//	} else if (domain.contains("'")) {
//		a = domain.split("'");
//		domain = a[0];
//	} else {
//		a = domain.split(">");
//		domain = a[0];
//	}
//	
//	
//	try {
//		URL u = new URL(domain);
//		System.out.println("questa è la url " + u.toString());
//		System.out.println("questo è l'host " + u.getHost());
//
//	} catch (MalformedURLException e) {
//		e.printStackTrace();
//	}
//	
//	
//	/* Removing prefix */
//	if (domain.startsWith("http://")) {
//		a = domain.split("http://");
//		domain = a[1];
//	} else if (domain.startsWith("http://www.")) {
//		a = domain.split("http://www.");
//		domain = a[1];
//	} else if (domain.startsWith("https://")) {
//		a = domain.split("https://");
//		domain = a[1];
//	} else if (domain.startsWith("https://www.")) {
//		a = domain.split("https://www.");
//		domain = a[1];
//	} else if (domain.startsWith("/")) { // comincia con / o ../ ed è quindi un caso della stessa url
//		
//	} else if (domain.startsWith("../")) {
//		
//	} else {
//		System.out.println("Case like / and ../");
//	}
//	
//	/* Removing www. */
//	if (domain.startsWith("www.")) {
//		a = domain.split("www.");
//		domain = a[1];
//	}
//	
//	// fin qui intera url a partire dal domain name fino alla fine (non solo il domain name)
//	
//	
//	// estrarre domain name
//	String[] a2 = domain.split("(\\..+?)");
//	domain = a2[0];
//	String n = a2[1];
//	
//	System.out.println(a2.length);
//	System.out.println(domain + " and " + n);
//	
//	return domain;
//}