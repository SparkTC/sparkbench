package com.ibm.sparktc.sparkbench.testfixtures

import java.io.File

import com.holdenkarau.spark.testing.Utils
import com.ibm.sparktc.sparkbench.utils.KMeansDefaults
import com.ibm.sparktc.sparkbench.utils.SparkFuncs.writeToDisk
import org.apache.spark.mllib.util.KMeansDataGenerator
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.Row
import org.apache.spark.sql.types.{DoubleType, StructField, StructType}

class BuildAndTeardownData(prefix: String) {
  val sparkBenchTestFolder = s"$prefix/spark-bench-test"
  val kmeansFile = s"${sparkBenchTestFolder}/kmeans-data.parquet"
  val sparkBenchDemoFolder = s"$prefix/spark-bench-demo"
  val spark = SparkSessionProvider.spark

  def createFolders(): Unit = {
    val fileSeq = Seq(new File(sparkBenchTestFolder), new File(sparkBenchDemoFolder))
    fileSeq.foreach(folder => folder.mkdirs())
  }

  def deleteFolders(): Unit = {
//    val fileSeq = Seq(new File(sparkBenchTestFolder), new File(sparkBenchDemoFolder))
//    fileSeq.foreach(folder => Utils.deleteRecursively(folder))
    Utils.deleteRecursively(new File(prefix))
  }

  def generateKMeansData(rows: Int, cols: Int, outputFile: String): Unit = {

    val data: RDD[Array[Double]] = KMeansDataGenerator.generateKMeansRDD(
      spark.sparkContext,
      rows,
      KMeansDefaults.NUM_OF_CLUSTERS,
      cols,
      KMeansDefaults.SCALING,
      KMeansDefaults.NUM_OF_PARTITIONS
    )

    val schemaString = data.first().indices.map(_.toString).mkString(" ")
    val fields = schemaString.split(" ").map(fieldName => StructField(fieldName, DoubleType, nullable = false))
    val schema = StructType(fields)

    val rowRDD = data.map(arr => Row(arr:_*))

    val df = spark.createDataFrame(rowRDD, schema)

    writeToDisk(outputFile, df, spark)
  }

}