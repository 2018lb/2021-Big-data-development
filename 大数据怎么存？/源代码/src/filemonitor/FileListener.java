package filemonitor;
 
/**
 * FileListener
 *
 * @author yx
 * @date 2019/12/21 0:55
 */
public interface FileListener {
	/**
	 * 
	 * @param fileEntry
	 */
	public void fileChanged(FileEntry fileEntry);
}