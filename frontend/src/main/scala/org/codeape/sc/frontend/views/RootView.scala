package org.codeape.sc.frontend.views

import io.udash._
import io.udash.bootstrap.utils.UdashJumbotron
import io.udash.bootstrap.{BootstrapStyles, UdashBootstrap}
import io.udash.core.DefaultViewPresenterFactory
import org.codeape.sc.frontend.RootState
import org.codeape.sc.frontend.views.components.Header
import org.scalajs.dom._

import scalatags.JsDom.tags2._

object RootViewPresenter extends DefaultViewPresenterFactory[RootState.type](() => new RootView)

class RootView extends View {
  import scalatags.JsDom.all._

  private val child: Element = div().render

  private val content = div(
    UdashBootstrap.loadBootstrapStyles(),
    Header.getTemplate,
    main(BootstrapStyles.container)(
      div(
        UdashJumbotron(
          h1("rest-spray-io"),
          p("Welcome in the Udash REST and the Udash Bootstrap modules demo!")
        ).render,
        child
      )
    )
  ).render

  override def getTemplate: Modifier = content

  override def renderChild(view: View): Unit = {
    import io.udash.wrappers.jquery._
    jQ(child).children().remove()
    view.getTemplate.applyTo(child)
  }
}
