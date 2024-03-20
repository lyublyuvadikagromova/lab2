import java.io.*;
import java.util.ArrayList;
import java.util.List;

class Author {
    private String name;
    private int yearOfBirth;

    public Author(String name, int yearOfBirth) {
        this.name = name;
        this.yearOfBirth = yearOfBirth;
    }

    public String getName() {
        return name;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Year: " + yearOfBirth;
    }
}

class Book {
    private String title;
    private transient Author author;
    private int edition;

    public Book(String title, Author author, int edition) {
        this.title = title;
        this.author = author;
        this.edition = edition;
    }

    public String getTitle() {
        return title;
    }

    public Author getAuthor() {
        return author;
    }

    public int getEdition() {
        return edition;
    }

    @Override
    public String toString() {
        return "Book: " + title + ", Authors: " + author + ", Edition: " + edition;
    }
}

class BookReader {
    private String name;
    private transient int registrationNumber;
    private List<Book> borrowedBooks;

    public BookReader(String name, int registrationNumber) {
        this.name = name;
        this.registrationNumber = registrationNumber;
        this.borrowedBooks = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }

    @Override
    public String toString() {
        return "Name: " + name + ", Registration Number: " + registrationNumber + "\nBorrowed Books: " + borrowedBooks;
    }
}

class Bookshelf implements Serializable {
    private String name;
    private List<Book> books;

    public Bookshelf(String name) {
        this.name = name;
        this.books = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void addBook(Book book) {
        books.add(book);
    }

    public void removeBook(Book book) {
        books.remove(book);
    }

    @Override
    public String toString() {
        return "\n" + name + ": " + books.size() + " books\nBooks in " + name + ": " + books;
    }
}

class LibraryStorage implements Serializable {
    private List<Bookshelf> shelves;

    public LibraryStorage() {
        this.shelves = new ArrayList<>();
    }

    public List<Bookshelf> getShelves() {
        return shelves;
    }

    public void addShelf(Bookshelf shelf) {
        shelves.add(shelf);
    }

    public void removeShelf(Bookshelf shelf) {
        shelves.remove(shelf);
    }

    @Override
    public String toString() {
        return "Book Storages: " + shelves;
    }
}

class LibraryReport implements Serializable {
    private transient LibraryStorage storage;
    private transient List<BookReader> readers;

    public LibraryReport(LibraryStorage storage, List<BookReader> readers) {
        this.storage = storage;
        this.readers = readers;
    }

    public LibraryStorage getStorage() {
        return storage;
    }

    public List<BookReader> getReaders() {
        return readers;
    }

    @Override
    public String toString() {
        StringBuilder report = new StringBuilder("Library Report:\n");
        report.append(storage);
        report.append("\nReaders:\n");


        if (readers != null) {
            for (BookReader reader : readers) {
                report.append(reader);
                report.append("\n");
            }
            report.append("Total Readers in the Library: ").append(readers.size());
        } else {
            report.append("No readers in the library.");
        }

        return report.toString();
    }

}

public class LibraryDriver2 {
    public static void main(String[] args) {

        Author author1 = new Author("John Doe", 2022);
        Author author2 = new Author("Jane Smith", 2020);
        Author author3 = new Author("J.R.R. Tolkien", 1937);
        Author author4 = new Author("Robert Louis Stevenson", 1883);
        Author author5 = new Author("Arthur Conan Doyle", 1902);
        Author author6 = new Author("Gillian Flynn", 2012);
        Author author7 = new Author("J.K. Rowling", 1997);
        Author author8 = new Author("Patrick Rothfuss", 2007);


        Book book1 = new Book("Java Programming", author1, 1);
        Book book2 = new Book("Data Structures", author2, 2);
        Book book3 = new Book("The Hobbit", author3, 1);
        Book book4 = new Book("Treasure Island", author4, 1);
        Book book5 = new Book("The Hound of the Baskervilles", author5, 1);
        Book book6 = new Book("Gone Girl", author6, 1);
        Book book7 = new Book("Harry Potter and the Philosopher's Stone", author7, 1);
        Book book8 = new Book("The Name of the Wind", author8, 1);


        Bookshelf programmingBooks = new Bookshelf("Programming Books");
        programmingBooks.addBook(book1);
        programmingBooks.addBook(book2);
        Bookshelf adventureBooks = new Bookshelf("Adventure Books");
        adventureBooks.addBook(book3);
        adventureBooks.addBook(book4);

        Bookshelf mysteryNovels = new Bookshelf("Mystery Novels");
        mysteryNovels.addBook(book5);
        mysteryNovels.addBook(book6);

        Bookshelf fantasyLibrary = new Bookshelf("Fantasy Library");
        fantasyLibrary.addBook(book7);
        fantasyLibrary.addBook(book8);

        LibraryStorage libraryStorage = new LibraryStorage();
        libraryStorage.addShelf(programmingBooks);
        libraryStorage.addShelf(adventureBooks);
        libraryStorage.addShelf(mysteryNovels);
        libraryStorage.addShelf(fantasyLibrary);


        BookReader alice = new BookReader("Alice Johnson", 12345);
        alice.borrowBook(book1);


        List<BookReader> readers = new ArrayList<>();
        readers.add(alice);

        LibraryReport report = new LibraryReport(libraryStorage, readers);


        serializeLibraryReport(report);


        LibraryReport restoredReport = deserializeLibraryReport();
        if (restoredReport != null) {
            System.out.println("Original Report:");
            System.out.println(report);
            System.out.println("\nRestored Report:");
            System.out.println(restoredReport);
        } else {
            System.out.println("Failed to restore report!");
        }
    }


    private static void serializeLibraryReport(LibraryReport report) {
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("library_report.ser"))) {
            outputStream.writeObject(report);
            System.out.println("Library report serialized successfully.");
        } catch (IOException e) {
            System.out.println("Serialization failed: " + e.getMessage());
        }
    }


    private static LibraryReport deserializeLibraryReport() {
        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("library_report.ser"))) {
            return (LibraryReport) inputStream.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Deserialization failed: " + e.getMessage());
            return null;
        }
    }
}