package dbpakage;

import java.sql.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

import java.sql.PreparedStatement; // 동적sql

public class DBManager {

	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://127.0.0.1:3306/phonedb?severTimeZone=UTC";
	private String id = "root";
	private String pw = "6532";

	private Connection conn = null;
	private Statement stmt = null;

	public DBManager() {
		// TODO Auto-generated constructor stub
	}

	public void initDBConnect() { // db 연동
		try {
			Class.forName(driver); // driver을 메모리에 로드한다. (driver: 클래스이다.)
			this.conn = DriverManager.getConnection(this.url, this.id, this.pw); // getConnection: 커넥션 객체를 만들어줌
			this.stmt = conn.createStatement(); // 연결객체를 통해서 명령객체가 만들어져서 stmt에 넣는다.

		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public int recordCount() {
		String sql = "select count(*) as cnt from usertbl";
		int recount = 0;
		try {
			ResultSet rs = stmt.executeQuery(sql);

			if (rs.next()) {
				recount = rs.getInt("cnt");
			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return recount;
	}

	public User[] allFetch() {
		int recount = this.recordCount();
		User[] userList = new User[recount];
		int userCount = 0;
		String sql = "select * from usertbl";

		try {
			ResultSet rs = stmt.executeQuery(sql);

			while (rs.next()) { // rs.next(): rs 레코드 하나를 본인이 가지고 있다.
				String id = rs.getString("userid");
				String username = rs.getString("username");
				int birthyear = rs.getInt("birthyear");
				String addr = rs.getString("addr");
				String mobile1 = rs.getString("mobile1");
				String mobile2 = rs.getString("mobile2");
				int height = rs.getInt("height");
				Date mdate = rs.getDate("mdate");

				userList[userCount++] = new User(id, username, birthyear, addr, mobile1, mobile2, height, mdate);
			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return userList;
	}

	public void selectUser(String username) {
		String sql = "select * from usertbl where username=?";
		try {
//			ResultSet rs = stmt.executeQuery(sql);
			PreparedStatement pstmt = this.conn.prepareStatement(sql);
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) { // rs.next(): rs 레코드 하나를 본인이 가지고 있다.
				String id = rs.getString("userid");
				String name = rs.getString("username");
				int birthyear = rs.getInt("birthyear");
				String addr = rs.getString("addr");
				String mobile1 = rs.getString("mobile1");
				String mobile2 = rs.getString("mobile2");
				int height = rs.getInt("height");
				Date mdate = rs.getDate("mdate");

				System.out.println(id);
				System.out.println(name);
				System.out.println(birthyear);
			}
			rs.close();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void inputUser(User user) {
		String sql = "insert into usertbl values(?, ?, ?, ?, ?, ?, ?, ?)";

		try {
//			ResultSet rs = stmt.executeQuery(sql);
			PreparedStatement pstmt = this.conn.prepareStatement(sql);
			pstmt.setString(1, user.getUserId());
			pstmt.setString(2, user.getUserName());
			pstmt.setInt(3, user.getBirthYear());
			pstmt.setString(4, user.getAddr());
			pstmt.setString(5, user.getMobile1());
			pstmt.setString(6, user.getMobile2());
			pstmt.setInt(7, user.getHeight());
			pstmt.setDate(8, user.getMdate());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void releaseDB() {
		try {
			this.conn.close();
			this.stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
