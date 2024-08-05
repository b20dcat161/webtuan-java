package com.example.demo8.DAO;

import com.example.demo8.model.*;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AppUserDAOImpl {

    private Connection getConnection() throws SQLException {
        String url = "jdbc:mysql://db:3306/student";
        String username = "tuan";
        String password = "password";
        return DriverManager.getConnection(url, username, password);
    }
    public void saveUser2(AppUser1 user) {
        String sql = "INSERT INTO user (username, email, sdt, name, role, password) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPhone());
            pstmt.setString(4, user.getFullname());
            pstmt.setString(5, user.getRole());
            pstmt.setString(6, user.getPassword());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public Challenge getChallengeById(int id) {
        Challenge challenge = null;
        String sql = "SELECT * FROM challenges WHERE id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                challenge = new Challenge(
                        rs.getInt("id"),
                        rs.getString("file_name"),
                        rs.getString("hint")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return challenge;
    }

    public List<Challenge> getAllChallenges() {
        List<Challenge> challenges = new ArrayList<>();
        String sql = "SELECT * FROM challenges";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Challenge challenge = new Challenge(
                        rs.getInt("id"),
                        rs.getString("file_name"),
                        rs.getString("hint")
                );
                challenges.add(challenge);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return challenges;
    }
    public void saveChallenge(String fileName, String hint) {
        String sql = "INSERT INTO challenges (file_name, hint) VALUES (?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fileName);
            pstmt.setString(2, hint);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Challenge getChallengeByHint(String hint) {
        Challenge challenge = null;
        String sql = "SELECT * FROM challenges WHERE hint = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hint);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                challenge = new Challenge(
                        rs.getInt("id"),
                        rs.getString("file_name"),
                        rs.getString("hint")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return challenge;
    }
    public List<AppUser2> getAllUsers1() {
        List<AppUser2> users = new ArrayList<>();
        String query = "SELECT * FROM user";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                AppUser2 user = new AppUser2();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));
                user.setRole(rs.getInt("role"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setSdt(rs.getString("sdt"));
                users.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }
    public List<SubmissionFile> getSubmissionsByStudentId(int studentId) {
        List<SubmissionFile> submissions = new ArrayList<>();
        String sql = "SELECT * FROM submission_files WHERE user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Thiết lập giá trị cho tham số
            pstmt.setInt(1, studentId);

            try (ResultSet resultSet = pstmt.executeQuery()) {
                while (resultSet.next()) {
                    SubmissionFile submissionFile = new SubmissionFile(
                            resultSet.getInt("id"),
                            resultSet.getInt("user_id"),
                            resultSet.getInt("homework_id"),
                            resultSet.getString("file_name"),
                            resultSet.getString("file_path")
                    );
                    submissions.add(submissionFile);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return submissions;
    }

    public void saveUsers2(List<AppUser2> users) {
        String sql = "INSERT INTO user (id, role, name, username, password, email, sdt) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            for (AppUser2 user : users) {
                statement.setInt(1, user.getId());
                statement.setInt(2, user.getRole());
                statement.setString(3, user.getName());
                statement.setString(4, user.getUsername());
                statement.setString(5, user.getPassword());
                statement.setString(6, user.getEmail());
                statement.setString(7, user.getSdt());
                statement.addBatch();
            }

            statement.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveHomeworkFile(HomeworkFile homeworkFile) {
        String sql = "INSERT INTO homework_files (user_id, file_name, file_path) VALUES (?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, homeworkFile.getUserId());
            pstmt.setString(2, homeworkFile.getFileName());
            pstmt.setString(3, homeworkFile.getFilePath());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<HomeworkFile> getAllHomeworkFiles() {
        List<HomeworkFile> files = new ArrayList<>();
        String sql = "SELECT * FROM homework_files";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                HomeworkFile file = new HomeworkFile(rs.getInt("id"), rs.getInt("user_id"), rs.getString("file_name"), rs.getString("file_path"));
                files.add(file);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return files;
    }


    public HomeworkFile getHomeworkFileById(int id) {
        HomeworkFile file = null;
        String sql = "SELECT * FROM homework_files WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    file = new HomeworkFile(rs.getInt("id"), rs.getInt("user_id"), rs.getString("file_name"), rs.getString("file_path"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return file;
    }


    public void deleteHomeworkFile(int id) {
        String sql = "DELETE FROM homework_files WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveSubmissionFile(SubmissionFile submissionFile) {
        String sql = "INSERT INTO submission_files (user_id, homework_id, file_name, file_path) VALUES (?, ?, ?, ?)";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, submissionFile.getUserId());
            pstmt.setInt(2, submissionFile.getHomeworkId());
            pstmt.setString(3, submissionFile.getFileName());
            pstmt.setString(4, submissionFile.getFilePath());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public List<SubmissionFile> getAllSubmissionFiles() {
        List<SubmissionFile> files = new ArrayList<>();
        String sql = "SELECT * FROM submission_files";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                SubmissionFile file = new SubmissionFile(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("homework_id"), rs.getString("file_name"), rs.getString("file_path"));
                files.add(file);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return files;
    }


    public SubmissionFile getSubmissionFileById(int id) {
        SubmissionFile file = null;
        String sql = "SELECT * FROM submission_files WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    file = new SubmissionFile(rs.getInt("id"), rs.getInt("user_id"), rs.getInt("homework_id"), rs.getString("file_name"), rs.getString("file_path"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return file;
    }

    public void deleteSubmissionFile(int id) {
        String sql = "DELETE FROM submission_files WHERE id = ?";
        try (Connection conn = getConnection(); PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }}

        public int getUserIdByUsername (String username){
            int userId = -1; // Default value if user not found
            String query = "SELECT id FROM user WHERE username = ?";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, username);

                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        userId = rs.getInt("id");
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return userId;
        }
        public void sendMessage ( int senderId, int receiverId, String message){
            String query = "INSERT INTO messages (sender_id, receiver_id, message) VALUES (?, ?, ?)";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, senderId);
                pstmt.setInt(2, receiverId);
                pstmt.setString(3, message);
                pstmt.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        public List<Message> getMessagesBetween ( int userId, int otherUserId){
            List<Message> messages = new ArrayList<>();
            String query = "SELECT * FROM messages WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?)";

            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, userId);
                pstmt.setInt(2, otherUserId);
                pstmt.setInt(3, otherUserId);
                pstmt.setInt(4, userId);

                try (ResultSet rs = pstmt.executeQuery()) {
                    while (rs.next()) {
                        Message message = new Message();
                        message.setId(rs.getInt("id"));
                        message.setSenderId(rs.getInt("sender_id"));
                        message.setReceiverId(rs.getInt("receiver_id"));
                        message.setMessage(rs.getString("message"));
                        message.setSentAt(rs.getTimestamp("sent_at"));
                        messages.add(message);
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return messages;
        }


        public void updateMessage ( int messageId, String newContent){
            String query = "UPDATE messages SET message = ? WHERE id = ?";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, newContent);
                pstmt.setInt(2, messageId);
                pstmt.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


        public void deleteMessage ( int messageId){
            String query = "DELETE FROM messages WHERE id = ?";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, messageId);
                pstmt.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        // User-related methods
        public List<AppUser> getAllUsers () {
            List<AppUser> users = new ArrayList<>();
            String query = "SELECT * FROM user";
            try (Connection conn = getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(query)) {

                while (rs.next()) {
                    AppUser user = new AppUser();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));
                    user.setRole(rs.getString("role"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));
                    user.setEmail(rs.getString("email"));
                    user.setSdt(rs.getString("sdt"));
                    users.add(user);
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return users;
        }

        public AppUser getUserById ( int id){
            AppUser user = null;
            String query = "SELECT * FROM user WHERE id = ?";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setInt(1, id);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        user = new AppUser();
                        user.setId(rs.getInt("id"));
                        user.setName(rs.getString("name"));
                        user.setRole(rs.getString("role"));
                        user.setUsername(rs.getString("username"));
                        user.setPassword(rs.getString("password"));
                        user.setEmail(rs.getString("email"));
                        user.setSdt(rs.getString("sdt"));
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return user;
        }

        public AppUser getUserByUsername (String username){
            AppUser user = null;
            String query = "SELECT * FROM user WHERE username = ?";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, username);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        user = new AppUser();
                        user.setId(rs.getInt("id"));
                        user.setName(rs.getString("name"));
                        user.setRole(rs.getString("role"));
                        user.setUsername(rs.getString("username"));
                        user.setPassword(rs.getString("password"));
                        user.setEmail(rs.getString("email"));
                        user.setSdt(rs.getString("sdt"));
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return user;
        }

        public void updateUser (AppUser user){
            String query = "UPDATE user SET name = ?, role = ?, username = ?, password = ?, email = ?, sdt = ? WHERE id = ?";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, user.getName());
                pstmt.setString(2, user.getRole());
                pstmt.setString(3, user.getUsername());
                pstmt.setString(4, user.getPassword());
                pstmt.setString(5, user.getEmail());
                pstmt.setString(6, user.getSdt());
                pstmt.setInt(7, user.getId());
                pstmt.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public void deleteUser ( int id){
            String deleteHomeworkFilesQuery = "DELETE FROM homework_files WHERE user_id = ?";
            String deleteSubmissionsQuery = "DELETE FROM submission_files WHERE user_id = ?";
            String deleteUserQuery = "DELETE FROM user WHERE id = ?";

            try (Connection conn = getConnection();
                 PreparedStatement deleteHomeworkFilesStmt = conn.prepareStatement(deleteHomeworkFilesQuery);
                 PreparedStatement deleteSubmissionsStmt = conn.prepareStatement(deleteSubmissionsQuery);
                 PreparedStatement deleteUserStmt = conn.prepareStatement(deleteUserQuery)) {


                conn.setAutoCommit(false);


                deleteHomeworkFilesStmt.setInt(1, id);
                deleteHomeworkFilesStmt.executeUpdate();

                deleteSubmissionsStmt.setInt(1, id);
                deleteSubmissionsStmt.executeUpdate();


                deleteUserStmt.setInt(1, id);
                deleteUserStmt.executeUpdate();


                conn.commit();

            } catch (SQLException e) {
                e.printStackTrace();

            }
        }
        public void saveUser1 (AppUser1 user){
            String query = "INSERT INTO user (name, role, username, password, email, sdt) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, user.getFullname());
                pstmt.setString(2, user.getRole());
                pstmt.setString(3, user.getUsername());
                pstmt.setString(4, user.getPassword());
                pstmt.setString(5, user.getEmail());
                pstmt.setString(6, user.getPhone());

                pstmt.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        public void saveUser (AppUser user){
            String query = "INSERT INTO user (name, role, username, password, email, sdt) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, user.getName());
                pstmt.setString(2, user.getRole());
                pstmt.setString(3, user.getUsername());
                pstmt.setString(4, user.getPassword());
                pstmt.setString(5, user.getEmail());
                pstmt.setString(6, user.getSdt());
                pstmt.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public AppUser authenticateUser (String username, String password){
            AppUser user = null;
            String query = "SELECT * FROM user WHERE username = ? AND password = ?";
            try (Connection conn = getConnection();
                 PreparedStatement pstmt = conn.prepareStatement(query)) {

                pstmt.setString(1, username);
                pstmt.setString(2, password);
                try (ResultSet rs = pstmt.executeQuery()) {
                    if (rs.next()) {
                        user = new AppUser();
                        user.setId(rs.getInt("id"));
                        user.setName(rs.getString("name"));
                        user.setRole(rs.getString("role"));
                        user.setUsername(rs.getString("username"));
                        user.setPassword(rs.getString("password"));
                        // Thêm các trường khác nếu cần
                    }
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return user;
        }



        public void sendMessage (Message message){
            String sql = "INSERT INTO messages (sender_id, receiver_id, message, sent_at) VALUES (?, ?, ?, ?)";
            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, message.getSenderId());
                statement.setInt(2, message.getReceiverId());
                statement.setString(3, message.getMessage());
                statement.setTimestamp(4, new Timestamp(message.getSentAt().getTime()));
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace(); // Handle exception
            }
        }

        public List<Message> getMessages ( int userId1, int userId2){
            List<Message> messages = new ArrayList<>();
            String sql = "SELECT * FROM messages WHERE (sender_id = ? AND receiver_id = ?) OR (sender_id = ? AND receiver_id = ?) ORDER BY sent_at";
            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, userId1);
                statement.setInt(2, userId2);
                statement.setInt(3, userId2);
                statement.setInt(4, userId1);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    messages.add(new Message(
                            resultSet.getInt("id"),
                            resultSet.getInt("sender_id"),
                            resultSet.getInt("receiver_id"),
                            resultSet.getString("message"),
                            resultSet.getTimestamp("sent_at")
                    ));
                }
            } catch (SQLException e) {
                e.printStackTrace(); // Handle exception
            }
            return messages;
        }

        public void updateMessage (Message message){
            String sql = "UPDATE messages SET message = ?, sent_at = ? WHERE id = ? AND sender_id = ?";
            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, message.getMessage());
                statement.setTimestamp(2, new Timestamp(message.getSentAt().getTime()));
                statement.setInt(3, message.getId());
                statement.setInt(4, message.getSenderId());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace(); // Handle exception
            }
        }

        public void deleteMessage ( int id, int senderId){
            String sql = "DELETE FROM messages WHERE id = ? AND sender_id = ?";
            try (Connection connection = getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                statement.setInt(2, senderId);
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace(); // Handle exception
            }
        }
    }
