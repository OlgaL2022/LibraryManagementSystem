package repository;

import model.Book;
import model.Status;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.sql.ResultSet.CONCUR_READ_ONLY;
import static java.sql.ResultSet.TYPE_SCROLL_SENSITIVE;

public class LibraryRepository {

    Connection connection = new DatabaseManager().getConnection();

    public void addBookToDB(Book book) throws SQLException {
        String query = "INSERT INTO books (book_id, title, author, genre, publishing_year, quantity, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, book.getId());
        preparedStatement.setString(2, book.getTitle());
        preparedStatement.setString(3, book.getAuthor());
        preparedStatement.setString(4, book.getGenre().toString());
        preparedStatement.setInt(5, book.getPublishYear());
        preparedStatement.setInt(6, book.getQuantity());
        preparedStatement.setString(7, book.getStatus().toString());
        preparedStatement.execute();
    }

    public int getBorrowCountByBookId(int bookId) throws SQLException {
        String query = "SELECT COUNT(*) FROM issued_books WHERE book_id=?";
        PreparedStatement preparedStatement = connection.prepareStatement(query, TYPE_SCROLL_SENSITIVE, CONCUR_READ_ONLY);
        preparedStatement.setInt(1, bookId);
        ResultSet rs = preparedStatement.executeQuery();
        rs.last();
        return rs.getRow();
    }

    public User addReaderToDB(User user) throws SQLException {
        String query = "INSERT INTO readers (user_id, user_name, user_password) VALUES (?,?,?)";

        PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        statement.setInt(1, user.getId());
        statement.setString(2, user.getUserName());
        statement.setString(3, user.getPassword());
        int affectedRows = statement.executeUpdate();

        if (affectedRows == 0) {
            System.out.println("User creation failed.");
            return null;
        }

        try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
                return user;
            }
            else {
                System.out.println("Couldn't obtain user ID. User creation failed.");
                return null;
            }
        }
    }

    public ArrayList<User> getAllReadersFromDB() throws SQLException{
        ArrayList<User> readerList = new ArrayList<>();
        String query = "SELECT * FROM readers";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            readerList.add(
                    new User(
                    resultSet.getInt("user_id"),
                    resultSet.getString("user_name"),
                    resultSet.getString("user_password")
            ));
        }
        return readerList;
    }

    public ArrayList<Book> getAllBooksFromDB() throws SQLException{
        ArrayList<Book> bookList = new ArrayList<>();
        String query = "SELECT * FROM books";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()){
            bookList.add(this.createBookFromResultSet(resultSet));
        }
        return bookList;
    }

    private Book createBookFromResultSet(ResultSet resultSet) throws SQLException {
        return new Book(
                resultSet.getInt("book_id"),
                resultSet.getString("title"),
                resultSet.getString("author"),
                resultSet.getString("genre"),
                resultSet.getInt("publishing_year"),
                resultSet.getInt("quantity"),
                Status.valueOf(resultSet.getString("status"))
        );
    }

    public Book getSingleBookFromDB(int bookId) throws Exception {
        String query = "SELECT * FROM books WHERE book_id = " + bookId;
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        if(resultSet.next()){
            return this.createBookFromResultSet(resultSet);
        }
        throw new Exception("Book not found");
    }

    public Book getBookByTitle(String title) throws Exception {
        String query = "SELECT * FROM books WHERE title ='" + title + "'";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            return this.createBookFromResultSet(resultSet);
        }
        throw new Exception("Book with this title not found");
    }

    public void deleteBook(int bookId) throws SQLException {
        String query = "DELETE FROM books WHERE book_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, bookId);
        preparedStatement.execute();
    }

    public void updateTitle(int bookId, String newTitle) throws SQLException{
        String query = "UPDATE books SET title = ? WHERE book_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, newTitle);
        preparedStatement.setInt(2, bookId);
        preparedStatement.execute();
    }

    public void updateAuthor(int bookId, String newAuthor) throws SQLException{
        String query = "UPDATE books SET author = ? WHERE book_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, newAuthor);
        preparedStatement.setInt(2, bookId);
        preparedStatement.execute();
    }

    public void updateYear(int bookId, int newYear) throws SQLException{
        String query = "UPDATE books SET year = ? WHERE book_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, newYear);
        preparedStatement.setInt(2, bookId);
        preparedStatement.execute();
    }

    public void updateQuantity(int bookId, int newQuantity) throws SQLException{
        String query = "UPDATE books SET quantity = ? WHERE book_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, newQuantity);
        preparedStatement.setInt(2, bookId);
        preparedStatement.execute();
    }

    public void updateGenre(int bookId, String newGenre) throws SQLException{
        String query = "UPDATE books SET genre = ? WHERE book_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, newGenre);
        preparedStatement.setInt(2, bookId);
        preparedStatement.execute();
    }

    public void updateStatus(int bookId, Status newStatus) throws SQLException{
        String query = "UPDATE books SET status = ? WHERE book_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, newStatus.toString());
        preparedStatement.setInt(2, bookId);
        preparedStatement.execute();
    }

    public boolean borrowBook(int bookId, int userId) throws SQLException {
        String query = "INSERT INTO issued_books (book_id, user_id, issued, return_due) VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 1 MONTH))";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, bookId);
        preparedStatement.setInt(2, userId);

        return preparedStatement.executeUpdate() == 1;
    }

    public boolean returnBookDB(int bookId, int userId) throws SQLException {
        String query = "UPDATE issued_books SET returned_at = NOW() WHERE book_id = ? AND user_id = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, bookId);
        preparedStatement.setInt(2, userId);

        return preparedStatement.executeUpdate() == 1;
    }

    public List<Book> findBorrowedBooksByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM books WHERE book_id IN (SELECT book_id FROM issued_books WHERE user_id = ? AND returned_at IS NULL)";
        PreparedStatement preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, userId);
        ResultSet rs = preparedStatement.executeQuery();
        List<Book> borrowedBooks = new ArrayList<>();
        while (rs.next()) {
            borrowedBooks.add(mapResultSetToBook(rs));
        }
        return borrowedBooks;
    }

    private Book mapResultSetToBook(ResultSet rs) throws SQLException {
        return new Book(
                rs.getInt(1),
                rs.getString(2),
                rs.getString(3),
                rs.getString(4),
                rs.getInt(5),
                rs.getInt(6),
                Status.valueOf(rs.getString(7))
        );
    }

    public void closeOpenConnections() {
        try {
            if (connection != null) connection.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}


