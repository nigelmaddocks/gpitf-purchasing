package io.swagger.client.api;

import io.swagger.client.ApiClient;

import io.swagger.client.model.PaginatedListIEnumerableStandardsApplicableEvidence;
import io.swagger.client.model.StandardsApplicableEvidence;

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
@Component("io.swagger.client.api.StandardsApplicableEvidenceApi")
public class StandardsApplicableEvidenceApi {
    private ApiClient apiClient;

    public StandardsApplicableEvidenceApi() {
        this(new ApiClient());
    }

    @Autowired
    public StandardsApplicableEvidenceApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Get all Evidence for the given Claim  Each list is a distinct &#39;chain&#39; of Evidence ie original Evidence with all subsequent Evidence  The first item in each &#39;chain&#39; is the most current Evidence.  The last item in each &#39;chain&#39; is the original Evidence.
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - StandardsApplicable not found
     * @param claimId CRM identifier of Claim
     * @param pageIndex 1-based index of page to return.  Defaults to 1
     * @param pageSize number of items per page.  Defaults to 20
     * @return PaginatedListIEnumerableStandardsApplicableEvidence
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public PaginatedListIEnumerableStandardsApplicableEvidence apiStandardsApplicableEvidenceByClaimByClaimIdGet(String claimId, Integer pageIndex, Integer pageSize) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'claimId' is set
        if (claimId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'claimId' when calling apiStandardsApplicableEvidenceByClaimByClaimIdGet");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("claimId", claimId);
        String path = UriComponentsBuilder.fromPath("/api/StandardsApplicableEvidence/ByClaim/{claimId}").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<PaginatedListIEnumerableStandardsApplicableEvidence> returnType = new ParameterizedTypeReference<PaginatedListIEnumerableStandardsApplicableEvidence>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Create a new evidence
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - Claim not found
     * @param evidence new evidence information
     * @return StandardsApplicableEvidence
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public StandardsApplicableEvidence apiStandardsApplicableEvidencePost(StandardsApplicableEvidence evidence) throws RestClientException {
        Object postBody = evidence;
        
        String path = UriComponentsBuilder.fromPath("/api/StandardsApplicableEvidence").build().toUriString();
        
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

        ParameterizedTypeReference<StandardsApplicableEvidence> returnType = new ParameterizedTypeReference<StandardsApplicableEvidence>() {};
        return apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
