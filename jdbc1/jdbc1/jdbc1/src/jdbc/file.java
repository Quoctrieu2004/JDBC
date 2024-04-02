package jdbc;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class file {
	public static void main(String[] args) throws SQLException, FileNotFoundException, UnsupportedEncodingException {
		try {
			Connection con = connectionDB();
			Scanner scanner = new Scanner(System.in);
			while (true) {
				System.out.println("\n----MENU----\n");
				System.out.println("1. Thêm lớp học");
				System.out.println("2. Cập nhật thông tin lớp học");
				System.out.println("3. Xóa lớp học");
				System.out.println("4. Thống kê số lượng lớp học");
				System.out.println("5. Thống kê số lượng sinh viên");
				System.out.println("6. In danh sách lớp học ra file");
				System.out.println("7. Hiển thị danh sách lớp học");
				System.out.println("8. Thoát");
				System.out.print("Chọn chức năng: ");
				int choice = scanner.nextInt();
				scanner.nextLine();
				if (choice == 1) {
					
					themLop(scanner, con);
				} else if (choice == 2) {
					
					capNhatLop(scanner, con);
				} else if (choice == 3) {
					
					xoaLop(scanner, con);
					System.out.println("\n Da xoa \n");
				} else if (choice == 4) {
					demLop(con);
				} else if (choice == 5) {
					demSinhVien(con);
				} else if (choice == 6) {
					XuatraFile();
				} else if (choice == 7) {
					In();
				} else if (choice == 8) {
					System.out.println("Đã thoát");
					break;
				} else {
					System.out.println("Lựa chọn không hợp lệ, vui lòng thử lại.");
				}
			}
		} catch (ClassNotFoundException | SQLException e) {
			System.out.print("Error: " + e.getMessage());
		}
	}

	static Connection connectionDB() throws ClassNotFoundException, SQLException {
		Class.forName("org.sqlite.JDBC");
		String str = "jdbc:sqlite::resource:name.db";
		Connection con = DriverManager.getConnection(str);
		return con;
	}

	static void themLop(Scanner scanner, Connection con) {
		System.out.print("Nhap ma lop: ");
		String ma = scanner.nextLine();
		System.out.print("Nhap ten lop: ");
		String ten = scanner.nextLine();
		System.out.print("Nhap so phong: ");
		String so = scanner.nextLine();
		try (Statement statement = con.createStatement()) {
			String sql = String.format("INSERT INTO LopHoc (MaLop, TenLop, SoPhong) VALUES ('%s', '%s', '%s')", ma, ten,
					so);
			statement.executeUpdate(sql);
			System.out.println("Lớp học đã được thêm.");
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}

	static void capNhatLop(Scanner scanner, Connection con) {
		System.out.print("Nhap ma lop can tim: ");
		String maLopCanTim = scanner.nextLine();
		System.out.println("Cap nhat lop voi ma lop la: " + maLopCanTim);
		System.out.print("Nhap ten lop: ");
		String ten = scanner.nextLine();
		System.out.print("Nhap so phong: ");
		String so = scanner.nextLine();
		try (Statement statement = con.createStatement()) {
			String sql = String.format("UPDATE LopHoc SET TenLop = '%s', SoPhong= '%s' WHERE MaLop = '%s'", ten, so,
					maLopCanTim);
			int rowsAffected = statement.executeUpdate(sql);
			if (rowsAffected > 0) {
				System.out.println("Lớp học đã được cập nhật.");
			} else {
				System.out.println("Không tìm thấy lớp học có mã: " + maLopCanTim);
			}
		} catch (SQLException e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	static void xoaLop(Scanner scanner, Connection con) {
        System.out.print("Nhap ma lop can xoa: ");
        String maLopCanXoa = scanner.nextLine();
        try (Statement statement = con.createStatement()) {
            String sql = String.format("DELETE FROM LopHoc WHERE MaLop='%s'", maLopCanXoa);
            int rowsAffected = statement.executeUpdate(sql);
            if (rowsAffected > 0) {
                System.out.println("Lớp học đã được xóa.");
            } else {
                System.out.println("Không tìm thấy lớp học có mã: " + maLopCanXoa);
            }
        } catch (SQLException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

	static List<LopHoc> DanhSach(Connection con) throws SQLException {
		String sql = "Select * from LopHoc";
		Statement state = con.createStatement();
		ResultSet rs = state.executeQuery(sql);
		List<LopHoc> ds = new ArrayList<>();
		while (rs.next()) {
			String maLop = rs.getString("MaLop");
			String tenLop = rs.getString("TenLop");
			String soPhong = rs.getString("SoPhong");
			LopHoc a = new LopHoc(maLop, tenLop, soPhong);
			ds.add(a);
		}
		return ds;
	}

	static void In() throws SQLException {
		try {
			Connection con = connectionDB();
			List<LopHoc> ds = DanhSach(con);
			System.out.println("DANH SACH LOP HOC");
			for (LopHoc x : ds) {
				System.out.printf("\t\n\n\t");
				System.out.printf("%s - %s tai %s \n", x.maLop, x.tenLop, x.phonghoc);
			}
			con.close();
		} catch (ClassNotFoundException e) {
			System.out.print("Error: " + e.getMessage());
		}
	}

	static void XuatraFile() throws SQLException, FileNotFoundException, UnsupportedEncodingException {
		try {
			Connection con = connectionDB();
			List<LopHoc> ds = DanhSach(con);
			PrintWriter pw = new PrintWriter("C:\\Users\\pc\\Downloads\\jdbc1\\SinhVien.txt", "UTF-8");
			System.out.println("DANH SACH LOP HOC");
			for (LopHoc x : ds) {
				pw.printf("%s - %s tai %s \n", x.maLop, x.tenLop, x.phonghoc);
			}
			pw.close();
			con.close();
		} catch (ClassNotFoundException e) {
			System.out.print("Error: " + e.getMessage());
		}
	}

	static void demLop(Connection con) throws SQLException {
		String sql = "Select COUNT(MaLop) AS SL FROM LopHoc";
		Statement statement = con.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		int dem = rs.getInt("SL");
		System.out.printf("\t\n\n\t");
		System.out.println("So luong lop: " + dem);
	}

	static void demSinhVien(Connection con) throws SQLException {
		String sql = "Select COUNT(MaSinhVien) AS SL FROM SinhVien";
		Statement statement = con.createStatement();
		ResultSet rs = statement.executeQuery(sql);
		int dem = rs.getInt("SL");
		System.out.printf("\t\n\n\t");
		System.out.println("So luong Sinh Vien: " + dem);
	}

}

class LopHoc {
	String maLop, tenLop, phonghoc;

	public LopHoc(String maLop, String tenLop, String phonghoc) {
		super();
		this.maLop = maLop;
		this.tenLop = tenLop;
		this.phonghoc = phonghoc;
	}

	public String getMaLop() {
		return maLop;
	}

	public void setMaLop(String maLop) {
		this.maLop = maLop;
	}

	public String getTenLop() {
		return tenLop;
	}

	public void setTenLop(String tenLop) {
		this.tenLop = tenLop;
	}

	public String getPhonghoc() {
		return phonghoc;
	}

	public void setPhonghoc(String phonghoc) {
		this.phonghoc = phonghoc;
	}

}