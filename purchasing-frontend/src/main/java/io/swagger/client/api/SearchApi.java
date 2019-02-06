package io.swagger.client.api;

import io.swagger.client.ApiClient;

import io.swagger.client.model.PaginatedListSearchResult;

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
@Component("io.swagger.client.api.SearchApi")
public class SearchApi {
    private ApiClient apiClient;

    public SearchApi() {
        this(new ApiClient());
    }

    @Autowired
    public SearchApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Get existing solution/s which are related to the given keyword &lt;br /&gt;  Keyword is not case sensitive &lt;br /&gt;  Capabilities are searched for capabilities which contain  the keyword in the capability name or descriptions.  This  forms a set of desired capabilities. &lt;br /&gt;  These desired capabilities are then matched to solution/s which  implement at least one of the desired capabilities. &lt;br /&gt;  An &#39;ideal&#39; solution would be one which only implements all  of the desired capabilities. &lt;br /&gt;  Each solution is given a &#39;distance&#39; which represents how many  different capabilites the solution implements, compared to the  set of desired capabilities: &lt;br /&gt;    zero     &#x3D;&#x3D; solution has exactly capabilities desired &lt;br /&gt;    positive &#x3D;&#x3D; solution has more capabilities than desired &lt;br /&gt;    negative &#x3D;&#x3D; solution has less capabilities than desired &lt;br /&gt;
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - No Solutions found with keyword
     * @param keyword keyword describing a solution or capability
     * @param pageIndex 1-based index of page to return.  Defaults to 1
     * @param pageSize number of items per page.  Defaults to 20
     * @return PaginatedListSearchResult
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public PaginatedListSearchResult apiPorcelainSearchByKeywordByKeywordGet(String keyword, Integer pageIndex, Integer pageSize) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'keyword' is set
        if (keyword == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'keyword' when calling apiPorcelainSearchByKeywordByKeywordGet");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("keyword", keyword);
        String path = UriComponentsBuilder.fromPath("/api/porcelain/Search/ByKeyword/{keyword}").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<PaginatedListSearchResult> returnType = new ParameterizedTypeReference<PaginatedListSearchResult>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
