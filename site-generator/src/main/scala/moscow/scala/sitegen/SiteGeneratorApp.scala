import com.gilt.handlebars.scala.binding.dynamic._
import com.gilt.handlebars.scala.Handlebars

object SiteGeneratorApp extends App {
    println("Try handlebars template")

    val template = """
      <p>Hello, my name is {{name}}. I am from {{hometown}}. I have {{kids.length}} kids:</p>
      <ul>
        {{#kids}}<li>{{name}} is {{age}}</li>{{/kids}}
      </ul>
    """
    object Guy {
      val name = "Alan"
      val hometown = "Somewhere, TX"
      val kids = Seq(Map(
        "name" -> "Jimmy",
        "age" -> "12"
      ), Map(
        "name" -> "Sally",
        "age" -> "4"
      ))
    }

    val t = Handlebars(template)
    println(t(Guy))
}
