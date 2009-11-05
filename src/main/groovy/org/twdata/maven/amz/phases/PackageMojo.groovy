package org.twdata.maven.amz.phases

import org.twdata.maven.amz.AbstractAmzMojo

/**
 * Executes compile scripts in the compile phase
 *
 * @goal package
 * @phase package
 */
class PackageMojo extends AbstractAmzMojo {

    void execute() {
        getAmzRunner().runScripts(scriptsDir, "package");
    }
}
