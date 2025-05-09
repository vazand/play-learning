name := """play-learning"""
organization := "com.scalaplay"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.16"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.1" % Test

// https://mvnrepository.com/artifact/org.postgresql/postgresql
libraryDependencies += "org.postgresql" % "postgresql" % "42.7.3"
// https://mvnrepository.com/artifact/org.playframework/play-slick
libraryDependencies += "org.playframework" %% "play-slick" % "6.0.0-M2"
// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.scalaplay.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.scalaplay.binders._"
