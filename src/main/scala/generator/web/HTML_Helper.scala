package generator.web

/**
  * Created by Dorian Thiessen on 2018-04-15.
  */
trait HTML_Helper {
  /**
    * Collect the content of each instance of the specified tag into a list.
    * Example
    *     tag: "href"
    *     page: href="XYZ"
    *     returned: List("XYZ")
    *
    * Note: Assumes no spaces between the tag and '=', or the '=' and the first double quote '"'
    *
    * @param page The content of an html page
    * @param tag The tag whose content is to be collected
    * @param acc Accumulator for internal use only (default argument provided)
    * @return A list of the contents from the instances of the provided tag
    */
  def tagContent(page: String, tag: String, acc: List[String] = List()): List[String] = {
    // Find index pattern appears
    val pattern: String = tag+"=\""
    val pend: Int = page.indexOf(pattern) + pattern.length

    // Terminate if pattern not found
    if(pend == pattern.length - 1) return acc

    // Collect string to right of pattern
    def getContentAfterPattern(partialPage: String, acc: String = ""): String =
      partialPage match {
        case cs if cs.startsWith("\"") => acc
        case cs => getContentAfterPattern(partialPage.tail, acc:+cs.head)
      }

    val found = getContentAfterPattern(page.drop(pend))

    tagContent(page.drop(pend + found.length), tag, acc :+ found)
  }
}
