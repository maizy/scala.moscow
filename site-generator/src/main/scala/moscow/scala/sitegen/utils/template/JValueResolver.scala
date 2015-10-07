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

  def resolve(context: AnyRef, name: String): AnyRef = {
    println(s"NAME=$name  in  $context  (${context.getClass})")
    val res = context match {
      case JArray(array) =>
        toIntOpt(name) match {
          case Some(ind) if ind >= 0 && ind <= array.length =>
            resolvePrimitive(array(ind))
          case _ => UNRESOLVED
        }
      case JObject(fields) =>
        fields.find(_._1 == name) match {
          case Some((fieldName, value)) => resolvePrimitive(value)
          case None => UNRESOLVED
        }
      case _ => UNRESOLVED
    }
    println(s"=N= $res (${res == UNRESOLVED}})")
    res
  }

  def resolvePrimitive(value: JValue): AnyRef = {
    val res = value match {
      case JNothing | JNull => null
      case JString(s) => s
      case JDouble(num) => Double.box(num)
      case JDecimal(num) => num
      case JInt(num) => num
      case JBool(bool) => Boolean.box(bool)
      case JLong(long) => Long.box(long)
      case other => other
    }
    println(s"RESOLVE=$value\n\t=> $res")
    res
  }

  def resolve(context: Any): AnyRef = {
    println(s"EXTRACT=$context")
    context match {
      case c: JValue =>
        val res = c match {
          case JNothing => null
          case JNull => null
          case JString(s) => s
          case JDouble(num) => Double.box(num)
          case JDecimal(num) => num
          case JInt(num) => num
          case JBool(bool) => Boolean.box(bool)
          case JLong(long) => Long.box(long)
          case JObject(obj) => obj
          case JArray(array) => seqAsJavaList(array)
        }
        println(s"=E= $res")
        res
      case _ =>
        println(s"!=E= ${context.getClass} not jValue")
        throw new ClassCastException
    }
  }

  private def unpack(jfield: (String, JValue)): Entry[String, AnyRef] =
    new SimpleImmutableEntry(jfield._1, resolve(jfield._2, jfield._1))

  def propertySet(context: AnyRef): JSet[Entry[String, AnyRef]] = {
    println(s"properties for $context")
    context match {
      case JObject(obj) =>
        val res = obj.map(unpack).toSet: Set[Entry[String, AnyRef]]
        print(s"= $res\n")
        res

      case _ =>
        println(s"no properties\n")
        Set.empty[Entry[String, AnyRef]]
    }
  }
}

object JValueResolver {
  val INSTANCE = new JValueResolver()
}
