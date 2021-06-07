package filemonitor;
 
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
 
/**
 * FileMonitor
 * 文件监控器
 *
 * @author yx
 * @date 2019/12/21 0:59
 */
public class FileMonitor {
 
    /**
     * 每2秒更新的默认监控器
     */
    private static FileMonitor defaultFileMonitor = new FileMonitor(2 * 1000);
 
    private Timer timer_;
    private HashMap<File, FileEntry> files_; // File -> Long
    private List<FileEntry> fileEntrys = new java.util.concurrent.CopyOnWriteArrayList<FileEntry>();
    private Collection<WeakReference<FileListener>> listeners_; // of WeakReference(FileListener)
 
    private long pollingInterval = 10000;
 
    public static FileMonitor getDefaultFileMonitor() {
        return defaultFileMonitor;
    }
 
    /**
     * Create a file monitor instance with specified polling interval.
     *
     * @param pollingInterval Polling interval in milli seconds.
     */
    public FileMonitor(long pollingInterval) {
        this.pollingInterval = pollingInterval;
 
        files_ = new HashMap<File, FileEntry>();
        listeners_ = new ArrayList<WeakReference<FileListener>>();
 
        timer_ = new Timer("FileMonitor", true);
        timer_.schedule(new FileMonitorNotifier(), 0, pollingInterval);
    }
 
    /**
     * Stop the file monitor polling.
     */
    public void stop() {
        timer_.cancel();
        timer_ = null;
    }
 
    public void start() {
        if (timer_ == null) {
            timer_ = new Timer(true);
            timer_.schedule(new FileMonitorNotifier(), 0, pollingInterval);
        }
    }
 
    /**
     * Add file to listen for. File may be any java.io.File (including a
     * directory) and may well be a non-existing file in the case where the
     * creating of the file is to be trepped.
     * <p>
     * More than one file can be listened for. When the specified file is
     * created, modified or deleted, listeners are notified.
     *
     * @param file File to listen for.
     */
    public void addFile(String id, File file) {
        if (!files_.containsKey(file)) {
 
            FileEntry entry = new FileEntry(id, file, file.exists() ? file.lastModified() : -1);
            files_.put(file, entry);
        }
    }
 
    /**
     * 添加监控文件实体。
     *
     * @param fileEntry
     */
    public void addFileEntry(FileEntry fileEntry) {
        if (!fileEntrys.contains(fileEntry)) {
            fileEntrys.add(fileEntry);
        }
    }
 
    /**
     * 通过文件实体的标识判断监控文件实体是否存在。
     *
     * @param id
     * @return
     */
    public boolean fileEntryExists(String id) {
        if (id == null) {
            return false;
        }
 
        for (int i = 0; i < fileEntrys.size(); i++) {
            if (id.equals(fileEntrys.get(i).getId())) {
                return true;
            }
        }
 
        return false;
    }
 
    /**
     * 通过文件实体标识删除一个监控文件实体。
     *
     * @param id
     */
    public void removeFileEntry(String id) {
        if (id == null) {
            return;
        }
 
        for (int i = 0; i < fileEntrys.size(); i++) {
            if (id.equals(fileEntrys.get(i).getId())) {
                fileEntrys.remove(i);
                return;
            }
        }
    }
 
    /**
     * Remove specified file for listening.
     *
     * @param file File to remove.
     */
    public void removeFile(File file) {
        files_.remove(file);
    }
 
    /**
     * Add listener to this file monitor.
     *
     * @param fileListener Listener to add.
     */
    public void addListener(FileListener fileListener) {
        // Don't add if its already there
        for (Iterator<WeakReference<FileListener>> i = listeners_.iterator(); i.hasNext(); ) {
            WeakReference<FileListener> reference = i.next();
            FileListener listener = (FileListener) reference.get();
            if (listener == fileListener) {
                return;
            }
        }
 
        // Use WeakReference to avoid memory leak if this becomes the
        // sole reference to the object.
        listeners_.add(new WeakReference<FileListener>(fileListener));
    }
 
    /**
     * Remove listener from this file monitor.
     *
     * @param fileListener Listener to remove.
     */
    public void removeListener(FileListener fileListener) {
        for (Iterator<WeakReference<FileListener>> i = listeners_.iterator(); i.hasNext(); ) {
            WeakReference<FileListener> reference = (WeakReference<FileListener>) i.next();
            FileListener listener = (FileListener) reference.get();
            if (listener == fileListener) {
                i.remove();
                break;
            }
        }
    }
 
    /**
     * This is the timer thread which is executed every n milliseconds according
     * to the setting of the file monitor. It investigates the file in question
     * and notify listeners if changed.
     */
    private class FileMonitorNotifier extends TimerTask {
        @Override
        public void run() {
            try {
                for (Iterator<FileEntry> i = fileEntrys.iterator(); i.hasNext(); ) {
                    try {
                        FileEntry entry = i.next();
                        if (entry == null || !entry.check()) {
                            i.remove();
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                        System.out.println("执行文件监控发生错误:" + t.getMessage());
                    }
                }
 
                // Loop over the registered files and see which have changed.
                // Use a copy of the list in case listener wants to alter the
                // list within its fileChanged method.
                Collection<File> files = new ArrayList<File>(files_.keySet());
 
                for (Iterator<File> i = files.iterator(); i.hasNext(); ) {
                    File file = i.next();
                    try {
                        FileEntry fileEntry = files_.get(file);
                        long lastModifiedTime = fileEntry.getLastModified();
                        long newModifiedTime = file.exists() ? file.lastModified() : -1;
 
                        //logger.debug(file.getAbsolutePath());
                        //logger.debug("    {}=>{}", lastModifiedTime, newModifiedTime);
                        // Chek if file has changed
                        if (newModifiedTime != lastModifiedTime) {
                            //logger.debug("file changed {})", file.getAbsolutePath());
                            fileEntry.setLastModified(newModifiedTime);
                            // Register new modified time
                            files_.put(file, fileEntry);
 
                            if (fileEntry.getFileListener() != null) {
                                fileEntry.getFileListener().fileChanged(fileEntry);
                            } else {
                                // Notify listeners
                                for (Iterator<WeakReference<FileListener>> j =
                                        listeners_.iterator(); j.hasNext(); ) {
                                    WeakReference<FileListener> reference =
                                            (WeakReference<FileListener>) j.next();
                                    FileListener listener = (FileListener) reference.get();
 
                                    // Remove from list if the back-end object has been GC'd
                                    if (listener == null) {
                                        j.remove();
                                    } else {
                                        listener.fileChanged(fileEntry);
                                    }
                                }
                            }
                        }
                    } catch (Throwable t) {
                        if (file != null) {
                            t.printStackTrace();
                            System.out.println(
                                    "file monitor execute error, file=" + file.getAbsolutePath() +
                                            t.getMessage());
                        } else {
                            System.out.println(
                                    "file monitor execute error, file=null" + t.getMessage());
                        }
                    }
                }
            } catch (Throwable t) {
                System.out.println("执行文件监控发生错误" + t.getMessage());
            }
        }
    }
}
