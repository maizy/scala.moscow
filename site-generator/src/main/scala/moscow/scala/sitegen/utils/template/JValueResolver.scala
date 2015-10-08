package moscow.scala.sitegen.utils.template

// based on idea from
// https://github.com/mfirry/handlebars-json4s/

import java.util.Map.Entry
import java.util.{ Set => JSet }
import java.util.AbstractMap.SimpleImmutableEntry
import moscow.scala.sitegen.utils.StringUtils.toIntOpt

import scala.collection.JavaConversions._
import com.github.jknack.handlebars.ValueResolver
import org.json4s.{ JValue, JArray, JString, JNothing, JNull, JDecimal, JDouble, JBool }
import org.json4s.{ JInt, JLong, JObject }

class JValueResolver extends ValueResolver {

  import ValueResolver.UNRESOLVED

  def resolve(context: AnyRef, name: String): AnyRef = context match {
    case JArray(array) =>
      toIntOpt(name) match {
        case Some(ind) if ind >= 0 && ind <= array.length => resolveOrReturnAsIs(array(ind))
        case _ => UNRESOLVED
      }
    case JObject(fields) =>
      fields.find(_._1 == name) match {
        case Some((fieldName, value)) => resolveOrReturnAsIs(value)
        case None => UNRESOLVED
      }
    case _ => UNRESOLVED
  }

  private val primitiveValuesResolver: PartialFunction[JValue, AnyRef] = {
    case JNothing | JNull => null  // scalastyle:ignore
    case JString(s) => s
    case JDouble(num) => Double.box(num)
    case JDecimal(num) => num
    case JInt(num) => num
    case JBool(bool) => Boolean.box(bool)
    case JLong(long) => Long.box(long)
  }

  private def resolveOrUndefined(value: JValue): AnyRef = {
    if (primitiveValuesResolver.isDefinedAt(value)) {
      primitiveValuesResolver(value)
    } else {
      UNRESOLVED
    }
  }

  private def resolveOrReturnAsIs(value: JValue): AnyRef = {
    if (primitiveValuesResolver.isDefinedAt(value)) {
      primitiveValuesResolver(value)
    } else {
      value
    }
  }

  def resolve(context: Any): AnyRef = context match {
    case value: JValue => resolveOrUndefined(value)
    case _ => throw new ClassCastException
  }

  private def unpack(jfield: (String, JValue)): Entry[String, AnyRef] =
    new SimpleImmutableEntry(jfield._1, resolve(jfield._2, jfield._1))

  def propertySet(context: AnyRef): JSet[Entry[String, AnyRef]] = {
    context match {
      case JObject(obj) => obj.map(unpack).toSet: Set[Entry[String, AnyRef]]
      case _ => Set.empty[Entry[String, AnyRef]]
    }
  }
}

object JValueResolver {
  val INSTANCE = new JValueResolver()
}
