package io.swagger.client.api;

import io.swagger.client.ApiClient;

import io.swagger.client.model.PaginatedListTechnicalContacts;
import io.swagger.client.model.TechnicalContacts;

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

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-02-27T11:46:50.314Z")
@Component("io.swagger.client.api.TechnicalContactsApi")
public class TechnicalContactsApi {
    private ApiClient apiClient;

    public TechnicalContactsApi() {
        this(new ApiClient());
    }

    @Autowired
    public TechnicalContactsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Retrieve all Technical Contacts for a solution in a paged list,  given the solution’s CRM identifier
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - Solution not found in CRM
     * @param solutionId CRM identifier of solution
     * @param pageIndex 1-based index of page to return.  Defaults to 1
     * @param pageSize number of items per page.  Defaults to 20
     * @return PaginatedListTechnicalContacts
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public PaginatedListTechnicalContacts apiTechnicalContactsBySolutionBySolutionIdGet(String solutionId, Integer pageIndex, Integer pageSize) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'solutionId' is set
        if (solutionId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'solutionId' when calling apiTechnicalContactsBySolutionBySolutionIdGet");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("solutionId", solutionId);
        String path = UriComponentsBuilder.fromPath("/api/TechnicalContacts/BySolution/{solutionId}").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<PaginatedListTechnicalContacts> returnType = new ParameterizedTypeReference<PaginatedListTechnicalContacts>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Delete an existing Technical Contact for a Solution
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - Technical Contact or Solution not found in CRM
     * @param techCont existing Technical Contact information
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void apiTechnicalContactsDelete(TechnicalContacts techCont) throws RestClientException {
        Object postBody = techCont;
        
        String path = UriComponentsBuilder.fromPath("/api/TechnicalContacts").build().toUriString();
        
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
     * Create a new Technical Contact for a Solution
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - Solution not found in CRM
     * @param techCont new Technical Contact information
     * @return TechnicalContacts
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public TechnicalContacts apiTechnicalContactsPost(TechnicalContacts techCont) throws RestClientException {
        Object postBody = techCont;
        
        String path = UriComponentsBuilder.fromPath("/api/TechnicalContacts").build().toUriString();
        
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

        ParameterizedTypeReference<TechnicalContacts> returnType = new ParameterizedTypeReference<TechnicalContacts>() {};
        return apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Update a Technical Contact with new information
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - Technical Contact or Solution not found in CRM
     * @param techCont Technical Contact with updated information
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void apiTechnicalContactsPut(TechnicalContacts techCont) throws RestClientException {
        Object postBody = techCont;
        
        String path = UriComponentsBuilder.fromPath("/api/TechnicalContacts").build().toUriString();
        
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
