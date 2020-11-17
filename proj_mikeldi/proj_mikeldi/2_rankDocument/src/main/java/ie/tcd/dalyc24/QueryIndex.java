package ie.tcd.dalyc24;

import java.io.IOException;

import java.util.Scanner;

import java.nio.file.Paths;
import java.nio.file.Files;


import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

import org.apache.lucene.document.Document;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import org.apache.lucene.index.Term;
import org.apache.lucene.index.DirectoryReader;

import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.queryparser.classic.ParseException;

import java.util.ArrayList;
import org.apache.lucene.index.Term;
import java.lang.Math;
import java.io.IOException;
import java.io.StringReader;
import java.lang.Object;
//weka.core.Stopwords;

public class QueryIndex
{

	// the location of the search index
	private static String INDEX_DIRECTORY = "../index";
	
	// Limit the number of search results we get
	private static int MAX_RESULTS = 10;

	public static void main(String[] args) throws IOException, ParseException
	{
		// Analyzer used by the query parser.
		// Must be the same as the one used when creating the index
		Analyzer analyzer = new StandardAnalyzer();
		
		// Open the folder that contains our search index
		Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));
		
		// create objects to read and search across the index
		DirectoryReader ireader = DirectoryReader.open(directory);
		IndexSearcher isearcher = new IndexSearcher(ireader);

		// Create the query parser. The default search field is "content", but
		// we can use this to search across any field
		QueryParser parser = new QueryParser("content", analyzer);
		
		String queryString = "";
		Scanner scanner = new Scanner(System.in);


		do
		{
			// trim leading and trailing whitespace from the query
			queryString = queryString.trim();


			// if the user entered a querystring
			if (queryString.length() > 0)
			{
				String[] stopwords = {"a", "as", "able", "about", "above", "according", "accordingly", "actually", "after", "afterwards", "again", "aint", "all", "allow", "allows", "almost", "alone", "along", "already", "also", "although", "always", "am", "among", "amongst", "an", "and", "another", "any", "anybody", "anyhow", "anyone", "anything", "anyway", "anyways", "anywhere", "appear", "appreciate", "appropriate", "are", "arent", "around", "as", "aside", "ask", "asking", "associated", "at", "available", "away", "awfully", "be", "became", "because", "become", "becomes", "becoming", "been", "before", "beforehand", "behind", "being", "believe", "below", "beside", "besides", "best", "better", "between", "beyond", "both", "brief", "but", "by", "cmon", "cs", "came", "can", "cant", "cannot", "cant", "cause", "causes", "certain", "certainly", "changes", "clearly", "co", "com", "come", "comes", "concerning", "consequently", "consider", "considering", "contain", "containing", "contains", "corresponding", "could", "couldnt", "course", "currently", "definitely", "described", "despite", "did", "didnt", "different", "do", "does", "doesnt", "doing", "dont", "done", "each", "edu", "eg", "eight", "either", "else", "elsewhere", "enough", "entirely", "especially", "et", "etc", "even", "ever", "every", "everybody", "everyone", "everything", "everywhere", "ex", "exactly", "example", "except", "far", "few", "ff", "fifth", "first", "for", "former", "formerly", "forth", "four", "from", "further", "furthermore", "get", "gets", "getting", "given", "gives", "go", "goes", "going", "gone", "got", "gotten", "greetings", "had", "hadnt", "happens", "hardly", "has", "hasnt", "have", "havent", "having", "he", "hes", "hello", "help", "hence", "her", "here", "heres", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "hi", "him", "himself", "his", "hither", "hopefully", "how", "howbeit", "however", "i", "id", "ill", "im", "ive", "ie", "if", "ignored", "immediate", "in", "inasmuch", "inc", "indeed", "insofar", "instead", "into", "is", "isnt", "it", "itd", "itll", "its", "its", "itself", "just", "keep", "keeps", "kept", "know", "knows", "known", "last", "lately", "later", "latter", "latterly", "least", "less", "lest", "let", "lets", "like", "liked", "likely", "little", "look", "ltd", "mainly", "many", "may", "maybe", "me", "mean", "meanwhile", "merely", "might", "more", "moreover", "most", "mostly", "much", "must", "my", "myself", "name", "namely", "nd", "near", "nearly", "need", "needs", "neither", "never", "nevertheless", "next", "no", "nobody", "non", "none", "noone", "nor", "normally", "not", "nothing", "novel", "now", "nowhere", "obviously", "of", "off", "often", "oh", "ok", "okay", "old", "on", "once", "one", "ones", "only", "onto", "or", "other", "others", "otherwise", "ought", "our", "ours", "ourselves", "out", "outside", "over", "overall", "own", "per", "perhaps", "please", "plus", "possible", "presumably", "que", "quite", "qv", "rather", "rd", "re", "really", "regarding", "regardless", "regards", "relatively", "respectively", "said", "saw", "say", "saying", "says", "secondly", "several", "shall", "she", "should", "shouldnt", "since", "so", "some", "somebody", "somehow", "someone", "something", "sometime", "sometimes", "somewhat", "somewhere", "soon", "sorry", "specified", "such", "sup", "sure", "ts", "take", "taken", "tell", "tends", "th", "than", "thanx", "that", "thats", "thats", "the", "their", "theirs", "them", "themselves", "then", "thence", "there", "theres", "thereafter", "thereby", "therefore", "therein", "theres", "thereupon", "these", "they", "theyd", "theyll", "theyre", "theyve", "think", "third", "this", "those", "though", "three", "thru", "thus", "to", "truly", "try", "two", "un", "under", "unfortunately", "unless", "unlikely", "until", "unto", "up", "upon", "us", "use", "used", "uses", "using", "usually", "value", "various", "very", "via", "viz", "vs", "was", "wasnt", "way", "we", "wed", "well", "were", "weve", "welcome", "went", "were", "werent", "what", "whats", "whatever", "when", "whence", "where", "wheres", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whos", "whoever", "whole", "whom", "whose", "why", "will", "willing", "wish", "with", "within", "without", "wont", "wonder", "would", "would", "wouldnt", "yes", "yet", "you", "youd", "youll", "youre", "youve", "your", "yours", "yourself", "yourselves"};
				String[] str = queryString.split(" ");
				ArrayList<Document> documents = new ArrayList<Document>();
				double tfidf[];
				tfidf = new double[1401];
				double bm25[];
				bm25 = new double[1401];
				double K= 1.2;
				double B = 0.75;

				for (String s : str) {
					s = s.toLowerCase();
					boolean notStp = true;
					int cnt = 0;
					int c = 0;
					for (String stop : stopwords) {
						if (s.length() == stop.length()) {
							for(c=0; c<stop.length(); c++) {
								if (s.charAt(c) == stop.charAt(c)) {
									cnt++;
								}
							}
							if (cnt == c) {
								notStp = false;
							}
						}
					}

					int num_docs = 0;
					int freq[];
					freq = new int[1401];
					int num_words[];
					int avg_words = 0;
					num_words = new int[1401];
					String term = s;
					for (int i=0; i<1401; i++) {
						freq[i] = 0;
						num_words[i]=0;
						documents.add(ireader.document(i));
						String text = ireader.document(i).get("filename") + ireader.document(i).get("content");
						String words[] = text.split("\\s+");
						for (String word : words) {
							num_words[i]++;
							boolean match = true;
							String word2 = word.replace(",", "");
							word2 = word2.toLowerCase();
							if (word2.length() == term.length()) {
								for(int n=0; n<word2.length(); n++) {
									if (word2.charAt(n) != term.charAt(n)) {
										match = false;
									}
								}
							}
							else {
								match = false;
							}
							notStp = true;
							if((match==true)&&(notStp==true)) {
								freq[i]++;
							}
						}
						avg_words = avg_words + num_words[i];
						if (freq[i] > 0) {
							num_docs++;
						}
					}
					avg_words = avg_words/1401;
					for (int i=0; i<1401; i++) {
						if (num_docs > 0) {
							tfidf[i] = tfidf[i] + (freq[i]*Math.log(1401/num_docs));
							bm25[i] = bm25[i] + ((freq[i]*(K+1))/(freq[i]+K*(1-B+(B*num_words[i]/avg_words)))*Math.log(1+((1401-num_docs + 0.5)/(num_docs+0.5))));
						}
					}
				}


				int max_index = 0;
				double max = 0;
				int top_index[];
				top_index = new int [30];
				double top_max[];
				top_max = new double [30];
				boolean notList = true;

				int max_index_BM = 0;
				double max_BM = 0;
				int top_index_BM[];
				top_index_BM = new int [30];
				double top_max_BM[];
				top_max_BM = new double [30];
				boolean notListBM = true;

				for (int a=0; a<30; a++) {
					max_index = 0;
					max = 0;
					for(int i=0; i<1401; i++) {
						notList = true;
						for (int b=0; b<30; b++) {
							if (top_index[b] == i) {
								notList = false;
							}
						}
						if ((tfidf[i] > max) && (notList==true)) {
							max = tfidf[i];
							max_index = i;
						}
					}
					top_index[a] = max_index;
					top_max[a] = max;
				}

				for (int a=0; a<30; a++) {
					max_index_BM = 0;
					max_BM = 0;
					for(int i=0; i<1401; i++) {
						notListBM = true;
						for (int b=0; b<30; b++) {
							if (top_index_BM[b] == i) {
								notListBM = false;
							}
						}
						if ((bm25[i] > max_BM) && (notListBM==true)) {
							max_BM = bm25[i];
							max_index_BM = i;
						}
					}
					top_index_BM[a] = max_index_BM;
					top_max_BM[a] = max_BM;
				}
				System.out.println("\n tf-idf");
				for (int i=0; i<20; i++) {
					if (top_max[i] > 0) {
						System.out.println(top_index[i]);
						System.out.println(top_max[i]);
					}
				}
				System.out.println("\n BM 25");
				for (int i=0; i<20; i++) {
					if (top_max_BM[i] > 0) {
						System.out.println(top_index_BM[i]);
						System.out.println(top_max_BM[i]);
					}
				}



				// Get the set of results
				/*ScoreDoc[] hits = isearcher.search(query, MAX_RESULTS).scoreDocs;


				// Print the results
				System.out.println("Documents: " + hits.length);
				for (int i = 0; i < hits.length; i++)
				{
					Document hitDoc = isearcher.doc(hits[i].doc);
					System.out.println(i + ") " + hitDoc.get("filename") + " " + hits[i].score);
				}*/

			}
			
			// prompt the user for input and quit the loop if they escape
			System.out.print(">>> ");
			queryString = scanner.nextLine();
		} while (!queryString.equals("\\q"));
		
		// close everything and quit
		ireader.close();
		directory.close();
	}

}
