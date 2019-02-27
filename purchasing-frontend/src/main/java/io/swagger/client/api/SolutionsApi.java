package io.swagger.client.api;

import io.swagger.client.ApiClient;

import io.swagger.client.model.PaginatedListSolutions;
import io.swagger.client.model.Solutions;

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
@Component("io.swagger.client.api.SolutionsApi")
public class SolutionsApi {
    private ApiClient apiClient;

    public SolutionsApi() {
        this(new ApiClient());
    }

    @Autowired
    public SolutionsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Get existing solution/s on which were onboarded onto a framework,  given the CRM identifier of the framework
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - Framework not found in CRM
     * @param frameworkId CRM identifier of framework
     * @param pageIndex 1-based index of page to return.  Defaults to 1
     * @param pageSize number of items per page.  Defaults to 20
     * @return PaginatedListSolutions
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public PaginatedListSolutions apiSolutionsByFrameworkByFrameworkIdGet(String frameworkId, Integer pageIndex, Integer pageSize) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'frameworkId' is set
        if (frameworkId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'frameworkId' when calling apiSolutionsByFrameworkByFrameworkIdGet");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("frameworkId", frameworkId);
        String path = UriComponentsBuilder.fromPath("/api/Solutions/ByFramework/{frameworkId}").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<PaginatedListSolutions> returnType = new ParameterizedTypeReference<PaginatedListSolutions>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Get an existing solution given its CRM identifier  Typically used to retrieve previous version
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - Solution not found in CRM
     * @param id CRM identifier of solution to find
     * @return Solutions
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public Solutions apiSolutionsByIdByIdGet(String id) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'id' when calling apiSolutionsByIdByIdGet");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("id", id);
        String path = UriComponentsBuilder.fromPath("/api/Solutions/ById/{id}").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<Solutions> returnType = new ParameterizedTypeReference<Solutions>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Retrieve all current solutions in a paged list for an organisation,  given the organisation’s CRM identifier
     * 
     * <p><b>200</b> - Success
     * @param organisationId CRM identifier of organisation
     * @param pageIndex 1-based index of page to return.  Defaults to 1
     * @param pageSize number of items per page.  Defaults to 20
     * @return PaginatedListSolutions
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public PaginatedListSolutions apiSolutionsByOrganisationByOrganisationIdGet(String organisationId, Integer pageIndex, Integer pageSize) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'organisationId' is set
        if (organisationId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'organisationId' when calling apiSolutionsByOrganisationByOrganisationIdGet");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("organisationId", organisationId);
        String path = UriComponentsBuilder.fromPath("/api/Solutions/ByOrganisation/{organisationId}").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<PaginatedListSolutions> returnType = new ParameterizedTypeReference<PaginatedListSolutions>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Delete an existing solution  DEVELOPMENT MODE ONLY
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - Organisation or solution not found in CRM
     * @param solution solution
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void apiSolutionsDelete(Solutions solution) throws RestClientException {
        Object postBody = solution;
        
        String path = UriComponentsBuilder.fromPath("/api/Solutions").build().toUriString();
        
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
     * Create a new solution for an organisation
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - Organisation not found in CRM
     * <p><b>500</b> - Validation exception
     * @param solution new solution information
     * @return Solutions
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public Solutions apiSolutionsPost(Solutions solution) throws RestClientException {
        Object postBody = solution;
        
        String path = UriComponentsBuilder.fromPath("/api/Solutions").build().toUriString();
        
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

        ParameterizedTypeReference<Solutions> returnType = new ParameterizedTypeReference<Solutions>() {};
        return apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Update an existing solution with new information
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - Organisation or solution not found in CRM
     * @param solution solution with updated information
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public void apiSolutionsPut(Solutions solution) throws RestClientException {
        Object postBody = solution;
        
        String path = UriComponentsBuilder.fromPath("/api/Solutions").build().toUriString();
        
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
