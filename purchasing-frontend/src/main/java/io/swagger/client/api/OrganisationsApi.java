package io.swagger.client.api;

import io.swagger.client.ApiClient;

import io.swagger.client.model.Organisations;

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
@Component("io.swagger.client.api.OrganisationsApi")
public class OrganisationsApi {
    private ApiClient apiClient;

    public OrganisationsApi() {
        this(new ApiClient());
    }

    @Autowired
    public OrganisationsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Retrieve an Organisation for the given Contact
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - Organisation not found
     * @param contactId CRM identifier of Contact
     * @return Organisations
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public Organisations apiOrganisationsByContactByContactIdGet(String contactId) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'contactId' is set
        if (contactId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'contactId' when calling apiOrganisationsByContactByContactIdGet");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("contactId", contactId);
        String path = UriComponentsBuilder.fromPath("/api/Organisations/ByContact/{contactId}").buildAndExpand(uriVariables).toUriString();
        
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

        ParameterizedTypeReference<Organisations> returnType = new ParameterizedTypeReference<Organisations>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
