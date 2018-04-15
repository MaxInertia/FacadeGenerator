package generator

import generator.PartialParser.HTML
import generator.web.ThreeJSURLHelper

import scala.io.Source

/**
  * Created by Dorian Thiessen on 2018-04-14.
  */
object Main {

  // TODO: It would be much simpler to just clone the github repo and provide the directory of the clone here.
  // TODO: Extract the methods here into an instance of PartialParser + refine them

  def getFilePaths(helper: ThreeJSURLHelper): List[String] = {
    val pageContent = Source.fromURL(helper.github + helper.pkgDir).mkString

    /** Gather all files and folders in the page */
    def singleLevelScan(pageContent: String): (List[String], List[String]) = {
      val refs = helper.tagContent(pageContent, "href")
        .filter(x => x.startsWith(helper.pkgDir)
          || x.startsWith(helper.fileDir))
        .filter(_ != "")
        .distinct

      val files = refs.filter(_.endsWith(".js"))
      val pkges = refs.filter(!_.endsWith(".js"))

      // For each package, perform the same procedure.
      (files, pkges)
    }

    val (files, pkges) = singleLevelScan(pageContent)

    /** Gather all files and folders nested in the folders of this page */
    def deepScan(packages: List[String],
                 fsAcc: List[String] = List(),
                 psAcc: List[String] = List()): (List[String], List[String]) = {
      val pattern = "src"
      val pIndex = packages.head.indexOf(pattern)
      val pageContent = Source.fromURL(
        helper.github + helper.pkgDir + packages.head.drop(pIndex + pattern.length)).mkString
      val (fs, ps) = singleLevelScan(pageContent)
      if (packages.length == 1) return (fs, ps)
      val (fs2, ps2) = deepScan(packages.tail, fs, ps)
      (fs ++ fs2, ps ++ ps2)
    }

    val (fs, ps) = deepScan(pkges)
    val allJSFiles = (files ++ fs).distinct
    val allPaths = (pkges ++ ps).distinct

    println(s"Paths: ${allPaths.length}")
    for (i <- allPaths.indices) println(s"${i + 1}: " + allPaths(i))
    println(s"JS Files: ${allJSFiles.length}")
    for (i <- allJSFiles.indices) println(s"${i + 1}: " + allJSFiles(i))

    allJSFiles
  }

  def parseJavascriptFields(link: String): List[String] = {
    val pageContent = Source.fromURL(link).getLines().filter(_!="")
    var fields: List[String] = List()
    val pattern = "this."
    for(line <- pageContent) {
      val i = line.indexOf(pattern) + pattern.length
      if(i != pattern.length - 1)
        fields = fields :+ line.drop(i).takeWhile(x => x!=' ' && x!='\n')
    }
    fields.distinct
  }

  def filesToFields(files: List[String], helper: ThreeJSURLHelper, acc: Map[String, List[String]] = Map()): Map[String, List[String]] = {
    if(files.isEmpty) return acc
    val url = helper.blobLinkToRawLink(files.head)
    val fields = parseJavascriptFields(url)
    filesToFields(files.tail, helper, acc + (files.head -> fields))
  }

  def main(args: Array[String]): Unit = {
    val tjsHelper = ThreeJSURLHelper(revision = 91)
    val filePaths = getFilePaths(tjsHelper)
    for(f <- filePaths) println(f)

    var link = tjsHelper.githubRawFile("mrdoob", "three.js", "r91", "src/scenes/Scene.js")

    var allFields = filesToFields(filePaths, tjsHelper)
    for(f <- allFields) {
      println(f._1)
      for(field <- f._2) println("\t" + field)
      println()
    }
  }
}
