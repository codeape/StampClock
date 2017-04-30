package org.codeape.sc.frontend

import io.udash.{RoutingRegistry, Url}

class RoutingRegistryDef extends RoutingRegistry[RoutingState] {
  def matchUrl(url: Url): RoutingState =
    url2State.applyOrElse(url.value.stripSuffix("/"), (x: String) => ErrorState)

  def matchState(state: RoutingState): Url =
    Url(state2Url.apply(state))

  private val url2State: PartialFunction[String, RoutingState] = {
    case "" => IndexState
  }

  private val state2Url: PartialFunction[RoutingState, String] = {
    case IndexState => ""
  }
}