package org.twdata.maven.amz

import org.apache.maven.execution.MavenSession
import org.apache.maven.plugin.PluginManager
import org.apache.maven.project.MavenProject
import org.codehaus.groovy.maven.mojo.GroovyMojo
import org.twdata.maven.amz.AmzRunner


/**
 *
 */
public abstract class AbstractAmzMojo extends GroovyMojo {
    /**
     * The scripts base directory.
     *
     * @parameter expression="${scriptsDirectory}" default-value="${basedir}/scripts"
     */
    protected File scriptsDir

    /**
     * The Maven Project Object
     *
     * @parameter expression="${project}"
     * @required
     * @readonly
     */
    protected MavenProject project;

    /**
     * The Maven Session Object
     *
     * @parameter expression="${session}"
     * @required
     * @readonly
     */
    protected MavenSession session;

    /**
     * The Maven PluginManager Object
     *
     * @component
     * @required
     */
    protected PluginManager pluginManager;

    AmzRunner getAmzRunner() {
        return new AmzRunner(project, session, pluginManager, getLog());
    }

}