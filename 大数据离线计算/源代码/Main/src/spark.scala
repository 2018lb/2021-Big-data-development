import java.sql.{DriverManager, ResultSet}
import java.util.Properties

class spark(ip: String, port: String, db: String, user: String, password: String) {
  var IP = ip;
  var PORT = port;
  var DB =db;
  var UESER = user;
  var PASSWORD = password;
  val properties = new Properties()
  val url = "jdbc:hive2://" + IP + ":" + PORT + "/" + DB;

  properties.setProperty("driverClassName", "org.apache.hive.jdbc.HiveDriver")
  properties.setProperty("user", UESER)
  properties.setProperty("password", PASSWORD);

  val connection = DriverManager.getConnection(url, properties)

  //查找所有数据库
  def showdb():ResultSet ={
    try {
      val statement = connection.createStatement
      val S = "show databases";
      val resultSet = statement.executeQuery(S);
      return resultSet;
    }catch {
      case e:Exception=>{throw e;}
    }
  }

  //查找db数据库下所有表
  def showtable(db:String): ResultSet ={
    try {
      val statement = connection.createStatement
      val S = "show tables from " + db;
      val resultSet = statement.executeQuery(S);
      return resultSet;
    }catch {
      case e:Exception=>{throw e;}
    }

  }


  def showword(table:String): ResultSet ={
    try {
      val statement = connection.createStatement
      val S = "desc formatted " + table
      val resultSet = statement.executeQuery(S)
      return resultSet;
    }catch {
      case e:Exception=>{throw e;}
    }
  }

  def work(S:String): ResultSet ={
    try {
      val statement = connection.createStatement
      var resultSet = statement.executeQuery(S);
      return resultSet;
    }catch {
      case e:Exception=>{throw e;}
    }

  }

  def test(): Boolean ={

    try {
      val statement = connection.createStatement
    }catch {
      case e:Exception=>{throw e;}
    }
    true;
  }
}

