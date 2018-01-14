package org.codeape.sc.frontend

import io.udash._
import org.codeape.sc.frontend.views._

class StatesToViewPresenterDef extends ViewPresenterRegistry[RoutingState] {
  def matchStateToResolver(state: RoutingState): ViewPresenter[_ <: RoutingState] = state match {
    case RootState => RootViewPresenter
    case LoginFormState() => LoginFormViewPresenter()
    case WeekFormState() => WeekFormViewPresenter()
    case _ => ErrorViewPresenter
  }
}