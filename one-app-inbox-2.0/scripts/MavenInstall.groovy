includeTargets << grailsScript("_GrailsWar")

target(main: "Install the grails jar into the local maven repository") {
    depends createConfig
    depends jar, createConfig

    try {
        runMaven("mvn.sh")
    }
    catch (IOException e) {
        runMaven("mvn.bat")
    }
}

target(jar: "Build Grails classes jar") {
    depends compile
    ant.jar(basedir: "target/classes", destfile: "target/${metadata.'app.name'}-${metadata.'app.version'}.jar")
}

def runMaven(mavenCommand) {
    def command = [mavenCommand, "install:install-file", "-DgroupId=${config.grails.project.groupId}", "-DartifactId=${metadata.'app.name'}",
            "-Dpackaging=jar", "-Dversion=${metadata.'app.version'}",
            "-Dfile=target/${metadata.'app.name'}-${metadata.'app.version'}.jar", "-DgeneratePom=true"]

    println "Executing ${command.join(" ")}"
    def proc = command.execute()

    println proc.in.text
}

setDefaultTarget(main)
