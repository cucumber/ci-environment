import groovy.text.SimpleTemplateEngine
import groovy.json.JsonSlurper
import java.nio.file.Files

SimpleTemplateEngine engine = new SimpleTemplateEngine()
def templateSource = new File(project.basedir, "src/main/groovy/io/cucumber/createmeta/CiServers.gsp").getText()

def jsonSlurper = new JsonSlurper()
def ciServers = jsonSlurper.parseText(new File(project.basedir, "../ciDict.json").getText())
def toJava(s) {
    return s == null ? 'null' : "\"${s.replace("\\", "\\\\")}\""
}
def binding = ["ciServers": ciServers, "toJava": this.&toJava]

def template = engine.createTemplate(templateSource).make(binding)
def file = new File(project.basedir, "target/generated-sources/ci-servers/java/io/cucumber/createmeta/CiServers.java")
Files.createDirectories(file.parentFile.toPath())
file.write(template.toString(), "UTF-8")
