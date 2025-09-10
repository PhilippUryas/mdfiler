ThisBuild / scalaVersion     := "3.3.6"
ThisBuild / version          := "1.0-SNAPSHOT"
ThisBuild / organization     := "com.philippuryas"

lazy val root = (project in file("."))
//  .enablePlugins(LiquibasePlugin)
  .settings(
    name := "mdfiler",

    Compile / compile / scalacOptions ++= Seq(
      "-deprecation",
      "-feature",
      "-unchecked",
      "-encoding", "UTF-8"
    ),

//    liquibaseProps := Map(
//      "changeLogFile" -> "src/main/resources/db/changelog.xml", // твой changelog
//      "url"           -> "jdbc:postgresql://localhost:5432/mydb",
//      "username"      -> "postgres",
//      "password"      -> "secret",
//      "driver"        -> "org.postgresql.Driver"
//    ),

    // Зависимости
    libraryDependencies ++= Seq(
      // Doobie
      "org.tpolecat"    %% "doobie-core"      % "1.0.0-RC8",
      "org.tpolecat"    %% "doobie-postgres"  % "1.0.0-RC8",
      "org.tpolecat"    %% "doobie-hikari"    % "1.0.0-RC8",

      // Http4s
      "org.http4s"      %% "http4s-dsl"       % "0.23.18",
      "org.http4s"      %% "http4s-ember-server" % "0.23.18",
      "org.http4s"      %% "http4s-circe"     % "0.23.18",
      "dev.profunktor"  %% "http4s-jwt-auth"  % "1.2.0",

      // JWT
      "com.github.jwt-scala" %% "jwt-circe"   % "9.4.4",

      // Circe
      "io.circe"        %% "circe-generic"    % "0.14.5",
      "io.circe"        %% "circe-parser"     % "0.14.5",

      // Logging
      "org.typelevel"   %% "log4cats-slf4j"   % "2.6.0",
      "ch.qos.logback" % "logback-classic" % "1.4.11",

      // Database driver
      "org.postgresql"   % "postgresql"       % "42.7.3",

      // Liquibase
      "org.liquibase"    % "liquibase-core"   % "4.29.2",

      // Cats
      "org.typelevel"   %% "cats-effect"      % "3.6.3",
      "org.typelevel"   %% "cats-core"        % "2.12.0"
    )
  )
