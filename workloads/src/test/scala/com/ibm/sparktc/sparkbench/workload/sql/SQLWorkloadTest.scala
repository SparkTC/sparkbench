package com.ibm.sparktc.sparkbench.workload.sql

import com.ibm.sparktc.sparkbench.testfixtures.{BuildAndTeardownData, SparkSessionProvider}
import org.scalatest.{BeforeAndAfterAll, FlatSpec, Matchers}

class SQLWorkloadTest extends FlatSpec with Matchers with BeforeAndAfterAll {
  
  val ioStuff = new BuildAndTeardownData("sql-workload")
  
  val spark = SparkSessionProvider.spark
  val outputFileRootName = ioStuff.sparkBenchTestFolder
  val smallData = s"$outputFileRootName/small-kmeans-data.parquet"
  val resOutput = "console"

  override def beforeAll(): Unit = {
    super.beforeAll()
    ioStuff.deleteFolders()
    ioStuff.createFolders()
//    ioStuff.deleteFilesStr(Seq(smallData))
    ioStuff.generateKMeansData(1000, 10, smallData)
  }

  override def afterAll(): Unit = {
    super.afterAll()
    ioStuff.deleteFolders()
  }

  "Sql Queries over generated kmeans data" should "work" in {

    val workload = SQLWorkload(name = "sql",
      inputDir = Some(smallData),
      workloadResultsOutputDir = Some(resOutput),
      queryStr = "select `0` from input where `0` < -0.9",
      cache = false)

    workload.doWorkload(None, spark)
  }


}