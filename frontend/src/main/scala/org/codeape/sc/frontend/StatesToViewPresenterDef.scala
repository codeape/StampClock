package org.codeape.sc.frontend

import io.udash._
import org.codeape.sc.frontend.views.{ErrorViewPresenter, RootViewPresenter}

class StatesToViewPresenterDef extends ViewPresenterRegistry[RoutingState] {
  def matchStateToResolver(state: RoutingState): ViewPresenter[_ <: RoutingState] = state match {
    case RootState => RootViewPresenter
    case _ => ErrorViewPresenter
  }
}