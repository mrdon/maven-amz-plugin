package org.twdata.maven.amz.phases

import org.twdata.maven.amz.AbstractAmzMojo

/**
 * Executes compile scripts in the compile phase
 *
 * @goal prepare-package
 * @phase prepare-package
 */
class PreparePackageMojo extends AbstractAmzMojo {

    void execute() {
        getAmzRunner().runScripts(scriptsDir, "prepare-package");
    }
}
