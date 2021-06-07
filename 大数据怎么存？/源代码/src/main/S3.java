package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.UploadPartRequest;

public class S3 {
	private String bucketName;
	private String savePath;
	private String accessKey;
	private String secretKey;
	private String serviceEndpoint;
	private final static String signingRegion = "";
	private final AmazonS3 s3;
	
	public S3(String BucketName, String SavePath, String AccessKey, String SecretKey, String ServiceEndpoint) {
		bucketName = BucketName;
		savePath = SavePath;
		accessKey = AccessKey;
		secretKey = SecretKey;
		serviceEndpoint = ServiceEndpoint;
		
		final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
		final ClientConfiguration ccfg = new ClientConfiguration().withUseExpectContinue(true);
		final EndpointConfiguration endpoint = new EndpointConfiguration(serviceEndpoint, signingRegion);
		s3 = AmazonS3ClientBuilder.standard()
				                .withCredentials(new AWSStaticCredentialsProvider(credentials))
				                .withClientConfiguration(ccfg)
				                .withEndpointConfiguration(endpoint)
				                .withPathStyleAccessEnabled(true)
				                .build();
	}
	
	
	public void delete(String filePath) {
		String keyName = Paths.get(filePath).toString().replace(savePath+"\\", "").replace("\\", "/");
		
		try {
		    s3.deleteObject(bucketName, keyName);
		} catch (AmazonServiceException e) {
		    System.err.println(e.getErrorMessage());
		    System.exit(1);
		}
		System.out.printf("删除 %s 文件成功!\n", keyName);
	}
	
	public void upload(String filePath) {
						
		String keyName = Paths.get(filePath).toString().replace(savePath+"\\", "").replace("\\", "/");
						
		// Create a list of UploadPartResponse objects. You get one of these
	    // for each part upload.
		ArrayList<PartETag> partETags = new ArrayList<PartETag>();
		File file = new File(filePath);
		long contentLength = file.length();
		String uploadId = null;
		
		try {
			// Step 1: Initialize.
			InitiateMultipartUploadRequest initRequest = 
					new InitiateMultipartUploadRequest(bucketName, keyName);
			uploadId = s3.initiateMultipartUpload(initRequest).getUploadId();
			System.out.format("Created upload ID was %s\n", uploadId);

			// Step 2: Upload parts.
			long filePosition = 0;
			long partSize = 20 << 20;
			for (int i = 1; filePosition < contentLength; i++) {
				// Last part can be less than 20 MB. Adjust part size.
				partSize = Math.min(partSize, contentLength - filePosition);

				// Create request to upload a part.
				UploadPartRequest uploadRequest = new UploadPartRequest()
						.withBucketName(bucketName)
						.withKey(keyName)
						.withUploadId(uploadId)
						.withPartNumber(i)
						.withFileOffset(filePosition)
						.withFile(file)
						.withPartSize(partSize);

				// Upload part and add response to our list.
				System.out.format("Uploading part %d\n", i);
				partETags.add(s3.uploadPart(uploadRequest).getPartETag());

				filePosition += partSize;
			}

			// Step 3: Complete.
			CompleteMultipartUploadRequest compRequest = 
					new CompleteMultipartUploadRequest(bucketName, keyName, uploadId, partETags);

			s3.completeMultipartUpload(compRequest);
			System.out.printf("Completing upload  %s\n", keyName);
		} 
		catch (Exception e) {
			System.err.println(e.toString());
			if (uploadId != null && !uploadId.isEmpty()) {
				// Cancel when error occurred
				System.out.println("Aborting upload");
				s3.abortMultipartUpload(new AbortMultipartUploadRequest(bucketName, keyName, uploadId));
			}
			System.exit(1);
		}
		//System.out.printf("上传 %s 文件成功!\n", keyName);
	}

	
	public void synchronize(){
		final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
		final ClientConfiguration ccfg = new ClientConfiguration().
				withUseExpectContinue(true);

		final EndpointConfiguration endpoint = new EndpointConfiguration(serviceEndpoint, signingRegion);

		final AmazonS3 s3 = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withClientConfiguration(ccfg)
                .withEndpointConfiguration(endpoint)
                .withPathStyleAccessEnabled(true)
                .build();
		
		ListObjectsV2Result result = s3.listObjectsV2(bucketName);
		List<S3ObjectSummary> objects = result.getObjectSummaries();
		for (S3ObjectSummary os : objects) {
			
			String cnt_File = os.getKey();
			final String filePath = Paths.get(savePath, cnt_File).toString();
			
			File file = new File(filePath);
			

			if (cnt_File.substring(cnt_File.length()-1).equals("/")) {
				
				//File file = new File(filePath);
				if(!file.exists()){//如果文件夹不存在
					file.mkdirs();//创建文件夹
				}
				
				continue;
			}
			
			
			File pofile = new File(filePath.substring(0, filePath.lastIndexOf("\\")));
			
			if(!pofile.exists()){//如果文件夹不存在
				pofile.mkdirs();//创建文件夹
			}
			
			S3Object o = null;
			
			S3ObjectInputStream s3is = null;
			FileOutputStream fos = null;
			String keyName = cnt_File;
			
			try {
				// Step 1: Initialize.
				StringBuilder s = new StringBuilder(file.getPath());
				s.insert(s.lastIndexOf("\\")+1, "(old)");
				
				File change_file = file;
				File changed_file = new File(new String (s));
				if (change_file.renameTo(changed_file)) {
					System.out.printf("文件 %s 冲突，已修改原文件名\n", file.getPath());
				} 
				
				
				ObjectMetadata oMetaData = s3.getObjectMetadata(bucketName, keyName);
				final long contentLength = oMetaData.getContentLength();
				final GetObjectRequest downloadRequest = new GetObjectRequest(bucketName, keyName);

				fos = new FileOutputStream(file);
				
				// Step 2: Download parts.
				long filePosition = 0;
				long partSize = 20 << 20;
				for (int i = 1; filePosition < contentLength; i++) {
					// Last part can be less than 20 MB. Adjust part size.
					partSize = Math.min(partSize, contentLength - filePosition);

					// Create request to download a part.
					downloadRequest.setRange(filePosition, filePosition + partSize);
					o = s3.getObject(downloadRequest);

					// download part and save to local file.
					System.out.format("Downloading part %d\n", i);

					filePosition += partSize+1;
					s3is = o.getObjectContent();
					byte[] read_buf = new byte[64 * 1024];
					int read_len = 0;
					while ((read_len = s3is.read(read_buf)) > 0) {
						fos.write(read_buf, 0, read_len);
					}
				}

				// Step 3: Complete.
				System.out.println("Completing download");

				System.out.format("save %s to %s\n", keyName, filePath);
			} catch (Exception e) {
				System.err.println(e.toString());
				
				System.exit(1);
			} finally {
				if (s3is != null) try { s3is.close(); } catch (IOException e) { }
				if (fos != null) try { fos.close(); } catch (IOException e) { }
			}
			System.out.printf("同步 %s 文件完成", keyName);
		}
		System.out.println("同步完成!");
	}
}
