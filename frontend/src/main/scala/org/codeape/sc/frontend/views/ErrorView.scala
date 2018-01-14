package org.codeape.sc.frontend.views

import io.udash._
import io.udash.core.DefaultViewPresenterFactory
import org.codeape.sc.frontend.ErrorState

object ErrorViewPresenter extends DefaultViewPresenterFactory[ErrorState.type](() => new ErrorView)

class ErrorView extends View {
  import scalatags.JsDom.all._

  private val content = h3(
    "URL not found!"
  ).render

  override def getTemplate: Modifier = content

  override def renderChild(view: View): Unit = {}
}
