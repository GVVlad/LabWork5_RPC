import java.io.FileOutputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class FileClient {
    public static void main(String[] args) {
        try {
            System.out.println("Підключення до сервера...");
            Registry registry = LocateRegistry.getRegistry("localhost", 1099);
            FileService fileService = (FileService) registry.lookup("FileService");
            System.out.println("Клієнт успішно підключився до сервера.");

            String fileName = fileService.getFileName();
            System.out.println("Отримано назву файлу: " + fileName);

            long fileSize = fileService.getFileSize(fileName);
            System.out.println("Розмір файлу: " + fileSize + " байтів");

            try (FileOutputStream fos = new FileOutputStream(fileName)) {
                int chunkSize = 2048;
                for (int offset = 0; offset < fileSize; offset += chunkSize) {
                    byte[] chunk = fileService.getFileChunk(fileName, offset, chunkSize);
                    fos.write(chunk);
                    System.out.println("Отримано блок: " + chunk.length + " байтів");
                }
            }
            System.out.println("Файл успішно завантажено як " + fileName);

        } catch (Exception e) {
            System.err.println("Помилка клієнта:");
            e.printStackTrace();
        }
    }
}
