package org.codeape.sc.shared.model

case class AuthToken(id: String)

case class AuthTokenRequest(user: String, password: String)