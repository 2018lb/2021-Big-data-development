package main;
import java.io.File;

import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;

public class FileListener extends FileAlterationListenerAdaptor {
	private S3 aws_S3;
	
	public FileListener(S3 aws_s3) {
		setAws_S3(aws_s3);
	}
	    /**
	     * �ļ�����ִ��
	     */
	    @Override
	    public void onFileCreate(File file) {
	    	aws_S3.upload(file.getAbsolutePath());
	        System.out.println("[�½�]:" + file.getAbsolutePath());
	    }

	    /**
	     * �ļ������޸�
	     */
	    @Override
	    public void onFileChange(File file) {
	    	aws_S3.upload(file.getAbsolutePath());
	        System.out.println("[�޸�]:" + file.getAbsolutePath());
	    }

	    /**
	     * �ļ�ɾ��
	     */
	    @Override
	    public void onFileDelete(File file) {
	    	aws_S3.delete(file.getAbsolutePath());
	        System.out.println("[ɾ��]:" + file.getAbsolutePath());
	    }


	    /**
	     * Ŀ¼ɾ��
	     */
	    @Override
	    public void onDirectoryDelete(File directory) {
	    	aws_S3.delete(directory.getAbsolutePath()+"/");
	        System.out.println("[ɾ��]:" + directory.getAbsolutePath());
	    }

	    @Override
	    public void onStart(FileAlterationObserver observer) {
	        // TODO Auto-generated method stub

	        super.onStart(observer);
	        System.out.println("�ļ������С�����");
	    }

	    @Override
	    public void onStop(FileAlterationObserver observer) {
	        // TODO Auto-generated method stub
	        super.onStop(observer);
	        //System.out.println("111111111111");
	    }
	    
		public S3 getAws_S3() {
			return aws_S3;
		}
		public void setAws_S3(S3 aws_S3) {
			this.aws_S3 = aws_S3;
		}

	}