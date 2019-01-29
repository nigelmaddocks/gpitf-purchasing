package io.swagger.client.api;

import io.swagger.client.ApiClient;

import java.io.File;
import io.swagger.client.model.FileResult;
import io.swagger.client.model.PaginatedListBlobInfo;

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
@Component("io.swagger.client.api.StandardsApplicableEvidenceBlobStoreApi")
public class StandardsApplicableEvidenceBlobStoreApi {
    private ApiClient apiClient;

    public StandardsApplicableEvidenceBlobStoreApi() {
        this(new ApiClient());
    }

    @Autowired
    public StandardsApplicableEvidenceBlobStoreApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Upload a file to support a claim  If the file already exists on the server, then a new version is created
     * Server side folder structure is something like:  --Organisation  ----Solution  ------Capability Evidence  --------Appointment Management - Citizen  --------Appointment Management - GP  --------Clinical Decision Support  --------[all other claimed capabilities]  ----------Images  ----------PDF  ----------Videos  ----------RTM  ----------Misc                where subFolder is an optional folder under a claimed capability ie Images, PDF, et al
     * <p><b>200</b> - Success
     * <p><b>404</b> - Claim not found in CRM
     * @param claimId Unique identifier of claim
     * @param file Client file path
     * @param filename Server file name
     * @param subFolder optional sub-folder
     * @return String
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public String apiStandardsApplicableEvidenceBlobStoreAddEvidenceForClaimPost(String claimId, File file, String filename, String subFolder) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'claimId' is set
        if (claimId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'claimId' when calling apiStandardsApplicableEvidenceBlobStoreAddEvidenceForClaimPost");
        }
        
        // verify the required parameter 'file' is set
        if (file == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'file' when calling apiStandardsApplicableEvidenceBlobStoreAddEvidenceForClaimPost");
        }
        
        // verify the required parameter 'filename' is set
        if (filename == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'filename' when calling apiStandardsApplicableEvidenceBlobStoreAddEvidenceForClaimPost");
        }
        
        String path = UriComponentsBuilder.fromPath("/api/StandardsApplicableEvidenceBlobStore/AddEvidenceForClaim").build().toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        
        if (claimId != null)
            formParams.add("claimId", claimId);
        if (file != null)
            formParams.add("file", new FileSystemResource(file));
        if (filename != null)
            formParams.add("filename", filename);
        if (subFolder != null)
            formParams.add("subFolder", subFolder);

        final String[] accepts = { 
            "application/json"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { 
            "multipart/form-data"
        };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basic", "oauth2" };

        ParameterizedTypeReference<String> returnType = new ParameterizedTypeReference<String>() {};
        return apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * Download a file which is supporting a claim
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - Claim not found in CRM
     * @param claimId unique identifier of solution claim
     * @param uniqueId unique identifier of file
     * @return FileResult
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public FileResult apiStandardsApplicableEvidenceBlobStoreDownloadByClaimIdPost(String claimId, String uniqueId) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'claimId' is set
        if (claimId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'claimId' when calling apiStandardsApplicableEvidenceBlobStoreDownloadByClaimIdPost");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("claimId", claimId);
        String path = UriComponentsBuilder.fromPath("/api/StandardsApplicableEvidenceBlobStore/Download/{claimId}").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "uniqueId", uniqueId));

        final String[] accepts = { 
            "text/plain", "application/json", "text/json"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basic", "oauth2" };

        ParameterizedTypeReference<FileResult> returnType = new ParameterizedTypeReference<FileResult>() {};
        return apiClient.invokeAPI(path, HttpMethod.POST, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
    /**
     * List all files and sub-folders for a claim including folder for claim
     * 
     * <p><b>200</b> - Success
     * <p><b>404</b> - Claim not found in CRM
     * @param claimId unique identifier of solution claim
     * @param subFolder optional sub-folder under claim
     * @param pageIndex 1-based index of page to return.  Defaults to 1
     * @param pageSize number of items per page.  Defaults to 20
     * @return PaginatedListBlobInfo
     * @throws RestClientException if an error occurs while attempting to invoke the API
     */
    public PaginatedListBlobInfo apiStandardsApplicableEvidenceBlobStoreEnumerateFolderByClaimIdGet(String claimId, String subFolder, Integer pageIndex, Integer pageSize) throws RestClientException {
        Object postBody = null;
        
        // verify the required parameter 'claimId' is set
        if (claimId == null) {
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "Missing the required parameter 'claimId' when calling apiStandardsApplicableEvidenceBlobStoreEnumerateFolderByClaimIdGet");
        }
        
        // create path and map variables
        final Map<String, Object> uriVariables = new HashMap<String, Object>();
        uriVariables.put("claimId", claimId);
        String path = UriComponentsBuilder.fromPath("/api/StandardsApplicableEvidenceBlobStore/EnumerateFolder/{claimId}").buildAndExpand(uriVariables).toUriString();
        
        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<String, String>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<String, Object>();
        
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "subFolder", subFolder));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "pageIndex", pageIndex));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "pageSize", pageSize));

        final String[] accepts = { 
            "application/json"
        };
        final List<MediaType> accept = apiClient.selectHeaderAccept(accepts);
        final String[] contentTypes = { };
        final MediaType contentType = apiClient.selectHeaderContentType(contentTypes);

        String[] authNames = new String[] { "basic", "oauth2" };

        ParameterizedTypeReference<PaginatedListBlobInfo> returnType = new ParameterizedTypeReference<PaginatedListBlobInfo>() {};
        return apiClient.invokeAPI(path, HttpMethod.GET, queryParams, postBody, headerParams, formParams, accept, contentType, authNames, returnType);
    }
}
