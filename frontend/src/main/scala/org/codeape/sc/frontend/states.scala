package org.codeape.sc.frontend

import io.udash.{Application, State}


sealed abstract class RoutingState(val parentState: RoutingState) extends State {
  def url(implicit application: Application[RoutingState]): String = s"#${application.matchState(this).value}"
}

case object RootState extends RoutingState(null)

case object ErrorState extends RoutingState(RootState)

case class WeekFormState() extends RoutingState(RootState)

case class LoginFormState() extends RoutingState(RootState)
