package org.codeape.sc.frontend.views.components

import io.udash.bootstrap.navs.{UdashNav, UdashNavbar}
import io.udash.properties.seq.SeqProperty
import io.udash.properties.single.CastableProperty
import org.codeape.sc.frontend.Context._
import org.codeape.sc.frontend.IndexState
import org.scalajs.dom.raw.Element

import scalatags.JsDom.all._

object Header {
  private val brand = a(href := IndexState.url)().render

  case class NavItem(href: String, imageSrc: String, name: String)
  private val navItems: SeqProperty[Header.NavItem, CastableProperty[Header.NavItem]] = SeqProperty.empty[Header.NavItem]

  private val nav = UdashNav.navbar(navItems)(
    (prop) => {
      val item = prop.get
      a(href := item.href, target := "_blank")().render
    }
  )

  val header = UdashNavbar.inverted(
    brand = brand,
    nav = nav
  )

  lazy val getTemplate: Element =
    header.render
}
