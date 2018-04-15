name := "FacadeGenerator"
version := "0.1"
scalaVersion := "2.12.4"
organization := "InertialFrame"

enablePlugins(GhpagesPlugin)
enablePlugins(SiteScaladocPlugin)
git.remoteRepo := s"git@github.com:MaxInertia/$name.git"

/*scalacOptions += "-Ypartial-unification"
libraryDependencies ++= Seq(
  "org.typelevel" %% "cats-core" % "1.1.0",
  "org.typelevel" %% "dogs-core" % "0.6.10"
)*/