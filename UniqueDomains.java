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
	}
}
