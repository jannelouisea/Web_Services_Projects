name := """play-java-seed"""
organization := "com.example"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.12.2"

libraryDependencies += guice
libraryDependencies += javaJdbc
libraryDependencies += "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.7"
libraryDependencies += "org.xerial" % "sqlite-jdbc" % "3.8.11.2"

