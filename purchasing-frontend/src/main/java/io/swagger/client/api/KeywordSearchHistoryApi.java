package io.swagger.client.api;

import io.swagger.client.ApiClient;

import org.threeten.bp.OffsetDateTime;
import io.swagger.client.model.PaginatedListKeywordCount;

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
@Component("io.swagger.client.api.KeywordSearchHistoryApi")
public class KeywordSearchHistoryApi {
    private ApiClient apiClient;

    public KeywordSearchHistoryApi() {
        this(new ApiClient());
    }

    @Autowired
    public KeywordSearchHistoryApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Get keywords and how many times they occurred in a specified UTC date range
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - No keywords found
     * @param startDate start of UTC date range eg 1965-05-15              Defaults to 10 years ago
     * @param endDate end of UTC date range eg 2006-02-20              Defaults to 10 years from now
     * @param pageIndex 1-based index of page to return.  Defaults to 1
     * @param pageSize number of items per page.  Defaults to 20
     * @return PaginatedListKeywordCount
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public PaginatedListKeywordCount apiPorcelainKeywordSearchHistoryGet(OffsetDateTime startDate, OffsetDateTime endDate, Integer pageIndex, Integer pageSize) throws RestClientException {
        Object postBody = null;
        
        String path = UriComponentsBuilder.fromPath("/api/porcelain/KeywordSearchHistory").build().toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "startDate", startDate));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "endDate", endDate));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "pageIndex", pageIndex));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "pageSize", pageSize));

        final String[] accepts = { 
            "application/json"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basic", "oauth2" };

        ParameterizedTypeReference<PaginatedListKeywordCount> returnType = new ParameterizedTypeReference<PaginatedListKeywordCount>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
