package org.twdata.maven.amz

import org.twdata.maven.amz.AbstractAmzMojo

/**
 * Executes compile scripts in the compile phase
 *
 * @goal compile
 * @phase compile
 */
class CompileMojo extends AbstractAmzMojo {

    void execute() {
        getAmzRunner().runScripts(scriptsDir, "compile");
    }
}
