package org.codeape.sc.frontend.views

import fr.hmil.roshttp.exceptions.HttpException
import io.udash._
import io.udash.bootstrap.button.{ButtonStyle, UdashButton}
import io.udash.bootstrap.form.UdashForm
import io.udash.core.Presenter
import org.codeape.sc.frontend.{Context, WeekFormState, LoginFormState}
import org.codeape.sc.shared.model.AuthTokenRequest

import scala.util.{Failure, Success}

trait LoginFormModel {
  def user: String
  def group: String
  def password: String

  def status: String
}

class LoginFormView(model: ModelProperty[LoginFormModel], presenter: LoginFormPresenter) extends View {
  import scalatags.JsDom.all._

  private val loginButton = UdashButton(
    buttonStyle = ButtonStyle.Primary,
    block = true
  )(span("Login"))

  loginButton.listen {
    case UdashButton.ButtonClickEvent(_) =>
      presenter.login()
  }

  private val content = div(
    UdashForm(
      UdashForm.textInput()("User")(model.subProp(_.user)),
      UdashForm.passwordInput()("Password")(model.subProp(_.password)),
      loginButton.render,
      br,
      div(bind(model.subProp(_.status)))
    ).render
  ).render

  override def getTemplate: Modifier = content

  override def renderChild(view: View): Unit = {}
}

class LoginFormPresenter(model: ModelProperty[LoginFormModel]) extends Presenter[LoginFormState] {

  import Context._

  override def handleState(state: LoginFormState): Unit = {
    model.subProp(_.user).set("")
    model.subProp(_.group).set("")
    model.subProp(_.password).set("")
    model.subProp(_.status).set("")
  }

  def login(): Unit = restServer.auth().login(
    AuthTokenRequest(
      user = model.subProp(_.user).get,
      group = model.subProp(_.group).get,
      password = model.subProp(_.password).get
    )
  ).onComplete{
    case Success(token) =>
      Context.setToken(token.id)
      applicationInstance.goTo(WeekFormState())
    case Failure(HttpException(response)) =>
      model.subProp(_.status).set(s"Fail: ${response.body} ${response.statusCode}")
    case Failure(ex) =>
      model.subProp(_.status).set(s"Fail: $ex")
  }

}

case class LoginFormViewPresenter() extends ViewPresenter[LoginFormState] {
  override def create(): (View, Presenter[LoginFormState]) = {
    import Context._
    val model = ModelProperty[LoginFormModel]
    val presenter = new LoginFormPresenter(model)
    val view = new LoginFormView(model, presenter)
    (view, presenter)
  }
}
