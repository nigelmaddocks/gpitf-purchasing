package io.swagger.client.api;

import io.swagger.client.ApiClient;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-01-29T15:45:36.951Z")
@Component("io.swagger.client.api.LinkManagerApi")
public class LinkManagerApi {
    private ApiClient apiClient;

    public LinkManagerApi() {
        this(new ApiClient());
    }

    @Autowired
    public LinkManagerApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Create a link between a Framework and a Solution
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - One entity not found or link already exists
     * @param frameworkId CRM identifier of Framework
     * @param solutionId CRM identifier of Solution
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void apiLinkManagerFrameworkSolutionCreateByFrameworkIdBySolutionIdPost(String frameworkId, String solutionId) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'frameworkId' is set
        if (frameworkId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'frameworkId' when calling apiLinkManagerFrameworkSolutionCreateByFrameworkIdBySolutionIdPost");
        }
        
        // verify the required parameter 'solutionId' is set
        if (solutionId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'solutionId' when calling apiLinkManagerFrameworkSolutionCreateByFrameworkIdBySolutionIdPost");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("frameworkId", frameworkId);
        uriVariables.put("solutionId", solutionId);
        String path = UriComponentsBuilder.fromPath("/api/LinkManager/FrameworkSolution/Create/{frameworkId}/{solutionId}").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = { };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basic", "oauth2" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
