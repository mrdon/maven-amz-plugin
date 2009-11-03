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
                            element(name("source"), scriptFile.getAbsolutePath())
                    ),
                    env
            );
        }
    }
}