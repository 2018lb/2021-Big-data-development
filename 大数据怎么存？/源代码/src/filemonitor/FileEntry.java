package filemonitor;
 
import java.io.File;
import java.lang.ref.WeakReference;
 
/**
 * FileEntry
 * �ļ�Entry�����FileEntryָ����FileListener����ô���ļ������䶯ʱֻ����ָ����FileListener
 *
 * @author yx
 * @date 2019/12/21 0:56
 */
public class FileEntry {
 
    String id;
    File file;
    long lastModified;
    FileListener fileListener = null;
    Object userData;
    WeakReference<Object> reference = null;
 
    /**
     * ���캯����
     *
     * @param id
     * @param file
     */
    public FileEntry(String id, File file) {
        this(id, file, file.exists() ? file.lastModified() : -1);
    }
 
    public FileEntry(Object reference, String id, File file) {
        this(id, file, file.exists() ? file.lastModified() : -1);
 
        reference = new WeakReference<Object>(reference);
    }
 
    /**
     * ���캯����
     *
     * @param id           ��ʶ
     * @param file         Ҫ��ص��ļ�
     * @param lastmodified ����޸�����
     */
    public FileEntry(String id, File file, long lastmodified) {
        super();
        this.id = id;
        this.file = file;
        this.lastModified = lastmodified;
    }
 
    public boolean check() {
        if (reference != null && reference.get() == null) {
            //��ض����Ѿ������ڣ�����FileMonitorɾ���Լ�
            return false;
        }
 
        long newModifiedTime = file.exists() ? file.lastModified() : -1;
        if (lastModified != newModifiedTime) {
            this.lastModified = newModifiedTime;
            FileListener ls = this.getFileListener();
            if (ls == null) {
                return false;
            } else {
                try {
                    ls.fileChanged(this);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("ִ���ļ�����¼�����" + e.getMessage());
                }
 
                return true;
            }
        } else {
            return true;
        }
    }
 
    public String getId() {
        return id;
    }
 
    public void setId(String id) {
        this.id = id;
    }
 
    public File getFile() {
        return file;
    }
 
    public void setFile(File file) {
        this.file = file;
    }
 
    public long getLastModified() {
        return lastModified;
    }
 
    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }
 
    public FileListener getFileListener() {
        return fileListener;
    }
 
    public void setFileListener(FileListener fileListener) {
        this.fileListener = fileListener;
    }
 
    public Object getUserData() {
        return userData;
    }
 
    public void setUserData(Object userData) {
        this.userData = userData;
    }
}