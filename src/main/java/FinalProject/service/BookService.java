package FinalProject.service;

import FinalProject.Book;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class BookService {
    private static final String FILE_PATH = "books.txt";
    private static final String DELIMITER = "|#|";
    private final ObservableList<Book> bookList = FXCollections.observableArrayList();

    public void loadFromFile() {
        bookList.clear();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            loadDefaultBooks();
            saveToFile();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;

                String[] parts = line.split("\\|#\\|");
                if (parts.length >= 7) {
                    String id = parts[0];
                    String title = parts[1];
                    String author = parts[2];
                    String category = parts[3];
                    String accessType = parts[4];
                    String contentSummary = parts[5].replace("<BR>", "\n");
                    String imagePath = parts[6].equals("EMPTY") ? "" : parts[6];

                    bookList.add(new Book(id, title, author, category, accessType, contentSummary, imagePath));
                }
            }
        } catch (IOException e) {
            System.err.println("Kitabları oxuyarkən xəta: " + e.getMessage());
        }
    }

    public synchronized void saveToFile() {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(FILE_PATH), StandardCharsets.UTF_8))) {
            for (Book b : bookList) {
                String safeSummary = (b.getContentSummary() != null)
                        ? b.getContentSummary().replace("\n", "<BR>").replace("\r", "")
                        : "";
                String safeImagePath = (b.getImagePath() != null && !b.getImagePath().isBlank())
                        ? b.getImagePath()
                        : "EMPTY";

                String line = b.getId() + DELIMITER +
                        b.getTitle() + DELIMITER +
                        b.getAuthor() + DELIMITER +
                        b.getCategory() + DELIMITER +
                        b.getAccessType() + DELIMITER +
                        safeSummary + DELIMITER +
                        safeImagePath;

                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Kitabları yazarkən xəta: " + e.getMessage());
        }
    }

    public void addBook(Book book) {
        bookList.add(book);
        saveToFile();
    }

    public void deleteBook(Book book) {
        bookList.remove(book);
        saveToFile();
    }

    public void updateBook(Book oldBook, Book updatedBook) {
        int index = bookList.indexOf(oldBook);
        if (index != -1) {
            bookList.set(index, updatedBook);
            saveToFile();
        }
    }

    public ObservableList<Book> getBookList() {
        return bookList;
    }

    private void loadDefaultBooks() {
        bookList.add(new Book(
                "1",
                "Atomik Vərdişlər",
                "James Clear",
                "Şəxsi İnkişaf",
                "FREE",
                "Vərdişlərin formalaşması 4 pillədən ibarətdir: İşarə, İstək, Reaksiya və Mükafat.\n\nGündə 1% irəliləyiş ilin sonunda böyük fərq yaradır.",
                "https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=400&q=80"
        ));

        bookList.add(new Book(
                "2",
                "Həyatın Mənasını Axtaran İnsan",
                "Viktor Frankl",
                "Psixoloji",
                "PREMIUM",
                "Loqoterapiyanın əsasları və insanın ən ağır şəraitdə belə daxili məqsəd və məna tapmaq qabiliyyəti haqqında psixoloji təhlil.",
                "https://images.unsplash.com/photo-1512820790803-83ca734da794?w=400&q=80"
        ));

        bookList.add(new Book(
                "3",
                "Rəqəmlərin Sirri və Tale",
                "Numerologiya Tədqiqatları",
                "Numeroloji",
                "FREE",
                "Doğum tarixi və adın hərfləri üzərindən xarakter analizi, şəxsi dövrlər və tale matrisinin hesablanması qaydaları.",
                "https://images.unsplash.com/photo-1509228468518-180dd4864904?w=400&q=80"
        ));
    }
}