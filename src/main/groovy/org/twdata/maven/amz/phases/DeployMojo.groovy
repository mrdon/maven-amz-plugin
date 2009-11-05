package org.twdata.maven.amz.phases

import org.twdata.maven.amz.AbstractAmzMojo

/**
 * Executes compile scripts in the compile phase
 *
 * @goal deploy
 * @phase deploy
 */
class DeployMojo extends AbstractAmzMojo {

    void execute() {
        getAmzRunner().runScripts(scriptsDir, "deploy");
    }
}
