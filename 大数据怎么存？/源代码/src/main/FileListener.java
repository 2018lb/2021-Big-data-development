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
	     * 文件创建执行
	     */
	    @Override
	    public void onFileCreate(File file) {
	    	aws_S3.upload(file.getAbsolutePath());
	        System.out.println("[新建]:" + file.getAbsolutePath());
	    }

	    /**
	     * 文件创建修改
	     */
	    @Override
	    public void onFileChange(File file) {
	    	aws_S3.upload(file.getAbsolutePath());
	        System.out.println("[修改]:" + file.getAbsolutePath());
	    }

	    /**
	     * 文件删除
	     */
	    @Override
	    public void onFileDelete(File file) {
	    	aws_S3.delete(file.getAbsolutePath());
	        System.out.println("[删除]:" + file.getAbsolutePath());
	    }


	    /**
	     * 目录删除
	     */
	    @Override
	    public void onDirectoryDelete(File directory) {
	    	aws_S3.delete(directory.getAbsolutePath()+"/");
	        System.out.println("[删除]:" + directory.getAbsolutePath());
	    }

	    @Override
	    public void onStart(FileAlterationObserver observer) {
	        // TODO Auto-generated method stub

	        super.onStart(observer);
	        System.out.println("文件监听中。。。");
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