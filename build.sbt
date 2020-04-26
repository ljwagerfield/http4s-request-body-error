val http4sVersion  = "0.21.1"
val catsVersion    = "2.1.0"

name              := "http4s-error"
version           := "0.1"
scalaVersion      := "2.13.2"

libraryDependencies ++= Seq(
  "org.typelevel"  %% "cats-core"                % catsVersion,
  "org.typelevel"  %% "cats-effect"              % catsVersion,
  "io.monix"       %% "monix"                    % "3.1.0",
  "org.http4s"     %% "http4s-dsl"               % http4sVersion,
  "org.http4s"     %% "http4s-async-http-client" % http4sVersion,
  "org.http4s"     %% "http4s-blaze-server"      % http4sVersion,
  "ch.qos.logback"  % "logback-classic"          % "1.2.3"
)