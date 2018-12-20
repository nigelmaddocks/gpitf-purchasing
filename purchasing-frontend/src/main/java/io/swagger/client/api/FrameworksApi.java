package io.swagger.client.api;

import io.swagger.client.ApiClient;

import io.swagger.client.model.Frameworks;
import io.swagger.client.model.PaginatedListFrameworks;

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
@Component("io.swagger.client.api.FrameworksApi")
public class FrameworksApi {
    private ApiClient apiClient;

    public FrameworksApi() {
        this(new ApiClient());
    }

    @Autowired
    public FrameworksApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Get existing framework/s which have the given capability
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - Capability not found in CRM
     * @param capabilityId CRM identifier of Capability
     * @param pageIndex 1-based index of page to return.  Defaults to 1
     * @param pageSize number of items per page.  Defaults to 20
     * @return PaginatedListFrameworks
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public PaginatedListFrameworks apiFrameworksByCapabilityByCapabilityIdGet(String capabilityId, Integer pageIndex, Integer pageSize) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'capabilityId' is set
        if (capabilityId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'capabilityId' when calling apiFrameworksByCapabilityByCapabilityIdGet");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("capabilityId", capabilityId);
        String path = UriComponentsBuilder.fromPath("/api/Frameworks/ByCapability/{capabilityId}").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<PaginatedListFrameworks> returnType = new ParameterizedTypeReference<PaginatedListFrameworks>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Get an existing framework given its CRM identifier  Typically used to retrieve previous version
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - Framework not found in CRM
     * @param id CRM identifier of framework to find
     * @return Frameworks
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public Frameworks apiFrameworksByIdByIdGet(String id) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'id' is set
        if (id == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'id' when calling apiFrameworksByIdByIdGet");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("id", id);
        String path = UriComponentsBuilder.fromPath("/api/Frameworks/ById/{id}").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<Frameworks> returnType = new ParameterizedTypeReference<Frameworks>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Get existing framework/s on which a solution was onboarded, given the CRM identifier of the solution
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - Solution not found in CRM
     * @param solutionId CRM identifier of solution
     * @param pageIndex 1-based index of page to return.  Defaults to 1
     * @param pageSize number of items per page.  Defaults to 20
     * @return PaginatedListFrameworks
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public PaginatedListFrameworks apiFrameworksBySolutionBySolutionIdGet(String solutionId, Integer pageIndex, Integer pageSize) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'solutionId' is set
        if (solutionId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'solutionId' when calling apiFrameworksBySolutionBySolutionIdGet");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("solutionId", solutionId);
        String path = UriComponentsBuilder.fromPath("/api/Frameworks/BySolution/{solutionId}").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<PaginatedListFrameworks> returnType = new ParameterizedTypeReference<PaginatedListFrameworks>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Get existing framework/s which have the given standard
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - Standard not found in CRM
     * @param standardId CRM identifier of Standard
     * @param pageIndex 1-based index of page to return.  Defaults to 1
     * @param pageSize number of items per page.  Defaults to 20
     * @return PaginatedListFrameworks
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public PaginatedListFrameworks apiFrameworksByStandardByStandardIdGet(String standardId, Integer pageIndex, Integer pageSize) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'standardId' is set
        if (standardId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'standardId' when calling apiFrameworksByStandardByStandardIdGet");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("standardId", standardId);
        String path = UriComponentsBuilder.fromPath("/api/Frameworks/ByStandard/{standardId}").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<PaginatedListFrameworks> returnType = new ParameterizedTypeReference<PaginatedListFrameworks>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Retrieve all current frameworks in a paged list
     * 
     * <p><b>200</b> - Success - if no frameworks found, return empty list
     * @param pageIndex 1-based index of page to return.  Defaults to 1
     * @param pageSize number of items per page.  Defaults to 20
     * @return PaginatedListFrameworks
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public PaginatedListFrameworks apiFrameworksGet(Integer pageIndex, Integer pageSize) throws RestClientException {
        Object postBody = null;
        
        String path = UriComponentsBuilder.fromPath("/api/Frameworks").build().toUriString();
        
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

        ParameterizedTypeReference<PaginatedListFrameworks> returnType = new ParameterizedTypeReference<PaginatedListFrameworks>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
