package main;

import java.util.concurrent.TimeUnit;

import org.apache.commons.io.monitor.FileAlterationMonitor;
import org.apache.commons.io.monitor.FileAlterationObserver;  


public class Main {
	private final static String bucketName = "libin";
	private final static String savePath    = "C:\\Users\\78136\\Desktop\\������ʵѵ\\work1";
	private final static String accessKey = "AA558F851A9B6DADEEA4";
	private final static String secretKey = "WzAyNkMyNTQ3MEIzMDJDNTg0RjUzQjM0MUQ4OUEy";
	private final static String serviceEndpoint = "http://10.16.0.1:81";
	
	
	public static void main(String[] args) throws Exception{
		
		S3 aws_s3 = new S3(bucketName, savePath, accessKey, secretKey, serviceEndpoint);
		aws_s3.synchronize();
		
		long interval = TimeUnit.SECONDS.toMillis(5);
        // ����һ���ļ��۲������ڴ����ļ��ĸ�ʽ  
       
        FileAlterationObserver observer = new FileAlterationObserver(savePath);
        
        observer.addListener(new FileListener(aws_s3)); //�����ļ��仯������  
        //�����ļ��仯������  
        FileAlterationMonitor monitor = new FileAlterationMonitor(interval, observer);  
        // ��ʼ���  
        try {
            monitor.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
