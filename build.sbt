// loads the server project at sbt startup
onLoad in Global := (Command.process("project server", _: State)) compose (onLoad in Global).value

scalaVersion in ThisBuild := "2.12.2"

lazy val commonSettings = Seq(
    version := "1.0-SNAPSHOT",
    scalaVersion := "2.12.2"
)

lazy val server = project.enablePlugins(PlayScala).settings(
    commonSettings,
    name := "play-scala-server",
    libraryDependencies ++= Seq(
        ws,
        guice,
        "com.typesafe.play" %% "play-json" % "2.6.3",
        "com.vmunier" %% "scalajs-scripts" % "1.1.0",
        "org.webjars" %% "webjars-play" % "2.6.0-M1",
        "org.webjars" % "bootstrap" % "3.3.7",
        "org.webjars" % "animate.css" % "3.5.2",
        "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % Test,
        "org.jsoup" % "jsoup" % "1.10.2" % Test
    ),
    scalaJSProjects := Seq(client),
    pipelineStages in Assets := Seq(scalaJSPipeline),
    compile in Compile := ((compile in Compile) dependsOn scalaJSPipeline).value
).dependsOn(sharedJvm)

lazy val shared = crossProject.crossType(CrossType.Pure).settings(
    name := "shared",
    commonSettings,
    libraryDependencies ++= Seq(
        "com.lihaoyi" %%% "upickle" % "0.4.4",
        "com.lihaoyi" %%% "autowire" % "0.2.6",
        "com.lihaoyi" %%% "scalatags" % "0.6.3",
        "com.typesafe.play" %% "play-json" % "2.6.0"
    )
).jsConfigure(_ enablePlugins ScalaJSWeb)
  .enablePlugins(SbtTwirl)
  .settings(
      sourceDirectories in (Compile, TwirlKeys.compileTemplates) +=
        (baseDirectory.value.getParentFile / "src" / "main" / "scala")
  )

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js


lazy val client = project.enablePlugins(ScalaJSPlugin, ScalaJSWeb).settings(
    commonSettings,
    mainClass in Compile := Some("App"),
    emitSourceMaps in fullOptJS := true,
    resolvers += "Sonatype OSS Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots",
    libraryDependencies ++= Seq(
        "org.scala-js" %%% "scalajs-dom" % "0.9.1",
        "com.lihaoyi" %%% "autowire" % "0.2.6",
        "com.lihaoyi" %%% "upickle" % "0.4.4",
        "com.lihaoyi" %%% "scalatags" % "0.6.3",
        "be.doeraene" %%% "scalajs-jquery" % "0.9.1",
        "io.surfkit" %%% "scalajs-google-maps" % "0.0.3-SNAPSHOT",
        "com.typesafe.play" %% "play-json" % "2.6.0"
    )
).dependsOn(sharedJs)


lazy val populateCatalogueCache = taskKey[Unit]("Populate conf/example-hotels with some PCS properties")

populateCatalogueCache := {
    import collection.JavaConverters._, org.jsoup.Jsoup, scala.io.Source, util.Try
    for {
        destinationId <- Seq("de549499", "de504261", "de550392", "de554288")
        hotelId <- Jsoup.connect(s"https://uk.hotels.com/$destinationId/").get.select("a[data-hotel-id]").asScala.map(_.attr("data-hotel-id"))
        _ = println(s"Retrieve JSON for $hotelId in $destinationId")
        pcsResp <- Try(Source.fromURL(s"http://hcom-pcs-stg.staging-hotels.com/property_catalogue_svc/v2/property/$hotelId").mkString).toOption
        path = (resourceDirectory in Compile).value / "example-hotels" / s"$hotelId.json"
        _ = println(s"Writing to $path")
    } IO.write(path, pcsResp)
}
