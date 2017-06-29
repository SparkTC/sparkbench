package com.ibm.sparktc.sparkbench.cli

import java.io.File
import com.ibm.sparktc.sparkbench.workload.MultipleSuiteKickoff

object CLIKickoff extends App {

  override def main(args: Array[String]): Unit = {
    args.length match {
      // We do want this to be serial so that one suite finishes entirely and the next suite starts.
      case 1 => MultipleSuiteKickoff.run(Configurator(new File(args(0))))
      case _ => throw new IllegalArgumentException("Requires exactly one option: config file path")
    }
  }
}
