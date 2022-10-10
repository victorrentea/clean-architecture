package victor.training.clean;

import java.sql.SQLException;

public class StartDatabase {
	public static void main(String[] args) throws SQLException {
		System.out.println("Started DB...");
		System.out.println("Connecting to 'jdbc:h2:tcp://localhost:9092/~/test' will auto-create a database file 'test.mv.db' in user home (~)...");

		// Allow auto-creating new databases on disk at first connection
		org.h2.tools.Server.createTcpServer("-ifNotExists").start();
	}

}
