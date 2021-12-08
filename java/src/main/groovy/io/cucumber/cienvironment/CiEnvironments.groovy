import groovy.text.SimpleTemplateEngine
import groovy.json.JsonSlurper
import java.nio.file.Files

SimpleTemplateEngine engine = new SimpleTemplateEngine()
def templateSource = new File(project.basedir, "src/main/groovy/io/cucumber/cienvironment/CiEnvironments.gsp").getText()

def jsonSlurper = new JsonSlurper()
def ciEnvironments = jsonSlurper.parseText(new File(project.basedir, "../CiEnvironments.json").getText())
def toJava(s) {
    return s == null ? 'null' : "\"${s.replace("\\", "\\\\")}\""
}
def binding = ["ciEnvironments": ciEnvironments, "toJava": this.&toJava]

def template = engine.createTemplate(templateSource).make(binding)
def file = new File(project.basedir, "target/generated-sources/ci-environments/java/io/cucumber/cienvironment/CiEnvironments.java")
Files.createDirectories(file.parentFile.toPath())
file.write(template.toString(), "UTF-8")
