package com.github.scribejava.core.model;

import com.github.scribejava.core.exceptions.OAuthException;
import com.github.scribejava.core.httpclient.MultipartPayload;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * The representation of an OAuth HttpRequest.
 */
public class OAuthRequest {

    private static final String OAUTH_PREFIX = "oauth_";

    private final String url;
    private final Verb verb;
    private final ParameterList querystringParams = new ParameterList();
    private final ParameterList bodyParams = new ParameterList();
    private final Map<String, String> headers = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
    private final Map<String, String> oauthParameters = new HashMap<>();
    private String charset;
    private String stringPayload;
    private byte[] byteArrayPayload;
    private File filePayload;
    private MultipartPayload multipartPayload;
    private String realm;

    /**
     * Default constructor.
     *
     * @param verb Http verb/method
     * @param url  resource URL
     */
    public OAuthRequest(Verb verb, String url) {
        this.verb = verb;
        this.url = url;
    }

    /**
     * Adds an OAuth parameter.
     *
     * @param key   name of the parameter
     * @param value value of the parameter
     * @throws IllegalArgumentException if the parameter is not an OAuth parameter
     */
    public void addOAuthParameter(String key, String value) {
        oauthParameters.put(checkKey(key), value);
    }

    private String checkKey(String key) {
        if (key.startsWith(OAUTH_PREFIX) || key.equals(OAuthConstants.SCOPE) || key.equals(OAuthConstants.REALM)) {
            return key;
        } else {
            throw new IllegalArgumentException(
              String.format("OAuth parameters must either be '%s', '%s' or start with '%s'", OAuthConstants.SCOPE,
                            OAuthConstants.REALM, OAUTH_PREFIX));
        }
    }

    /**
     * Add an HTTP Header to the Request
     *
     * @param key   the header name
     * @param value the header value
     */
    public void addHeader(String key, String value) {
        headers.put(key, value);
    }

    /**
     * Add a body Parameter (for POST/ PUT Requests)
     *
     * @param key   the parameter name
     * @param value the parameter value
     */
    public void addBodyParameter(String key, String value) {
        bodyParams.add(key, value);
    }

    /**
     * Add a QueryString parameter
     *
     * @param key   the parameter name
     * @param value the parameter value
     */
    public void addQuerystringParameter(String key, String value) {
        querystringParams.add(key, value);
    }

    public void addParameter(String key, String value) {
        if (verb.isPermitBody()) {
            bodyParams.add(key, value);
        } else {
            querystringParams.add(key, value);
        }
    }

    /**
     * you can invoke {@link #initMultipartBoundary(java.lang.String) } to set custom boundary
     *
     * @param contentDisposition contentDisposition
     * @param contentType        contentType
     * @param payload            payload
     */
    public void addMultipartPayload(String contentDisposition, String contentType, byte[] payload) {
        if (multipartPayload == null) {
            initMultipartBoundary();
        }
        multipartPayload.addMultipartPayload(contentDisposition, contentType, payload);
    }

    /**
     * init boundary of multipart request with default boundary
     */
    public void initMultipartBoundary() {
        initMultipartBoundary(null);
    }

    /**
     * Set boundary of multipart request
     *
     * @param boundary can be any string
     */
    public void initMultipartBoundary(String boundary) {
        multipartPayload = new MultipartPayload(boundary == null
                                                ? Long.toString(System.currentTimeMillis())
                                                : boundary);
    }

    public void setMultipartPayload(String contentDisposition, String contentType, byte[] payload) {
        setMultipartPayload(null, contentDisposition, contentType, payload);
    }

    public void setMultipartPayload(String boundary, String contentDisposition, String contentType, byte[] payload) {
        initMultipartBoundary(boundary);
        multipartPayload.addMultipartPayload(contentDisposition, contentType, payload);
    }

    @Override
    public String toString() {
        return String.format("@Request(%s %s)", getVerb(), getUrl());
    }

    /**
     * Obtains the URL of the HTTP Request.
     *
     * @return the original URL of the HTTP Request
     */
    public String getUrl() {
        return url;
    }

    public Verb getVerb() {
        return verb;
    }

    public interface ResponseConverter<T> {

        T convert(Response response) throws IOException;
    }

    /**
     * Obtains a {@link ParameterList} of the body parameters.
     *
     * @return a {@link ParameterList}containing the body parameters.
     */
    public ParameterList getBodyParams() {
        return bodyParams;
    }

    /**
     * @return the body of the request (set in {@link #setPayload(byte[])} or in
     * {@link #addBodyParameter(java.lang.String, java.lang.String)} )
     */
    public byte[] getByteArrayPayload() {
        if (byteArrayPayload != null) {
            return byteArrayPayload;
        }
        final String body = bodyParams.asFormUrlEncodedString();
        try {
            return body.getBytes(getCharset());
        } catch (UnsupportedEncodingException uee) {
            throw new OAuthException("Unsupported Charset: " + getCharset(), uee);
        }
    }

    public String getCharset() {
        return charset == null ? Charset.defaultCharset().name() : charset;
    }

    /**
     * Set the charset of the body of the request
     *
     * @param charsetName name of the charset of the request
     */
    public void setCharset(String charsetName) {
        charset = charsetName;
    }

    /**
     * Returns the complete url (host + resource + encoded querystring parameters).
     *
     * @return the complete url.
     */
    public String getCompleteUrl() {
        return querystringParams.appendTo(url);
    }

    public File getFilePayload() {
        return filePayload;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public MultipartPayload getMultipartPayloads() {
        return multipartPayload;
    }

    public Map<String, String> getOauthParameters() {
        return oauthParameters;
    }

    /**
     * Get a {@link ParameterList} with the query string parameters.
     *
     * @return a {@link ParameterList} containing the query string parameters.
     * @throws OAuthException if the request URL is not valid.
     */
    public ParameterList getQueryStringParams() {
        try {
            final ParameterList result = new ParameterList();
            final String queryString = new URL(url).getQuery();
            result.addQuerystring(queryString);
            result.addAll(querystringParams);
            return result;
        } catch (MalformedURLException mue) {
            throw new OAuthException("Malformed URL", mue);
        }
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    /**
     * Returns the URL without the port and the query string part.
     *
     * @return the OAuth-sanitized URL
     */
    public String getSanitizedUrl() {
        if (url.startsWith("http://") && (url.endsWith(":80") || url.contains(":80/"))) {
            return url.replaceAll("\\?.*", "").replaceAll(":80", "");
        } else if (url.startsWith("https://") && (url.endsWith(":443") || url.contains(":443/"))) {
            return url.replaceAll("\\?.*", "").replaceAll(":443", "");
        } else {
            return url.replaceAll("\\?.*", "");
        }
    }

    /**
     * Returns the body of the request (set in {@link #setPayload(java.lang.String)})
     *
     * @return form encoded string
     */
    public String getStringPayload() {
        return stringPayload;
    }

    /**
     * Set body payload. This method is used when the HTTP body is not a form-url-encoded string, but another thing.
     * Like for example XML. Note: The contents are not part of the OAuth signature
     *
     * @param payload the body of the request
     */
    public void setPayload(String payload) {
        resetPayload();
        stringPayload = payload;
    }

    private void resetPayload() {
        stringPayload = null;
        byteArrayPayload = null;
        filePayload = null;
        multipartPayload = null;
    }

    /**
     * Overloaded version for byte arrays
     *
     * @param payload byte[]
     */
    public void setPayload(byte[] payload) {
        resetPayload();
        byteArrayPayload = payload.clone();
    }

    /**
     * Overloaded version for File
     *
     * @param payload File
     */
    public void setPayload(File payload) {
        resetPayload();
        filePayload = payload;
    }
}
