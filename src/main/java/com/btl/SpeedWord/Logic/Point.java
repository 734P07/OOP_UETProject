package com.btl.SpeedWord.Logic;

import com.btl.Database;
import com.btl.getAccountData;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;

public class Point {
    private static Point instance;

    private Map<Timestamp, Integer> pointMap;

    public static Point getInstance() {
        if (instance == null) {
            instance = new Point();
        }
        return instance;
    }

    public void insertData(int gamePoints) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            // Kết nối đến cơ sở dữ liệu
            connection = Database.connectDb();

            // Truy vấn để chèn dữ liệu vào bảng
            String insertQuery = "INSERT INTO game_points (gamePoints, username, time) VALUES (?, ?, ?)";

            // Tạo PreparedStatement
            preparedStatement = connection.prepareStatement(insertQuery);

            // Thiết lập giá trị cho các tham số
            preparedStatement.setInt(1, gamePoints);
            preparedStatement.setString(2, getAccountData.username);
            preparedStatement.setTimestamp(3, new Timestamp(System.currentTimeMillis()));

            // Thực thi truy vấn
            preparedStatement.executeUpdate();

            // Xóa điểm cũ nếu quá giới hạn
            checkAndDeleteExcessRows(connection);

            System.out.println("Dữ liệu được chèn thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng PreparedStatement và Connection
            try {
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void getDataByUsername() {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            // Kết nối đến cơ sở dữ liệu
            connection = Database.connectDb();

            // Truy vấn để lấy dữ liệu từ bảng với điều kiện username
            String selectQuery = "SELECT * FROM ten_bang WHERE username = ?";

            // Tạo PreparedStatement
            preparedStatement = connection.prepareStatement(selectQuery);

            // Thiết lập giá trị cho tham số username
            preparedStatement.setString(1, getAccountData.username);

            // Thực thi truy vấn và nhận kết quả
            resultSet = preparedStatement.executeQuery();

            // Xử lý kết quả trả về
            while (resultSet.next()) {
                int gamePoints = resultSet.getInt("gamePoints");
                String retrievedUsername = resultSet.getString("username");
                Timestamp time = resultSet.getTimestamp("time");

                // Xử lý dữ liệu lấy được tại đây
                pointMap = new HashMap<>();
                pointMap.put(time, gamePoints);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Đóng ResultSet, PreparedStatement và Connection
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void checkAndDeleteExcessRows(Connection connection) throws SQLException {
        // Truy vấn để đếm số lượng hàng
        String countQuery = "SELECT COUNT(*) AS row_count FROM game_points";
        PreparedStatement countStatement = connection.prepareStatement(countQuery);
        ResultSet resultSet = countStatement.executeQuery();

        int rowCount = 0;
        if (resultSet.next()) {
            rowCount = resultSet.getInt("row_count");
        }

        // Nếu số lượng hàng vượt quá MAX_ROWS, xóa hàng có thời gian cũ nhất
        if (rowCount >= 10) {
            String deleteQuery = "DELETE FROM game_points ORDER BY time ASC LIMIT ?";
            PreparedStatement deleteStatement = connection.prepareStatement(deleteQuery);
            deleteStatement.setInt(1, rowCount - 10);
            deleteStatement.executeUpdate();
        }
    }

}
