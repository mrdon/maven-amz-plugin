package org.twdata.maven.amz

import org.codehaus.groovy.maven.mojo.GroovyMojo
import groovyx.net.http.RESTClient
import groovy.util.slurpersupport.GPathResult
import static groovyx.net.http.ContentType.URLENC

/**
 * Shares a file via gist
 *
 * @goal share
 */
public class ShareMojo extends GroovyMojo {

    /**
     * The script to share.
     *
     * @parameter expression="${script}"
     * @required
     */
    protected File script

    /**
     * The script to share.
     *
     * @parameter expression="${username}"
     * @required
     */
    protected String username

    /**
     * The script to share.
     *
     * @parameter expression="${password}"
     * @required
     */
    protected String password

    public void execute() {

        String repo = createGistRepo();
        getLog().info("Gist repo at http://gist.github.com/${repo}");
    }

    private String createGistRepo() {

        def http = new RESTClient("http://gist.github.com/api/v1/xml/new")
        def postBody = [
                login: username,
                token: password];
        postBody[new String("files[${script.getName()}]")] = new String(script.text);

        def response = http.post( body: postBody,
           requestContentType: URLENC );

        println "Gist response status: ${response.status}"
        assert response.success
        assert response.status == 200
        assert response.data instanceof GPathResult
        return response.data.gist.repo;
    }

}