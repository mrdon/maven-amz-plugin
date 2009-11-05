import groovy.net.xmlrpc.*
import com.dolby.jira.net.soap.jira.*;

def genClient(String baseUrl) {
    def svcGetter = new JiraSoapServiceServiceLocator();
    svcGetter.setJirasoapserviceV2EndpointAddress("${baseUrl}/rpc/soap/jirasoapservice-v2?wsdl")
    svcGetter.setMaintainSession(true)
    return svcGetter.getJirasoapserviceV2();
}

if (project.issueManagement == null) {
    throw new IllegalArgumentException("No <issueManagement> section specified")
}
def jiraUrl = project.issueManagement.url
def projectKey = null;
if (jiraUrl.contains("/browse/")) {
    def pos = jiraUrl.indexOf("/browse/");
    projectKey = jiraUrl.substring(pos + "/browse/".length())
    jiraUrl = jiraUrl.substring(0, pos);
    if (projectKey.endsWith("/")) {
        projectKey = projectKey.substring(0, projectKey.length() - 1)
    }    
}
println "jiraUrl ${jiraUrl}"
println "project key ${projectKey}"
jiraUrl = "http://localhost:8888"


def jira = genClient(jiraUrl);

//def jira = new XMLRPCServerProxy("${jiraUrl}/rpc/xmlrpc").jira1
def auth = jira.login("admin", "admin");
println("auth ${auth}");

def releaseProps = new Properties();
releaseProps.load(new FileInputStream('release.properties'));
def relVersion = null;
def relVersionName = releaseProps.getProperty("project.rel.${project.groupId}:${project.artifactId}");
def devVersionName = releaseProps.getProperty("project.dev.${project.groupId}:${project.artifactId}");
devVersionName = devVersionName.substring(0, devVersionName.indexOf('-SNAPSHOT'));

println "found version name ${relVersionName}"
def devVersionExists = false;
def versions = jira.getVersions(auth, projectKey);
versions.each {
        if (relVersionName.equals(it.name)) {
            relVersion = it;
            relVersion.setReleased(true);
        } else if (devVersionName.equals(it.name)) {
            devVersionExists = true;
        }    
};
assert relVersion != null
println "found version id ${relVersion.id}";

println "Releasing version ${relVersion.name} ${relVersion.isReleased()}"
jira.releaseVersion(auth, projectKey, relVersion);
println "Version ${relVersionName} released"
if (!devVersionExists) {
    println "Dev version ${devVersionName} doesn't exist in JIRA.  Creating..."
    def devVersion = new RemoteVersion();
    devVersion.setName(devVersionName);
    jira.addVersion(auth, projectKey, devVersion);
    println "Dev version ${devVersionName} created"
}

