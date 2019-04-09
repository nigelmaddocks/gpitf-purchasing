package io.swagger.client.api;

import io.swagger.client.ApiClient;

import io.swagger.client.model.CapabilitiesImplementedReviews;
import io.swagger.client.model.PaginatedListIEnumerableCapabilitiesImplementedReviews;

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

@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaClientCodegen", date = "2019-04-09T09:44:06.853Z")
@Component("io.swagger.client.api.CapabilitiesImplementedReviewsApi")
public class CapabilitiesImplementedReviewsApi {
    private ApiClient apiClient;

    public CapabilitiesImplementedReviewsApi() {
        this(new ApiClient());
    }

    @Autowired
    public CapabilitiesImplementedReviewsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Get all Reviews for a CapabilitiesImplemented  Each list is a distinct &#39;chain&#39; of Review ie original Review with all subsequent Review  The first item in each &#39;chain&#39; is the most current Review.  The last item in each &#39;chain&#39; is the original Review.
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - Evidence not found
     * @param evidenceId CRM identifier of CapabilitiesImplementedEvidence
     * @param pageIndex 1-based index of page to return.  Defaults to 1
     * @param pageSize number of items per page.  Defaults to 20
     * @return PaginatedListIEnumerableCapabilitiesImplementedReviews
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public PaginatedListIEnumerableCapabilitiesImplementedReviews apiCapabilitiesImplementedReviewsByEvidenceByEvidenceIdGet(String evidenceId, Integer pageIndex, Integer pageSize) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'evidenceId' is set
        if (evidenceId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'evidenceId' when calling apiCapabilitiesImplementedReviewsByEvidenceByEvidenceIdGet");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("evidenceId", evidenceId);
        String path = UriComponentsBuilder.fromPath("/api/CapabilitiesImplementedReviews/ByEvidence/{evidenceId}").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<PaginatedListIEnumerableCapabilitiesImplementedReviews> returnType = new ParameterizedTypeReference<PaginatedListIEnumerableCapabilitiesImplementedReviews>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Create a new Review for a CapabilitiesImplemented
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - CapabilitiesImplemented not found
     * @param review new Review information
     * @return CapabilitiesImplementedReviews
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public CapabilitiesImplementedReviews apiCapabilitiesImplementedReviewsPost(CapabilitiesImplementedReviews review) throws RestClientException {
        Object postBody = review;
        
        String path = UriComponentsBuilder.fromPath("/api/CapabilitiesImplementedReviews").build().toUriString();
        
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

        ParameterizedTypeReference<CapabilitiesImplementedReviews> returnType = new ParameterizedTypeReference<CapabilitiesImplementedReviews>() {};
        return apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
