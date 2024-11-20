import java.rmi.Remote;
import java.rmi.RemoteException;

public interface FileService extends Remote {
    byte[] getFileChunk(String fileName, int offset, int chunkSize) throws RemoteException;
    long getFileSize(String fileName) throws RemoteException;
    String getFileName() throws RemoteException; // Новий метод
}
