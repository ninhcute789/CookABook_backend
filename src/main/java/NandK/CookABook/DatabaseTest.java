package NandK.CookABook;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class DatabaseTest {
    public static void main(String[] args) {
        try {
            // Đặt System.out sử dụng UTF-8 encoding
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8.name()));

            // Kết nối tới cơ sở dữ liệu
            Connection conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/cookabookdb", "root", "ninhcute789");

            // Thực hiện truy vấn đơn giản
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT 1");

            // Kiểm tra kết quả
            if (rs.next()) {
                System.out.println("Kết nối cơ sở dữ liệu thành công!");
            }

            // Đóng kết nối
            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}