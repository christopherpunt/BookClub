import book.BookDao;

public class BookClubServer {
    public static void main(String[] args){ new BookClubServer(args); }

    private BookClubServer(String[] args) {
        BookDao bookDao = new BookDao();

//        bookDao.addBook(new Book(9, 123, "Harry Potter 8", 12345, "a nice harry potter book", null, null));

        System.out.println("Books:");

        for (var book : bookDao.getBooks()) {
            System.out.println(book);
        }
    }
}
