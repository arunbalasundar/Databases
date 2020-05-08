import java.time.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
//Importing date classes
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import static com.mongodb.client.model.Filters.*;

public class UseMongoDB {
	// MongoDB listens on port 27017 by default.
	private MongoClient mongoClient = new MongoClient("localhost", 27017);

	// This is the database we use in the exercise.
	private MongoDatabase db = mongoClient.getDatabase("mybookstore");

	// This gives you a collection of Mongo documents. See the
	// documentation of MongoCollection and org.bson.Document for more
	// fields and methods associated with these two classes.
	private MongoCollection<Document> books = db.getCollection("books");

	void showSomeBooks() {
		System.out.println("0. Example: Some of the books in the collection");
		FindIterable<Document> someBooks = books.find();
		for (Document book : someBooks) {
			System.out.println(book.toJson());
		}
	}

	void insertNewBook() {
		System.out.println("1. Insert a new book");
		// write your code here
		//FindIterable<Document> insertOBook = books.find();
		Document book = new Document();
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
		String date = "";
		try{
			Date ISODate = dateFormat.parse("2010-13-04T00:00:00");
			date = dateFormat.format(ISODate);
		}catch(Exception e){
			//Do Something
			System.out.println(e.getMessage());
		}

		book.put("book_id", 9);
		book.put("title", "Prince of Persia: The Sands of Time");
		book.put("category", "Fiction");
		book.put("price", 15.13);

		Document author = new Document();
		author.put("first_name", "James");
		author.put("last_name", "Ponti");
		author.put("country", "it");
		author.put("website", "http://www.jamesponti.com/");
		book.put("author", author);

		Document publisher = new Document();
		publisher.append("publish_date", date);
		publisher.put("name", "Disney Press");
		publisher.put("country", "us");
		publisher.put("website", "https://www.disneyabcpress.com/");
		book.put("publishers", publisher);

		books.insertOne(book);
		
//		System.out.println(book);

		System.out.println("One New book has been inserted");

	}

	void showExpensiveBooks() {
		System.out.println("2. All books whose price is greater than $14");
		// write your code here
		FindIterable<Document> booksByPrice = books.find(and(gt("price", 14)));
		for (Document book : booksByPrice) {
			System.out.println(book.toJson());
		}

	}

	/**
	 * Create a Date from a year, month and day at UTC midnight.
	 */
	Date date(int year, int month, int day) {
		LocalDate date = LocalDate.of(year, month, day);
		ZonedDateTime dateTime = date.atStartOfDay(ZoneOffset.UTC);
		return Date.from(dateTime.toInstant());
	}

	void run() {
		showSomeBooks();
		insertNewBook();
		showExpensiveBooks();
	}

	public static void main(String[] args) {
		// make the MongoDB driver quiet
		Logger.getLogger("org.mongodb.driver").setLevel(Level.WARNING);
		new UseMongoDB().run();
	}
}
