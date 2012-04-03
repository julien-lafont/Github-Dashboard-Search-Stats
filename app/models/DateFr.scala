package models

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import scala.util.control.Exception.catching
import play.api.libs.json.Format
import play.api.libs.json.JsString
import play.api.libs.json.JsValue
import org.joda.time.format.ISODateTimeFormat
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import java.util.Locale

object DateFr {

  val dateParserISO8601 = ISODateTimeFormat.dateTimeParser()
  val dateFormatFR = DateTimeFormat.mediumDateTime().withLocale(Locale.FRENCH)
  
  implicit object DateFormat extends Format[DateTime] {
    
  	def reads(json: JsValue): DateTime = {
    	json match {
        case JsString(s) => dateParserISO8601.parseDateTime(s)
        case _ => new DateTime()
    }}
    
    def writes(date: DateTime) : JsValue = JsString(date.toString(dateFormatFR))
  }
}