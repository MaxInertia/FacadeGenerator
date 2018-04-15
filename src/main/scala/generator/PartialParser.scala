package generator

import generator.web.HTML_Helper

/**
  * A TypeClass for a partial parser.
  * Partial parser here is defined as an object that extracts
  * a subset of the data from the files of type F. That subset
  * extracted is called 'relevant', and is completely up to
  * the implementation of this type class.
  *
  * @tparam F The file format of the files it can partial-parse
  *
  * Created by Dorian Thiessen on 2018-04-15.
  */
trait PartialParser[F] {
  /**
    * Extracts the relevant parts of the specified file.
    * What is relevant depends upon the TypeClass instance.
    *
    * @param filePath The pathname of the file to be parsed.
    * @return A list of the relevant sections of the file.
    */
  def parse(filePath: String): List[String]
}

object PartialParser {
  trait FileFormat
  sealed trait HTML extends FileFormat
  sealed trait JS extends FileFormat

  class HtmlTagContentParser(tag: String) extends PartialParser[HTML] with HTML_Helper {
    /**
      * Extracts the contents of each instance of the specified tag from the html file.
      * Example tags: "href", "id", etc...
      *
      * @param filePath The pathname of the file to be parsed.
      * @return A list of contents of each tag instance.
      */
    override def parse(filePath: String): List[String] = ??? //{
      // TODO: Load file
      // TODO: mkString from file
      // TODO: Pass file string into tagContent()
      //tagContent(fileContent, tag)
    //}
  }


}