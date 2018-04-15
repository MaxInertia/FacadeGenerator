package generator.web

/**
  * Functional, but to be replaced.
  *
  * Created by Dorian Thiessen on 2018-04-15.
  */
trait GitHub_URL_Helper {
  val github: String = "https://github.com"
  val githubUserContent: String = "https://raw.githubusercontent.com"
}

// TODO: Remove HTML_Helper mixin
case class ThreeJSURLHelper(revision: Int) extends GitHub_URL_Helper with HTML_Helper {
  val pkgDir: String = s"/mrdoob/three.js/tree/r$revision/src"
  val fileDir: String = s"/mrdoob/three.js/blob/r$revision/src/"

  /**
    * The directory of a 'raw' file view.
    * See blobLinkToRawLink for a description.
    *
    * @param user The github username the repo resides under
    * @param repo The name of the github repository
    * @param branch The desired branch/tag
    * @param filepath The path to the file in that branch/tag
    * @return The link to the 'raw' view of the file
    */
  def githubRawFile(user: String, repo: String, branch: String, filepath: String): String =
    s"https://raw.githubusercontent.com/$user/$repo/$branch/$filepath"

  /**
    * Converts the link to a file in the github repository to
    * the 'raw' file link to simplify parsing of the file.
    *
    * If the difference is not clear, compare the content of the links below.
    *
    * Example links:
    *   blob: https://github.com/mrdoob/three.js/blob/r91/src/scenes/Scene.js
    *   raw:  https://raw.githubusercontent.com/mrdoob/three.js/r91/src/scenes/Scene.js
    * @param blob the link to a file in the github repository
    * @return The link to the 'raw' view of the file
    */
  def blobLinkToRawLink(blob: String): String = {
    var v = 0
    var acc = 0
    var xs: List[String] = List()
    while(v < 4) {
      val i = blob.drop(acc).indexOf('/')
      acc += i+1
      val i2 = blob.drop(acc).indexOf('/')
      xs = xs :+ blob.slice(acc, acc + i2)
      v += 1
    }
    githubRawFile(xs.head, xs(1), xs(3), blob.drop(acc + xs(3).length + 1))
  }
}
