import java.io.File;
import java.io.FileInputStream;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class FileServer extends UnicastRemoteObject implements FileService {

    private static final int PORT = 1099;
    private static final String FILE_NAME = "Звіт.docx";

    protected FileServer() throws RemoteException {
        super();
    }

    @Override
    public byte[] getFileChunk(String fileName, int offset, int chunkSize) throws RemoteException {
        try (FileInputStream fis = new FileInputStream(new File(fileName))) {
            fis.skip(offset);
            byte[] buffer = new byte[chunkSize];
            int bytesRead = fis.read(buffer, 0, chunkSize);
            if (bytesRead < chunkSize) {
                byte[] actualBytes = new byte[bytesRead];
                System.arraycopy(buffer, 0, actualBytes, 0, bytesRead);
                return actualBytes;
            }
            return buffer;
        } catch (Exception e) {
            System.err.println("Помилка читання файлу: " + fileName);
            e.printStackTrace();
            throw new RemoteException("Помилка читання файлу: " + fileName, e);
        }
    }

    @Override
    public long getFileSize(String fileName) throws RemoteException {
        File file = new File(fileName);
        if (file.exists() && file.isFile()) {
            return file.length();
        } else {
            System.err.println("Файл не знайдено: " + fileName);
            throw new RemoteException("Файл не знайдено: " + fileName);
        }
    }

    @Override
    public String getFileName() throws RemoteException {
        System.out.println("Клієнт підключився до сервера.");
        return FILE_NAME;
    }

    public static void main(String[] args) {
        try {
            System.out.println("Запускаємо сервер...");

            FileServer server = new FileServer();
            Registry registry = LocateRegistry.createRegistry(PORT);
            registry.rebind("FileService", server);

            System.out.println("Сервер запущений на порту " + PORT);
            System.out.println("Очікування підключення клієнтів...");

        } catch (Exception e) {
            System.err.println("Помилка запуску сервера:");
            e.printStackTrace();
        }
    }
}
