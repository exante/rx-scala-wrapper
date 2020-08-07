ThisBuild / scalaVersion := "2.13.3"
ThisBuild / organization := "eu.exante"
ThisBuild / version := "2.2.0-SNAPSHOT"
ThisBuild / autoAPIMappings := true

lazy val apiMap = taskKey[Map[File, URL]]("An int task")
apiMap := {
    def findManagedDependency(organization: String, name: String): File = {
      ( for {
        entry <- (fullClasspath in Compile).value
        module <- entry.get(moduleID.key)
        if module.organization == organization
        if module.name.startsWith(name)
        jarFile = entry.data
      } yield jarFile
        ).head
    }

    Map (
      findManagedDependency("io.reactivex.rxjava2", "rxjava") -> url("http://reactivex.io/RxJava/2.x/")
    )
}

lazy val rx_scala_wrapper = (project in file("."))
  .settings(
    name := "rx-scala-wrapper",
    libraryDependencies += "io.reactivex.rxjava2" % "rxjava" % "[2.2, 2.3)",
    libraryDependencies += "org.scalactic" %% "scalactic" % "3.0.8",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test",
    autoAPIMappings := true,
    apiMappings ++= apiMap.value,
    scalacOptions += "-feature"
  )
