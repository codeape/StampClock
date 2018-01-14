package org.codeape.sc.frontend.views

import io.udash._
import io.udash.core.Presenter
import org.codeape.sc.frontend.{Context, WeekFormState}
import org.codeape.sc.shared.model.PingRequest

import scala.util.{Failure, Success}

trait PingFormModel { //Test
  def loaded: Boolean
  def msg: String
}

class WeekFormView(model: ModelProperty[PingFormModel], presenter: WeekFormPresenter) extends View {
  import scalatags.JsDom.all._

  private val content = produce(model.subProp(_.loaded)) {
    case true => h3(model.subProp(_.msg).get).render
    case false => h3("Loading").render
  }

  override def getTemplate: Modifier = content

  override def renderChild(view: View): Unit = {}
}

class WeekFormPresenter(model: ModelProperty[PingFormModel]) extends Presenter[WeekFormState] {
  import Context._

  override def handleState(state: WeekFormState): Unit = {
    model.subProp(_.loaded).set(false)
    restServer.util().ping(Context.getToken(), PingRequest("WeekFormView")).onComplete {
      case Success(ping) =>
        model.subProp(_.msg).set(ping.msg)
        model.subProp(_.loaded).set(true)
      case Failure(ex) =>
        model.subProp(_.msg).set(ex.getMessage)
        model.subProp(_.loaded).set(true)
    }
  }
}

case class WeekFormViewPresenter() extends ViewPresenter[WeekFormState] {
  override def create(): (View, Presenter[WeekFormState]) = {
    import Context._
    val model = ModelProperty[PingFormModel]
    val presenter = new WeekFormPresenter(model)
    val view = new WeekFormView(model, presenter)
    (view, presenter)
  }
}
