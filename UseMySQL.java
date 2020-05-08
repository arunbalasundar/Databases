import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.text.DecimalFormat;

public class UseMySQL {

	private Connection conn;
	private static DecimalFormat df2 = new DecimalFormat(".##");

	private void useDatabase() throws SQLException {
		// Write your code here
		Statement st = conn.createStatement();
		st.executeUpdate("INSERT INTO comp575_authors (first_name, last_name, country, website)"
			+ "VALUES ('J.M.', 'Barlog', 'us', 'http://www.jmbarlog.com')");

		String query = "INSERT INTO comp575_books (title, author_id, category, price)"
			+ "values (?, ?, ?, ?),"
				+ "(?, ?, ?, ?);";

		PreparedStatement preparedStmt = conn.prepareStatement(query);
		preparedStmt.setString (1, "God of War: The Official Novelization");
		preparedStmt.setInt (2, 5);
		preparedStmt.setString (3, "Fiction");
		preparedStmt.setDouble (4, 15.27);
		preparedStmt.setString (5, "Dark Side: The Haunting");
		preparedStmt.setInt (6, 5);
		preparedStmt.setString (7, "Horror");
		preparedStmt.setDouble (8, 37.64);

		preparedStmt.execute();

		String query2 = "SELECT * FROM comp575_books";

		ResultSet rs = st.executeQuery(query2);
		System.out.println("book_id\ttitle\tauthor_id\tcategory\tprice\tpublisher_id\tpublish_date");		
		
		while (rs.next()) {
			int id = rs.getInt("book_id");
			String title = rs.getString("title");
			int aut_id = rs.getInt("author_id");
			String category = rs.getString("category");
			double price = rs.getFloat("price");
			int pub_id = rs.getInt("publisher_id");
			String date = rs.getString("publish_date");
			if(date == null){
				date = "N/A";
			}
			System.out.println(id+"\t"+title+"\t"+aut_id+"\t"+category+"\t"+df2.format(price)+"\t"+pub_id+"\t"+date);
		}
		st.close();
		conn.close();
	}

	/**
	 * Set up the connection to the database.
	 */
	private void connect() throws SQLException {
		String username = System.getProperty("user.name");
		String password = System.getenv("MYSQL_PASSWORD");
		String database = username;
		String url = "jdbc:mysql://mysql.cms.waikato.ac.nz/" + database;
		if (password == null) {
			if (System.console() == null) {
				System.err.println(
					"MySQL password not set and console unavailable.\n" +
					"Please set the password from the terminal using\n" +
					"  export MYSQL_PASSWORD=\"your_password\"\n" +
					"or set it in line 2 of UseMySQL.connect().");
				System.exit(1);
			}
			// read password from console
			password = new String(System.console().readPassword("MySQL password: "));
		}
		Properties info = new Properties();
		info.setProperty("user", username);
		info.setProperty("password", password);
		conn = DriverManager.getConnection(url, info);
	}

	void run() throws SQLException {
		try {
			connect();
			useDatabase();
		} finally {
			if (conn != null) {
				conn.close();
			}
		}
	}

	public static void main(String[] args) {
		try {
			new UseMySQL().run();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
