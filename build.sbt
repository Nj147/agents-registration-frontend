name := """agents-registration-frontend"""
organization := "gov.uk.hmrc.agentsRegistrationFrontend"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.6"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test
libraryDependencies += "com.github.tomakehurst" % "wiremock-jre8" % "2.27.1"
libraryDependencies += "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.12.3"
libraryDependencies += "org.scalatestplus" %% "mockito-3-4" % "3.2.9.0" % "test"
libraryDependencies += "org.scalactic" %% "scalactic" % "3.2.9"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.9" % "test"
libraryDependencies += "org.jsoup"  %  "jsoup"  % "1.13.1"
libraryDependencies += ws

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "agents-registration-frontend.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "agents-registration-frontend.binders._"
