package src;
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

public class S3 extends Thread{
    private String bucketName;
    private String filePath;
    private String accessKey;
    private String secretKey;
    private String serviceEndpoint;
    private final static String signingRegion = "";
    private  AmazonS3 s3;

    public S3(AmazonS3 S3, String BucketName,String AccessKey, String SecretKey, String ServiceEndpoint,String FilePath) {
        super("S3");
        s3 = S3;
        filePath = FilePath;
        bucketName = BucketName;
        filePath = FilePath;
        accessKey = AccessKey;
        secretKey = SecretKey;
        serviceEndpoint = ServiceEndpoint;

        /*final BasicAWSCredentials credentials = new BasicAWSCredentials(accessKey,secretKey);
        final ClientConfiguration ccfg = new ClientConfiguration().withUseExpectContinue(true);
        final EndpointConfiguration endpoint = new EndpointConfiguration(serviceEndpoint, signingRegion);
        try {
            s3 = AmazonS3ClientBuilder.standard()
                    .withCredentials(new AWSStaticCredentialsProvider(credentials))
                    .withClientConfiguration(ccfg)
                    .withEndpointConfiguration(endpoint)
                    .withPathStyleAccessEnabled(true)
                    .build();
        }catch (Exception e){
            System.out.println(e);
        }*/
    }


    @Override
    public void run() {

        String keyName = filePath.replace("\\", "/");

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


}
