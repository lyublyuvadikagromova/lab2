import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

abstract class Human implements Serializable {
    private String firstName;
    private String lastName;

    public Human(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Name: " + firstName + " " + lastName;
    }
}

class Author extends Human {
    public Author(String firstName, String lastName) {
        super(firstName, lastName);
    }
}

class Book implements Serializable {
    private String title;
    private List<Author> authors;
    private int publicationYear;
    private int editionNumber;
    private String storageName;

    public Book(String title, List<Author> authors, int publicationYear, int editionNumber) {
        this.title = title;
        this.authors = authors;
        this.publicationYear = publicationYear;
        this.editionNumber = editionNumber;
    }

    public Book(String title, List<Author> authors, int publicationYear, int editionNumber, String storageName) {
        this(title, authors, publicationYear, editionNumber);
        this.storageName = storageName;
    }

    @Override
    public String toString() {
        StringBuilder authorsString = new StringBuilder();
        for (Author author : authors) {
            authorsString.append(author).append(", ");
        }
        authorsString.delete(authorsString.length() - 2, authorsString.length());

        String storageInfo = (storageName != null) ? ", Storage: " + storageName : "";

        return "Book: " + title + ", Authors: " + authorsString + ", Year: " + publicationYear +
                ", Edition: " + editionNumber + storageInfo;
    }

    public String getStorageName() {
        return storageName;
    }
}


class BookStore implements Serializable {
    private static final long serialVersionUID = 987654321L;
    private String storeName;
    private List<Book> books;

    public BookStore(String storeName) {
        this.storeName = storeName;
        this.books = new ArrayList<>();
    }

    public String getStoreName() {
        return storeName;
    }

    public List<Book> getBooks() {
        return books;
    }

    @Override
    public String toString() {
        return "Store: " + storeName + "\nBooks: " + books;
    }
}

class BookReader extends Human implements Serializable {
    private int registrationNumber;
    private List<Book> borrowedBooks;

    public BookReader(String firstName, String lastName, int registrationNumber) {
        super(firstName, lastName);
        this.registrationNumber = registrationNumber;
        this.borrowedBooks = new ArrayList<>();
    }

    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }

    @Override
    public String toString() {
        return super.toString() + ", Registration Number: " + registrationNumber + "\nBorrowed Books: " + borrowedBooks;
    }
}

class Library implements Serializable {
    private String libraryName;
    private List<BookStore> bookStores;
    private List<BookReader> readers;

    public Library(String libraryName) {
        this.libraryName = libraryName;
        this.bookStores = new ArrayList<>();
        this.readers = new ArrayList<>();
    }

    public List<BookStore> getBookStores() {
        return bookStores;
    }

    public List<BookReader> getReaders() {
        return readers;
    }

    @Override
    public String toString() {
        return "Library: " + libraryName + "\nBook Stores: " + bookStores + "\nReaders: " + readers;
    }
}

public class LibraryDriver {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Author author1 = new Author("John", "Doe");
        Author author2 = new Author("Jane", "Smith");

        Book book1 = new Book("Java Programming", List.of(author1), 2022, 1);
        Book book2 = new Book("Data Structures", List.of(author2), 2020, 2);

        BookStore bookStore = new BookStore("Programming Books");
        BookStore adventureBooks = new BookStore("Adventure Books");
        BookStore mysteryNovels = new BookStore("Mystery Novels");
        BookStore fantasyLibrary = new BookStore("Fantasy Library");
        BookStore scienceFictionCollection = new BookStore("Science Fiction Collection");
        bookStore.getBooks().add(book1);
        bookStore.getBooks().add(book2);
        adventureBooks.getBooks().add(new Book("The Hobbit", List.of(new Author("J.R.R.", "Tolkien")), 1937, 1));
        adventureBooks.getBooks().add(new Book("Treasure Island", List.of(new Author("Robert Louis", "Stevenson")), 1883, 1));

        mysteryNovels.getBooks().add(new Book("The Hound of the Baskervilles", List.of(new Author("Arthur Conan", "Doyle")), 1902, 1));
        mysteryNovels.getBooks().add(new Book("Gone Girl", List.of(new Author("Gillian", "Flynn")), 2012, 1));

        fantasyLibrary.getBooks().add(new Book("Harry Potter and the Philosopher's Stone", List.of(new Author("J.K.", "Rowling")), 1997, 1));
        fantasyLibrary.getBooks().add(new Book("The Name of the Wind", List.of(new Author("Patrick", "Rothfuss")), 2007, 1));

        scienceFictionCollection.getBooks().add(new Book("Dune", List.of(new Author("Frank", "Herbert")), 1965, 1));
        scienceFictionCollection.getBooks().add(new Book("Neuromancer", List.of(new Author("William", "Gibson")), 1984, 1));
        BookReader reader1 = new BookReader("Alice", "Johnson", 12345);
        reader1.borrowBook(book1);

        Library library = new Library("City Library");
        library.getBookStores().add(bookStore);
        library.getReaders().add(reader1);
        library.getBookStores().add(adventureBooks);
        library.getBookStores().add(mysteryNovels);
        library.getBookStores().add(fantasyLibrary);
        library.getBookStores().add(scienceFictionCollection);

        int choice;
        do {
            System.out.println("\nMenu:");
            System.out.println("1) Add a reader");
            System.out.println("2) Library Report");
            System.out.println("3) Serialize Library");
            System.out.println("4) Deserialize Library");
            System.out.println("5) Add Book or Storage");
            System.out.println("6) Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addReader(library);
                    break;
                case 2:
                    displayLibraryReport(library);
                    showTotalReaders(library);
                    break;
                case 3:
                    serializeLibrary(library, "library.ser");
                    break;
                case 4:
                    library = deserializeLibrary("library.ser");
                    if (library != null) {
                        System.out.println("Library deserialized successfully.");
                    }
                    break;
                case 5:
                    addBookOrStorage(library);
                    break;
                case 6:
                    System.out.println("Exiting the program.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (choice != 6);
    }

    private static void showTotalReaders(Library library) {
        int totalReaders = library.getReaders().size();
        System.out.println("Total Readers in the Library: " + totalReaders);
    }

    private static void addBookOrStorage(Library library) {
        System.out.println("Choose what to add:");
        System.out.println("1) Book");
        System.out.println("2) Storage");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                addBook(library);
                break;
            case 2:
                addStorage(library);
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private static void addStorage(Library library) {
        System.out.print("Enter the name of the new Book Storage: ");
        String storageName = scanner.nextLine();


        for (BookStore existingStore : library.getBookStores()) {
            if (existingStore.getStoreName().equals(storageName)) {
                System.out.println("Error: Storage with this name already exists.");
                return;
            }
        }


        BookStore newStorage = new BookStore(storageName);
        library.getBookStores().add(newStorage);
        System.out.println("Storage added successfully: " + storageName);
    }

    private static void addBook(Library library) {
        System.out.println("Enter book details:");
        System.out.print("Title: ");
        String title = scanner.nextLine();
        System.out.print("Author's First Name: ");
        String authorFirstName = scanner.nextLine();
        System.out.print("Author's Last Name: ");
        String authorLastName = scanner.nextLine();
        System.out.print("Publication Year: ");
        int publicationYear = scanner.nextInt();
        System.out.print("Edition Number: ");
        int editionNumber = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Enter the name of the Book Storage: ");
        String storageName = scanner.nextLine();


        boolean storageExists = false;
        for (BookStore bookStore : library.getBookStores()) {
            if (bookStore.getStoreName().equals(storageName)) {
                storageExists = true;
                break;
            }
        }

        if (!storageExists) {
            System.out.println("Error: The specified storage does not exist.");
            return;
        }


        for (BookStore bookStore : library.getBookStores()) {
            if (bookStore.getStoreName().equals(storageName)) {
                Author author = new Author(authorFirstName, authorLastName);
                Book book = new Book(title, List.of(author), publicationYear, editionNumber, storageName);
                bookStore.getBooks().add(book);
                System.out.println("Book added successfully to storage: " + storageName);
                return;
            }
        }
    }


    private static void addReader(Library library) {
        System.out.println("Enter reader details:");
        System.out.print("First Name: ");
        String firstName = scanner.nextLine();
        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();
        System.out.print("Registration Number: ");
        int registrationNumber = scanner.nextInt();

        BookReader reader = new BookReader(firstName, lastName, registrationNumber);


        library.getReaders().add(reader);

        System.out.println("Reader added successfully.");
    }

    private static void displayLibraryReport(Library library) {
        int subChoice;
        do {
            System.out.println("\nLibrary Report:");
            System.out.println("1) Display Book Storages");
            System.out.println("2) Display Readers");
            System.out.println("3) Display Total Readers");
            System.out.println("4) Back to Main Menu");
            System.out.print("Enter your choice: ");
            subChoice = scanner.nextInt();

            switch (subChoice) {
                case 1:
                    displayBookStorages(library);
                    break;
                case 2:
                    displayReaders(library);
                    break;
                case 3:
                    showTotalReaders(library);
                    break;
                case 4:
                    System.out.println("Returning to the main menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        } while (subChoice != 4);
    }

    private static void displayBookStorages(Library library) {
        List<BookStore> bookStorages = library.getBookStores();
        System.out.println("Book Storages:");

        for (int i = 0; i < bookStorages.size(); i++) {
            System.out.println((i + 1) + ") " + bookStorages.get(i).getStoreName());
        }

        System.out.print("Enter the number of the BookStorage to display books (0 to go back): ");
        int storageChoice = scanner.nextInt();

        if (storageChoice > 0 && storageChoice <= bookStorages.size()) {
            displayBooksInStorage(bookStorages.get(storageChoice - 1));
        }
    }

    private static void displayBooksInStorage(BookStore bookStorage) {
        System.out.println("Books in " + bookStorage.getStoreName() + ":");

        List<Book> books = bookStorage.getBooks();
        for (Book book : books) {
            System.out.println("    - " + book);
        }
        System.out.println();
    }

    private static void displayReaders(Library library) {
        List<BookReader> readers = library.getReaders();
        System.out.println("Readers:");
        for (BookReader reader : readers) {
            System.out.println(reader);
        }
    }

    private static void serializeLibrary(Library library, String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(library);
            System.out.println("Library serialized successfully.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static Library deserializeLibrary(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (Library) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}




