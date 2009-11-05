package org.twdata.maven.amz

import static org.twdata.maven.mojoexecutor.MojoExecutor.*
import org.twdata.maven.mojoexecutor.MojoExecutor.ExecutionEnvironment
import org.apache.maven.project.MavenProject
import org.apache.maven.execution.MavenSession
import org.apache.maven.plugin.PluginManager
import org.apache.maven.plugin.logging.Log;
/**
 *
 */

public class AmzRunner {
    ExecutionEnvironment env;
    Log log;

    public AmzRunner(MavenProject project, MavenSession session, PluginManager pluginManager, Log log) {
        this.env = new ExecutionEnvironment(project, session, pluginManager)
        this.log = log;
    }

    void runScripts(File baseScriptsDir, String phase) {
        File scriptsDir = new File(baseScriptsDir, phase);
        if (!scriptsDir.exists()) {
            log.debug("No scripts found for phase " + phase);
            return;
        }
        scriptsDir.eachFile {File scriptFile ->
            if (scriptFile.name.endsWith("~") || scriptFile.name.startsWith(".")) {
                return;
            }
            log.info("Executing ${scriptFile.getAbsolutePath()}");
            executeMojo(
                    plugin(
                            groupId("org.codehaus.groovy.maven"),
                            artifactId("gmaven-plugin"),
                            // keep this in sync with the pom
                            version("1.0")
                    ),
                    goal("execute"),
                    configuration(
                            element(name("source"), scriptFile.getAbsolutePath()),
                            element(name("classpath"),
                                    element(name("element"),
                                            element(name("groupId"), "org.codehaus.groovy"),
                                            element(name("artifactId"), "groovy-xmlrpc"),
                                            element(name("version"), "0.4")),
                                    element(name("element"),
                                            element(name("groupId"), "com.dolby.jira.net"),
                                            element(name("artifactId"), "jira-soap"),
                                            element(name("version"), "3.13.4")),
                                    element(name("element"),
                                            element(name("groupId"), "org.codehaus.groovy.modules.http-builder"),
                                            element(name("artifactId"), "http-builder"),
                                            element(name("version"), "0.5.0-RC1")))

                    ),
                    env
            );
        }
    }
}