package io.swagger.client.api;

import io.swagger.client.ApiClient;

import io.swagger.client.model.CapabilitiesImplemented;
import io.swagger.client.model.PaginatedListCapabilitiesImplemented;

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
@Component("io.swagger.client.api.CapabilitiesImplementedApi")
public class CapabilitiesImplementedApi {
    private ApiClient apiClient;

    public CapabilitiesImplementedApi() {
        this(new ApiClient());
    }

    @Autowired
    public CapabilitiesImplementedApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Retrieve claim, given the claim’s CRM identifier
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - Claim not found in CRM
     * @param id CRM identifier of claim
     * @return CapabilitiesImplemented
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public CapabilitiesImplemented apiCapabilitiesImplementedByIdGet(String id) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'id' when calling apiCapabilitiesImplementedByIdGet");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("id", id);
        String path = UriComponentsBuilder.fromPath("/api/CapabilitiesImplemented/{id}").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<CapabilitiesImplemented> returnType = new ParameterizedTypeReference<CapabilitiesImplemented>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Retrieve all claimed capabilities for a solution in a paged list,  given the solution’s CRM identifier
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - Solution not found in CRM
     * @param solutionId CRM identifier of solution
     * @param pageIndex 1-based index of page to return.  Defaults to 1
     * @param pageSize number of items per page.  Defaults to 20
     * @return PaginatedListCapabilitiesImplemented
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public PaginatedListCapabilitiesImplemented apiCapabilitiesImplementedBySolutionBySolutionIdGet(String solutionId, Integer pageIndex, Integer pageSize) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'solutionId' is set
        if (solutionId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'solutionId' when calling apiCapabilitiesImplementedBySolutionBySolutionIdGet");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("solutionId", solutionId);
        String path = UriComponentsBuilder.fromPath("/api/CapabilitiesImplemented/BySolution/{solutionId}").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "pageIndex", pageIndex));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "pageSize", pageSize));

        final String[] accepts = { 
            "application/json"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basic", "oauth2" };

        ParameterizedTypeReference<PaginatedListCapabilitiesImplemented> returnType = new ParameterizedTypeReference<PaginatedListCapabilitiesImplemented>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Delete an existing claimed capability for a solution
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - ClaimedCapability not found in CRM
     * @param claimedcapability existing claimed capability information
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void apiCapabilitiesImplementedDelete(CapabilitiesImplemented claimedcapability) throws RestClientException {
        Object postBody = claimedcapability;
        
        String path = UriComponentsBuilder.fromPath("/api/CapabilitiesImplemented").build().toUriString();
        
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
        apiClient.invokeAPI(path, HttpMethod.DELETE, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Create a new claimed capability for a solution
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - Solution not found in CRM
     * @param claimedcapability new claimed capability information
     * @return CapabilitiesImplemented
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public CapabilitiesImplemented apiCapabilitiesImplementedPost(CapabilitiesImplemented claimedcapability) throws RestClientException {
        Object postBody = claimedcapability;
        
        String path = UriComponentsBuilder.fromPath("/api/CapabilitiesImplemented").build().toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();

        final String[] accepts = { 
            "application/json"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { 
            "application/json-patch+json", "application/json", "text/json", "application/_*+json"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basic", "oauth2" };

        ParameterizedTypeReference<CapabilitiesImplemented> returnType = new ParameterizedTypeReference<CapabilitiesImplemented>() {};
        return apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Update an existing claimed capability with new information
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - Solution or ClaimedCapability not found in CRM
     * @param claimedcapability claimed capability with updated information
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void apiCapabilitiesImplementedPut(CapabilitiesImplemented claimedcapability) throws RestClientException {
        Object postBody = claimedcapability;
        
        String path = UriComponentsBuilder.fromPath("/api/CapabilitiesImplemented").build().toUriString();
        
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
