package src

import java.io.FileWriter
import java.text.SimpleDateFormat
import java.util.{Properties, UUID}

import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.streaming.api.scala._
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer010
import java.io.BufferedReader
import java.io.BufferedWriter
import java.io.FileReader
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.HashSet
import java.util.Set

import com.amazonaws.AmazonServiceException
import com.amazonaws.ClientConfiguration
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder.EndpointConfiguration
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest
import com.amazonaws.services.s3.model.GetObjectRequest
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest
import com.amazonaws.services.s3.model.ListObjectsV2Result
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PartETag
import com.amazonaws.services.s3.model.S3Object
import com.amazonaws.services.s3.model.S3ObjectInputStream
import com.amazonaws.services.s3.model.S3ObjectSummary
import com.amazonaws.services.s3.model.UploadPartRequest
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.node.ObjectNode
import org.apache.flink.streaming.util.serialization.JSONKeyValueDeserializationSchema
import java.util.Timer
import java.util.Vector
import java.util.TimerTask

import com.amazonaws.auth.BasicAWSCredentials

import scala.collection.JavaConversions._

object kafka_test {
  /**
   * 输入的主题名称
   */
  val inputTopic = "libin3"
  /**
   * kafka地址
   */
  val bootstrapServers = "bigdata35.depts.bingosoft.net:29035,bigdata36.depts.bingosoft.net:29036,bigdata37.depts.bingosoft.net:29037"
  /**
   * 文件是否修改
   */
  var change = false
  /**
   * 是否5秒未修改文件
   */
  var no_change_5s = true

  val accessKey = "AA558F851A9B6DADEEA4"
  val secretKey = "WzAyNkMyNTQ3MEIzMDJDNTg0RjUzQjM0MUQ4OUEy"
  //s3地址
  val endpoint = "http://scut.depts.bingosoft.net:29997"
  //上传到的桶
  val bucket = "libin"
  //上传文件的路径前缀
  val keyPrefix = "data/"
  var change_name:Set[String] = new HashSet[String]()

  private var s3:AmazonS3 = null

  class time_task(timer: Timer) extends TimerTask{
    override def run(): Unit = {
      if (s3 == null) return
      if(!change) return
      if (!no_change_5s) {
        no_change_5s = true
        return
      }
      val iterator = change_name.iterator
      while ( {iterator.hasNext}) {
        val str = iterator.next
        var s3_ = new S3(s3, bucket, accessKey, secretKey,endpoint, str);
        s3_.start()
      }

      change_name.clear()
    }
  }

  def main(args: Array[String]): Unit = {

    val credentials = new BasicAWSCredentials(accessKey, secretKey)
    val ccfg = new ClientConfiguration().withUseExpectContinue(true);
    val sendpoint = new EndpointConfiguration(endpoint, "");
    s3 = AmazonS3ClientBuilder.standard()
      .withCredentials(new AWSStaticCredentialsProvider(credentials))
      .withClientConfiguration(ccfg)
      .withEndpointConfiguration(sendpoint)
      .withPathStyleAccessEnabled(true)
      .build()

    //开始计时
    var s = new Timer()
    s.schedule(new time_task(s),1000, 5000)

    val env = StreamExecutionEnvironment.getExecutionEnvironment

    val kafkaProperties = new Properties()
    kafkaProperties.put("bootstrap.servers", bootstrapServers)
    kafkaProperties.put("group.id", UUID.randomUUID().toString)
    kafkaProperties.put("auto.offset.reset", "earliest")
    kafkaProperties.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    kafkaProperties.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")

    val kafkaConsumer = new FlinkKafkaConsumer010[ObjectNode](inputTopic,
      new JSONKeyValueDeserializationSchema(true), kafkaProperties)
    kafkaConsumer.setCommitOffsetsOnCheckpoints(true)
    val inputKafkaStream = env.addSource(kafkaConsumer)

    inputKafkaStream.map(x => {
      var fileName = x.get("value").get("buy_time").toString()
      fileName = "data/" + fileName.substring(1,fileName.indexOf("/",fileName.indexOf("/")+1)).replaceAll("/","_")

      var writer=new FileWriter(fileName,true);
      change_name.add(fileName)
      writer.write(x.toString + "\n")
      writer.close()
      println(x)
      change = true
      no_change_5s = false
    })

    env.execute()
  }
}
