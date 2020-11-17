package ie.tcd.dalyc24;

import java.io.IOException;

import java.util.ArrayList;

import java.nio.file.Paths;
import java.nio.file.Files;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class CreateIndex
{
	// Directory where the search index will be saved
	private static String INDEX_DIRECTORY = "../index";
	private Document doc;

	public static void main(String[] args) throws IOException
	{
		// Make sure we were given something to index
		if (args.length <= 0)
		{
            System.out.println("Expected corpus as input");
            System.exit(1);
        }

		// Analyzer that is used to process TextField
		Analyzer analyzer = new StandardAnalyzer();

		// ArrayList of documents in the corpus
		ArrayList<Document> documents = new ArrayList<Document>();

		// Open the directory that contains the search index
		Directory directory = FSDirectory.open(Paths.get(INDEX_DIRECTORY));

		// Set up an index writer to add process and save documents to the index
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
		IndexWriter iwriter = new IndexWriter(directory, config);


		for (String arg : args)
		{

			// Load the contents of the file
			//System.out.printf("Indexing \"%s\"\n", arg);
			String content = new String(Files.readAllBytes(Paths.get(arg)));

			String[] big = content.split(".I");
			for (String a : big) {
				Document doc = new Document();
				int count = 0;
				String[] small = a.split(".A");
				for (String b : small) {
					if (count == 0) {
						doc.add(new StringField("filename", b, Field.Store.YES));
					}
					else {
						doc.add(new TextField("content", b, Field.Store.YES));
					}
					count++;
				}
				documents.add(doc);
			}
			for (Document doc : documents) {
				System.out.println(doc.get("filename"));
			}
		}

		// Write all the documents in the linked list to the search index
		iwriter.addDocuments(documents);

		// Commit everything and close
		iwriter.close();
		directory.close();
	}
}
