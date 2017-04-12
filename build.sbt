import Dependencies._

val jarName = "spark-bench"

lazy val commonSettings = Seq(
  organization := "com.ibm.sparktc",
  scalaVersion := "2.11.8",
  parallelExecution in Test := false
)

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    scalacOptions ++= Seq("-feature", "-Ylog-classpath"),
    mainClass in assembly := Some("CLIKickoff"),
    assemblyJarName in assembly := s"$jarName.jar"
  )
  .aggregate(utils, workloads, datageneration, cli)
  .dependsOn(utils, workloads, datageneration, cli)

lazy val utils = project
  .settings(
    commonSettings,
    libraryDependencies ++= sparkDeps,
    libraryDependencies ++= testDeps
  )

lazy val workloads = project
  .settings(
    commonSettings,
    libraryDependencies ++= sparkDeps,
    libraryDependencies ++= otherCompileDeps,
    libraryDependencies ++= testDeps
  )
  .dependsOn(utils)

lazy val datageneration = project
  .settings(
    commonSettings,
    libraryDependencies ++= sparkDeps,
    libraryDependencies ++= otherCompileDeps,
    libraryDependencies ++= testDeps
  )
  .dependsOn(utils % "compile->compile;test->test")

lazy val cli = project
  .settings(
    commonSettings,
    libraryDependencies ++= sparkDeps,
    libraryDependencies ++= testDeps
  )
  .dependsOn(workloads, datageneration, utils)
