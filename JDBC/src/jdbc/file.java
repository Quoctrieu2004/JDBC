package jdbc;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class file {
  public static void main(String[] args) {
	try {
		Connection con=  connectionDB();
		String sql = "Select * from LopHoc";
		Statement state= con.createStatement();
		ResultSet rs=  state.executeQuery(sql);
		while(rs.next()) {
			String maLop= rs.getString("MaLop");
			String tenLop= rs.getString("TenLop");
			String soPhong= rs.getString("SoPhong");
			System.out.printf("%s - %s tai %s \n", maLop, tenLop, soPhong);
		}
		con.close();
	} catch (ClassNotFoundException e) {
		System.out.print("Error: "+ e.getMessage());
	}catch (SQLException e) {
		System.out.print("Loi SQL Chi tiet:"+ e.getMessage());
	}
  }
  static Connection connectionDB() throws ClassNotFoundException, SQLException {
	  Class.forName("org.sqlite.JDBC");
	  String str= "jdbc:sqlite::resource:name.db";
	  Connection con= DriverManager.getConnection(str);
	  return con;
  }
}
