package io.swagger.client.api;

import io.swagger.client.ApiClient;

import io.swagger.client.model.SolutionEx;

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

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2018-12-11T12:47:15.885Z")
@Component("io.swagger.client.api.SolutionsExApi")
public class SolutionsExApi {
    private ApiClient apiClient;

    public SolutionsExApi() {
        this(new ApiClient());
    }

    @Autowired
    public SolutionsExApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Get a Solution with a list of corresponding TechnicalContact, ClaimedCapability, ClaimedStandard et al
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - Solution not found in CRM
     * @param solutionId CRM identifier of Solution
     * @return SolutionEx
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public SolutionEx apiPorcelainSolutionsExBySolutionBySolutionIdGet(String solutionId) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'solutionId' is set
        if (solutionId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'solutionId' when calling apiPorcelainSolutionsExBySolutionBySolutionIdGet");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("solutionId", solutionId);
        String path = UriComponentsBuilder.fromPath("/api/porcelain/SolutionsEx/BySolution/{solutionId}").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = { 
            "application/json"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basic", "oauth2" };

        ParameterizedTypeReference<SolutionEx> returnType = new ParameterizedTypeReference<SolutionEx>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Update an existing Solution, TechnicalContact, ClaimedCapability, ClaimedStandard et al with new information
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - Solution, TechnicalContact, ClaimedCapability, ClaimedStandard et al not found in CRM
     * <p><b>500</b> - Datastore exception
     * @param solnEx Solution, TechnicalContact, ClaimedCapability, ClaimedStandard et al with updated information
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void apiPorcelainSolutionsExUpdatePut(SolutionEx solnEx) throws RestClientException {
        Object postBody = solnEx;
        
        String path = UriComponentsBuilder.fromPath("/api/porcelain/SolutionsEx/Update").build().toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = { };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { 
            "application/json-patch+json", "application/json", "text/json", "application/_*+json"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basic", "oauth2" };

        ParameterizedTypeReference<Void> returnType = new ParameterizedTypeReference<Void>() {};
        apiClient.invokeAPI(path, HttpMethod.PUT, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
