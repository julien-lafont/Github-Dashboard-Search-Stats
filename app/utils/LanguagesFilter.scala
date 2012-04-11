package utils
import play.api.cache.Cache
import play.libs.Yaml
import java.util.LinkedHashMap
import scala.collection.JavaConversions._
import play.api.Play.current
import java.util.ArrayList

object LanguagesFilter {

	/**
	 * Load languages list from fixtures, group and sort the results
	 */
	def list() = {
		Cache.getOrElse[List[Tuple2[String, Seq[String]]]]("languages")({
			// We need a Scala (and Typesafe?) lib for Yaml files
			val languages = Yaml.load("languages.yml").asInstanceOf[LinkedHashMap[String, ArrayList[String]]]
			languages.map(group => (group._1.toUpperCase -> group._2.toSeq)).toList.sortWith { _._1 > _._1 }
		})
	}
}