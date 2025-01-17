/*
 * (C) Copyright IBM Corp. 2023.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package com.ibm.cloud.platform_services.iam_policy_management.v1;

import com.ibm.cloud.platform_services.iam_policy_management.v1.IamPolicyManagement;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.Control;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.ControlResponse;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.ControlResponseControl;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.ControlResponseControlWithEnrichedRoles;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.CreatePolicyOptions;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.CreateRoleOptions;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.CreateV2PolicyOptions;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.CustomRole;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.DeletePolicyOptions;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.DeleteRoleOptions;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.DeleteV2PolicyOptions;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.EnrichedRoles;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.GetPolicyOptions;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.GetRoleOptions;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.GetV2PolicyOptions;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.Grant;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.GrantWithEnrichedRoles;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.ListPoliciesOptions;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.ListRolesOptions;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.ListV2PoliciesOptions;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.Policy;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.PolicyList;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.PolicyResource;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.PolicyRole;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.PolicySubject;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.ReplacePolicyOptions;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.ReplaceRoleOptions;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.ReplaceV2PolicyOptions;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.ResourceAttribute;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.ResourceTag;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.Role;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.RoleAction;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.RoleList;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.Roles;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.RuleAttribute;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.SubjectAttribute;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.UpdatePolicyStateOptions;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.V2Policy;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.V2PolicyCollection;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.V2PolicyResource;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.V2PolicyResourceAttribute;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.V2PolicyResourceTag;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.V2PolicyRule;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.V2PolicyRuleRuleAttribute;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.V2PolicyRuleRuleWithConditions;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.V2PolicySubject;
import com.ibm.cloud.platform_services.iam_policy_management.v1.model.V2PolicySubjectAttribute;
import com.ibm.cloud.platform_services.iam_policy_management.v1.utils.TestUtilities;
import com.ibm.cloud.sdk.core.http.Response;
import com.ibm.cloud.sdk.core.security.Authenticator;
import com.ibm.cloud.sdk.core.security.NoAuthAuthenticator;
import com.ibm.cloud.sdk.core.service.model.FileWithMetadata;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;

/**
 * Unit test class for the IamPolicyManagement service.
 */
public class IamPolicyManagementTest {

  final HashMap<String, InputStream> mockStreamMap = TestUtilities.createMockStreamMap();
  final List<FileWithMetadata> mockListFileWithMetadata = TestUtilities.creatMockListFileWithMetadata();

  protected MockWebServer server;
  protected IamPolicyManagement iamPolicyManagementService;

  // Construct the service with a null authenticator (negative test)
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testConstructorWithNullAuthenticator() throws Throwable {
    final String serviceName = "testService";
    new IamPolicyManagement(serviceName, null);
  }

  // Test the listPolicies operation with a valid options model parameter
  @Test
  public void testListPoliciesWOptions() throws Throwable {
    // Register a mock response
    String mockResponseBody = "{\"policies\": [{\"id\": \"id\", \"type\": \"type\", \"description\": \"description\", \"subjects\": [{\"attributes\": [{\"name\": \"name\", \"value\": \"value\"}]}], \"roles\": [{\"role_id\": \"roleId\", \"display_name\": \"displayName\", \"description\": \"description\"}], \"resources\": [{\"attributes\": [{\"name\": \"name\", \"value\": \"value\", \"operator\": \"operator\"}], \"tags\": [{\"name\": \"name\", \"value\": \"value\", \"operator\": \"operator\"}]}], \"href\": \"href\", \"created_at\": \"2019-01-01T12:00:00.000Z\", \"created_by_id\": \"createdById\", \"last_modified_at\": \"2019-01-01T12:00:00.000Z\", \"last_modified_by_id\": \"lastModifiedById\", \"state\": \"active\"}]}";
    String listPoliciesPath = "/v1/policies";
    server.enqueue(new MockResponse()
      .setHeader("Content-type", "application/json")
      .setResponseCode(200)
      .setBody(mockResponseBody));

    // Construct an instance of the ListPoliciesOptions model
    ListPoliciesOptions listPoliciesOptionsModel = new ListPoliciesOptions.Builder()
      .accountId("testString")
      .acceptLanguage("default")
      .iamId("testString")
      .accessGroupId("testString")
      .type("access")
      .serviceType("service")
      .tagName("testString")
      .tagValue("testString")
      .sort("id")
      .format("include_last_permit")
      .state("active")
      .build();

    // Invoke listPolicies() with a valid options model and verify the result
    Response<PolicyList> response = iamPolicyManagementService.listPolicies(listPoliciesOptionsModel).execute();
    assertNotNull(response);
    PolicyList responseObj = response.getResult();
    assertNotNull(responseObj);

    // Verify the contents of the request sent to the mock server
    RecordedRequest request = server.takeRequest();
    assertNotNull(request);
    assertEquals(request.getMethod(), "GET");
    // Verify request path
    String parsedPath = TestUtilities.parseReqPath(request);
    assertEquals(parsedPath, listPoliciesPath);
    // Verify query params
    Map<String, String> query = TestUtilities.parseQueryString(request);
    assertNotNull(query);
    assertEquals(query.get("account_id"), "testString");
    assertEquals(query.get("iam_id"), "testString");
    assertEquals(query.get("access_group_id"), "testString");
    assertEquals(query.get("type"), "access");
    assertEquals(query.get("service_type"), "service");
    assertEquals(query.get("tag_name"), "testString");
    assertEquals(query.get("tag_value"), "testString");
    assertEquals(query.get("sort"), "id");
    assertEquals(query.get("format"), "include_last_permit");
    assertEquals(query.get("state"), "active");
  }

  // Test the listPolicies operation with and without retries enabled
  @Test
  public void testListPoliciesWRetries() throws Throwable {
    iamPolicyManagementService.enableRetries(4, 30);
    testListPoliciesWOptions();

    iamPolicyManagementService.disableRetries();
    testListPoliciesWOptions();
  }

  // Test the listPolicies operation with a null options model (negative test)
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testListPoliciesNoOptions() throws Throwable {
    server.enqueue(new MockResponse());
    iamPolicyManagementService.listPolicies(null).execute();
  }

  // Test the createPolicy operation with a valid options model parameter
  @Test
  public void testCreatePolicyWOptions() throws Throwable {
    // Register a mock response
    String mockResponseBody = "{\"id\": \"id\", \"type\": \"type\", \"description\": \"description\", \"subjects\": [{\"attributes\": [{\"name\": \"name\", \"value\": \"value\"}]}], \"roles\": [{\"role_id\": \"roleId\", \"display_name\": \"displayName\", \"description\": \"description\"}], \"resources\": [{\"attributes\": [{\"name\": \"name\", \"value\": \"value\", \"operator\": \"operator\"}], \"tags\": [{\"name\": \"name\", \"value\": \"value\", \"operator\": \"operator\"}]}], \"href\": \"href\", \"created_at\": \"2019-01-01T12:00:00.000Z\", \"created_by_id\": \"createdById\", \"last_modified_at\": \"2019-01-01T12:00:00.000Z\", \"last_modified_by_id\": \"lastModifiedById\", \"state\": \"active\"}";
    String createPolicyPath = "/v1/policies";
    server.enqueue(new MockResponse()
      .setHeader("Content-type", "application/json")
      .setResponseCode(201)
      .setBody(mockResponseBody));

    // Construct an instance of the SubjectAttribute model
    SubjectAttribute subjectAttributeModel = new SubjectAttribute.Builder()
      .name("testString")
      .value("testString")
      .build();

    // Construct an instance of the PolicySubject model
    PolicySubject policySubjectModel = new PolicySubject.Builder()
      .attributes(java.util.Arrays.asList(subjectAttributeModel))
      .build();

    // Construct an instance of the PolicyRole model
    PolicyRole policyRoleModel = new PolicyRole.Builder()
      .roleId("testString")
      .build();

    // Construct an instance of the ResourceAttribute model
    ResourceAttribute resourceAttributeModel = new ResourceAttribute.Builder()
      .name("testString")
      .value("testString")
      .operator("testString")
      .build();

    // Construct an instance of the ResourceTag model
    ResourceTag resourceTagModel = new ResourceTag.Builder()
      .name("testString")
      .value("testString")
      .operator("testString")
      .build();

    // Construct an instance of the PolicyResource model
    PolicyResource policyResourceModel = new PolicyResource.Builder()
      .attributes(java.util.Arrays.asList(resourceAttributeModel))
      .tags(java.util.Arrays.asList(resourceTagModel))
      .build();

    // Construct an instance of the CreatePolicyOptions model
    CreatePolicyOptions createPolicyOptionsModel = new CreatePolicyOptions.Builder()
      .type("testString")
      .subjects(java.util.Arrays.asList(policySubjectModel))
      .roles(java.util.Arrays.asList(policyRoleModel))
      .resources(java.util.Arrays.asList(policyResourceModel))
      .description("testString")
      .acceptLanguage("default")
      .build();

    // Invoke createPolicy() with a valid options model and verify the result
    Response<Policy> response = iamPolicyManagementService.createPolicy(createPolicyOptionsModel).execute();
    assertNotNull(response);
    Policy responseObj = response.getResult();
    assertNotNull(responseObj);

    // Verify the contents of the request sent to the mock server
    RecordedRequest request = server.takeRequest();
    assertNotNull(request);
    assertEquals(request.getMethod(), "POST");
    // Verify request path
    String parsedPath = TestUtilities.parseReqPath(request);
    assertEquals(parsedPath, createPolicyPath);
    // Verify that there is no query string
    Map<String, String> query = TestUtilities.parseQueryString(request);
    assertNull(query);
  }

  // Test the createPolicy operation with and without retries enabled
  @Test
  public void testCreatePolicyWRetries() throws Throwable {
    iamPolicyManagementService.enableRetries(4, 30);
    testCreatePolicyWOptions();

    iamPolicyManagementService.disableRetries();
    testCreatePolicyWOptions();
  }

  // Test the createPolicy operation with a null options model (negative test)
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testCreatePolicyNoOptions() throws Throwable {
    server.enqueue(new MockResponse());
    iamPolicyManagementService.createPolicy(null).execute();
  }

  // Test the replacePolicy operation with a valid options model parameter
  @Test
  public void testReplacePolicyWOptions() throws Throwable {
    // Register a mock response
    String mockResponseBody = "{\"id\": \"id\", \"type\": \"type\", \"description\": \"description\", \"subjects\": [{\"attributes\": [{\"name\": \"name\", \"value\": \"value\"}]}], \"roles\": [{\"role_id\": \"roleId\", \"display_name\": \"displayName\", \"description\": \"description\"}], \"resources\": [{\"attributes\": [{\"name\": \"name\", \"value\": \"value\", \"operator\": \"operator\"}], \"tags\": [{\"name\": \"name\", \"value\": \"value\", \"operator\": \"operator\"}]}], \"href\": \"href\", \"created_at\": \"2019-01-01T12:00:00.000Z\", \"created_by_id\": \"createdById\", \"last_modified_at\": \"2019-01-01T12:00:00.000Z\", \"last_modified_by_id\": \"lastModifiedById\", \"state\": \"active\"}";
    String replacePolicyPath = "/v1/policies/testString";
    server.enqueue(new MockResponse()
      .setHeader("Content-type", "application/json")
      .setResponseCode(200)
      .setBody(mockResponseBody));

    // Construct an instance of the SubjectAttribute model
    SubjectAttribute subjectAttributeModel = new SubjectAttribute.Builder()
      .name("testString")
      .value("testString")
      .build();

    // Construct an instance of the PolicySubject model
    PolicySubject policySubjectModel = new PolicySubject.Builder()
      .attributes(java.util.Arrays.asList(subjectAttributeModel))
      .build();

    // Construct an instance of the PolicyRole model
    PolicyRole policyRoleModel = new PolicyRole.Builder()
      .roleId("testString")
      .build();

    // Construct an instance of the ResourceAttribute model
    ResourceAttribute resourceAttributeModel = new ResourceAttribute.Builder()
      .name("testString")
      .value("testString")
      .operator("testString")
      .build();

    // Construct an instance of the ResourceTag model
    ResourceTag resourceTagModel = new ResourceTag.Builder()
      .name("testString")
      .value("testString")
      .operator("testString")
      .build();

    // Construct an instance of the PolicyResource model
    PolicyResource policyResourceModel = new PolicyResource.Builder()
      .attributes(java.util.Arrays.asList(resourceAttributeModel))
      .tags(java.util.Arrays.asList(resourceTagModel))
      .build();

    // Construct an instance of the ReplacePolicyOptions model
    ReplacePolicyOptions replacePolicyOptionsModel = new ReplacePolicyOptions.Builder()
      .policyId("testString")
      .ifMatch("testString")
      .type("testString")
      .subjects(java.util.Arrays.asList(policySubjectModel))
      .roles(java.util.Arrays.asList(policyRoleModel))
      .resources(java.util.Arrays.asList(policyResourceModel))
      .description("testString")
      .build();

    // Invoke replacePolicy() with a valid options model and verify the result
    Response<Policy> response = iamPolicyManagementService.replacePolicy(replacePolicyOptionsModel).execute();
    assertNotNull(response);
    Policy responseObj = response.getResult();
    assertNotNull(responseObj);

    // Verify the contents of the request sent to the mock server
    RecordedRequest request = server.takeRequest();
    assertNotNull(request);
    assertEquals(request.getMethod(), "PUT");
    // Verify request path
    String parsedPath = TestUtilities.parseReqPath(request);
    assertEquals(parsedPath, replacePolicyPath);
    // Verify header parameters
    assertEquals(request.getHeader("If-Match"), "testString");
    // Verify that there is no query string
    Map<String, String> query = TestUtilities.parseQueryString(request);
    assertNull(query);
  }

  // Test the replacePolicy operation with and without retries enabled
  @Test
  public void testReplacePolicyWRetries() throws Throwable {
    iamPolicyManagementService.enableRetries(4, 30);
    testReplacePolicyWOptions();

    iamPolicyManagementService.disableRetries();
    testReplacePolicyWOptions();
  }

  // Test the replacePolicy operation with a null options model (negative test)
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testReplacePolicyNoOptions() throws Throwable {
    server.enqueue(new MockResponse());
    iamPolicyManagementService.replacePolicy(null).execute();
  }

  // Test the getPolicy operation with a valid options model parameter
  @Test
  public void testGetPolicyWOptions() throws Throwable {
    // Register a mock response
    String mockResponseBody = "{\"id\": \"id\", \"type\": \"type\", \"description\": \"description\", \"subjects\": [{\"attributes\": [{\"name\": \"name\", \"value\": \"value\"}]}], \"roles\": [{\"role_id\": \"roleId\", \"display_name\": \"displayName\", \"description\": \"description\"}], \"resources\": [{\"attributes\": [{\"name\": \"name\", \"value\": \"value\", \"operator\": \"operator\"}], \"tags\": [{\"name\": \"name\", \"value\": \"value\", \"operator\": \"operator\"}]}], \"href\": \"href\", \"created_at\": \"2019-01-01T12:00:00.000Z\", \"created_by_id\": \"createdById\", \"last_modified_at\": \"2019-01-01T12:00:00.000Z\", \"last_modified_by_id\": \"lastModifiedById\", \"state\": \"active\"}";
    String getPolicyPath = "/v1/policies/testString";
    server.enqueue(new MockResponse()
      .setHeader("Content-type", "application/json")
      .setResponseCode(200)
      .setBody(mockResponseBody));

    // Construct an instance of the GetPolicyOptions model
    GetPolicyOptions getPolicyOptionsModel = new GetPolicyOptions.Builder()
      .policyId("testString")
      .build();

    // Invoke getPolicy() with a valid options model and verify the result
    Response<Policy> response = iamPolicyManagementService.getPolicy(getPolicyOptionsModel).execute();
    assertNotNull(response);
    Policy responseObj = response.getResult();
    assertNotNull(responseObj);

    // Verify the contents of the request sent to the mock server
    RecordedRequest request = server.takeRequest();
    assertNotNull(request);
    assertEquals(request.getMethod(), "GET");
    // Verify request path
    String parsedPath = TestUtilities.parseReqPath(request);
    assertEquals(parsedPath, getPolicyPath);
    // Verify that there is no query string
    Map<String, String> query = TestUtilities.parseQueryString(request);
    assertNull(query);
  }

  // Test the getPolicy operation with and without retries enabled
  @Test
  public void testGetPolicyWRetries() throws Throwable {
    iamPolicyManagementService.enableRetries(4, 30);
    testGetPolicyWOptions();

    iamPolicyManagementService.disableRetries();
    testGetPolicyWOptions();
  }

  // Test the getPolicy operation with a null options model (negative test)
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testGetPolicyNoOptions() throws Throwable {
    server.enqueue(new MockResponse());
    iamPolicyManagementService.getPolicy(null).execute();
  }

  // Test the deletePolicy operation with a valid options model parameter
  @Test
  public void testDeletePolicyWOptions() throws Throwable {
    // Register a mock response
    String mockResponseBody = "";
    String deletePolicyPath = "/v1/policies/testString";
    server.enqueue(new MockResponse()
      .setResponseCode(204)
      .setBody(mockResponseBody));

    // Construct an instance of the DeletePolicyOptions model
    DeletePolicyOptions deletePolicyOptionsModel = new DeletePolicyOptions.Builder()
      .policyId("testString")
      .build();

    // Invoke deletePolicy() with a valid options model and verify the result
    Response<Void> response = iamPolicyManagementService.deletePolicy(deletePolicyOptionsModel).execute();
    assertNotNull(response);
    Void responseObj = response.getResult();
    assertNull(responseObj);

    // Verify the contents of the request sent to the mock server
    RecordedRequest request = server.takeRequest();
    assertNotNull(request);
    assertEquals(request.getMethod(), "DELETE");
    // Verify request path
    String parsedPath = TestUtilities.parseReqPath(request);
    assertEquals(parsedPath, deletePolicyPath);
    // Verify that there is no query string
    Map<String, String> query = TestUtilities.parseQueryString(request);
    assertNull(query);
  }

  // Test the deletePolicy operation with and without retries enabled
  @Test
  public void testDeletePolicyWRetries() throws Throwable {
    iamPolicyManagementService.enableRetries(4, 30);
    testDeletePolicyWOptions();

    iamPolicyManagementService.disableRetries();
    testDeletePolicyWOptions();
  }

  // Test the deletePolicy operation with a null options model (negative test)
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testDeletePolicyNoOptions() throws Throwable {
    server.enqueue(new MockResponse());
    iamPolicyManagementService.deletePolicy(null).execute();
  }

  // Test the updatePolicyState operation with a valid options model parameter
  @Test
  public void testUpdatePolicyStateWOptions() throws Throwable {
    // Register a mock response
    String mockResponseBody = "{\"id\": \"id\", \"type\": \"type\", \"description\": \"description\", \"subjects\": [{\"attributes\": [{\"name\": \"name\", \"value\": \"value\"}]}], \"roles\": [{\"role_id\": \"roleId\", \"display_name\": \"displayName\", \"description\": \"description\"}], \"resources\": [{\"attributes\": [{\"name\": \"name\", \"value\": \"value\", \"operator\": \"operator\"}], \"tags\": [{\"name\": \"name\", \"value\": \"value\", \"operator\": \"operator\"}]}], \"href\": \"href\", \"created_at\": \"2019-01-01T12:00:00.000Z\", \"created_by_id\": \"createdById\", \"last_modified_at\": \"2019-01-01T12:00:00.000Z\", \"last_modified_by_id\": \"lastModifiedById\", \"state\": \"active\"}";
    String updatePolicyStatePath = "/v1/policies/testString";
    server.enqueue(new MockResponse()
      .setHeader("Content-type", "application/json")
      .setResponseCode(200)
      .setBody(mockResponseBody));

    // Construct an instance of the UpdatePolicyStateOptions model
    UpdatePolicyStateOptions updatePolicyStateOptionsModel = new UpdatePolicyStateOptions.Builder()
      .policyId("testString")
      .ifMatch("testString")
      .state("active")
      .build();

    // Invoke updatePolicyState() with a valid options model and verify the result
    Response<Policy> response = iamPolicyManagementService.updatePolicyState(updatePolicyStateOptionsModel).execute();
    assertNotNull(response);
    Policy responseObj = response.getResult();
    assertNotNull(responseObj);

    // Verify the contents of the request sent to the mock server
    RecordedRequest request = server.takeRequest();
    assertNotNull(request);
    assertEquals(request.getMethod(), "PATCH");
    // Verify request path
    String parsedPath = TestUtilities.parseReqPath(request);
    assertEquals(parsedPath, updatePolicyStatePath);
    // Verify header parameters
    assertEquals(request.getHeader("If-Match"), "testString");
    // Verify that there is no query string
    Map<String, String> query = TestUtilities.parseQueryString(request);
    assertNull(query);
  }

  // Test the updatePolicyState operation with and without retries enabled
  @Test
  public void testUpdatePolicyStateWRetries() throws Throwable {
    iamPolicyManagementService.enableRetries(4, 30);
    testUpdatePolicyStateWOptions();

    iamPolicyManagementService.disableRetries();
    testUpdatePolicyStateWOptions();
  }

  // Test the updatePolicyState operation with a null options model (negative test)
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testUpdatePolicyStateNoOptions() throws Throwable {
    server.enqueue(new MockResponse());
    iamPolicyManagementService.updatePolicyState(null).execute();
  }

  // Test the listRoles operation with a valid options model parameter
  @Test
  public void testListRolesWOptions() throws Throwable {
    // Register a mock response
    String mockResponseBody = "{\"custom_roles\": [{\"id\": \"id\", \"display_name\": \"displayName\", \"description\": \"description\", \"actions\": [\"actions\"], \"crn\": \"crn\", \"name\": \"Developer\", \"account_id\": \"accountId\", \"service_name\": \"iam-groups\", \"created_at\": \"2019-01-01T12:00:00.000Z\", \"created_by_id\": \"createdById\", \"last_modified_at\": \"2019-01-01T12:00:00.000Z\", \"last_modified_by_id\": \"lastModifiedById\", \"href\": \"href\"}], \"service_roles\": [{\"display_name\": \"displayName\", \"description\": \"description\", \"actions\": [\"actions\"], \"crn\": \"crn\"}], \"system_roles\": [{\"display_name\": \"displayName\", \"description\": \"description\", \"actions\": [\"actions\"], \"crn\": \"crn\"}]}";
    String listRolesPath = "/v2/roles";
    server.enqueue(new MockResponse()
      .setHeader("Content-type", "application/json")
      .setResponseCode(200)
      .setBody(mockResponseBody));

    // Construct an instance of the ListRolesOptions model
    ListRolesOptions listRolesOptionsModel = new ListRolesOptions.Builder()
      .acceptLanguage("default")
      .accountId("testString")
      .serviceName("iam-groups")
      .sourceServiceName("iam-groups")
      .policyType("authorization")
      .serviceGroupId("IAM")
      .build();

    // Invoke listRoles() with a valid options model and verify the result
    Response<RoleList> response = iamPolicyManagementService.listRoles(listRolesOptionsModel).execute();
    assertNotNull(response);
    RoleList responseObj = response.getResult();
    assertNotNull(responseObj);

    // Verify the contents of the request sent to the mock server
    RecordedRequest request = server.takeRequest();
    assertNotNull(request);
    assertEquals(request.getMethod(), "GET");
    // Verify request path
    String parsedPath = TestUtilities.parseReqPath(request);
    assertEquals(parsedPath, listRolesPath);
    // Verify query params
    Map<String, String> query = TestUtilities.parseQueryString(request);
    assertNotNull(query);
    assertEquals(query.get("account_id"), "testString");
    assertEquals(query.get("service_name"), "iam-groups");
    assertEquals(query.get("source_service_name"), "iam-groups");
    assertEquals(query.get("policy_type"), "authorization");
    assertEquals(query.get("service_group_id"), "IAM");
  }

  // Test the listRoles operation with and without retries enabled
  @Test
  public void testListRolesWRetries() throws Throwable {
    iamPolicyManagementService.enableRetries(4, 30);
    testListRolesWOptions();

    iamPolicyManagementService.disableRetries();
    testListRolesWOptions();
  }

  // Test the createRole operation with a valid options model parameter
  @Test
  public void testCreateRoleWOptions() throws Throwable {
    // Register a mock response
    String mockResponseBody = "{\"id\": \"id\", \"display_name\": \"displayName\", \"description\": \"description\", \"actions\": [\"actions\"], \"crn\": \"crn\", \"name\": \"Developer\", \"account_id\": \"accountId\", \"service_name\": \"iam-groups\", \"created_at\": \"2019-01-01T12:00:00.000Z\", \"created_by_id\": \"createdById\", \"last_modified_at\": \"2019-01-01T12:00:00.000Z\", \"last_modified_by_id\": \"lastModifiedById\", \"href\": \"href\"}";
    String createRolePath = "/v2/roles";
    server.enqueue(new MockResponse()
      .setHeader("Content-type", "application/json")
      .setResponseCode(201)
      .setBody(mockResponseBody));

    // Construct an instance of the CreateRoleOptions model
    CreateRoleOptions createRoleOptionsModel = new CreateRoleOptions.Builder()
      .displayName("testString")
      .actions(java.util.Arrays.asList("testString"))
      .name("Developer")
      .accountId("testString")
      .serviceName("iam-groups")
      .description("testString")
      .acceptLanguage("default")
      .build();

    // Invoke createRole() with a valid options model and verify the result
    Response<CustomRole> response = iamPolicyManagementService.createRole(createRoleOptionsModel).execute();
    assertNotNull(response);
    CustomRole responseObj = response.getResult();
    assertNotNull(responseObj);

    // Verify the contents of the request sent to the mock server
    RecordedRequest request = server.takeRequest();
    assertNotNull(request);
    assertEquals(request.getMethod(), "POST");
    // Verify request path
    String parsedPath = TestUtilities.parseReqPath(request);
    assertEquals(parsedPath, createRolePath);
    // Verify that there is no query string
    Map<String, String> query = TestUtilities.parseQueryString(request);
    assertNull(query);
  }

  // Test the createRole operation with and without retries enabled
  @Test
  public void testCreateRoleWRetries() throws Throwable {
    iamPolicyManagementService.enableRetries(4, 30);
    testCreateRoleWOptions();

    iamPolicyManagementService.disableRetries();
    testCreateRoleWOptions();
  }

  // Test the createRole operation with a null options model (negative test)
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testCreateRoleNoOptions() throws Throwable {
    server.enqueue(new MockResponse());
    iamPolicyManagementService.createRole(null).execute();
  }

  // Test the replaceRole operation with a valid options model parameter
  @Test
  public void testReplaceRoleWOptions() throws Throwable {
    // Register a mock response
    String mockResponseBody = "{\"id\": \"id\", \"display_name\": \"displayName\", \"description\": \"description\", \"actions\": [\"actions\"], \"crn\": \"crn\", \"name\": \"Developer\", \"account_id\": \"accountId\", \"service_name\": \"iam-groups\", \"created_at\": \"2019-01-01T12:00:00.000Z\", \"created_by_id\": \"createdById\", \"last_modified_at\": \"2019-01-01T12:00:00.000Z\", \"last_modified_by_id\": \"lastModifiedById\", \"href\": \"href\"}";
    String replaceRolePath = "/v2/roles/testString";
    server.enqueue(new MockResponse()
      .setHeader("Content-type", "application/json")
      .setResponseCode(200)
      .setBody(mockResponseBody));

    // Construct an instance of the ReplaceRoleOptions model
    ReplaceRoleOptions replaceRoleOptionsModel = new ReplaceRoleOptions.Builder()
      .roleId("testString")
      .ifMatch("testString")
      .displayName("testString")
      .actions(java.util.Arrays.asList("testString"))
      .description("testString")
      .build();

    // Invoke replaceRole() with a valid options model and verify the result
    Response<CustomRole> response = iamPolicyManagementService.replaceRole(replaceRoleOptionsModel).execute();
    assertNotNull(response);
    CustomRole responseObj = response.getResult();
    assertNotNull(responseObj);

    // Verify the contents of the request sent to the mock server
    RecordedRequest request = server.takeRequest();
    assertNotNull(request);
    assertEquals(request.getMethod(), "PUT");
    // Verify request path
    String parsedPath = TestUtilities.parseReqPath(request);
    assertEquals(parsedPath, replaceRolePath);
    // Verify header parameters
    assertEquals(request.getHeader("If-Match"), "testString");
    // Verify that there is no query string
    Map<String, String> query = TestUtilities.parseQueryString(request);
    assertNull(query);
  }

  // Test the replaceRole operation with and without retries enabled
  @Test
  public void testReplaceRoleWRetries() throws Throwable {
    iamPolicyManagementService.enableRetries(4, 30);
    testReplaceRoleWOptions();

    iamPolicyManagementService.disableRetries();
    testReplaceRoleWOptions();
  }

  // Test the replaceRole operation with a null options model (negative test)
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testReplaceRoleNoOptions() throws Throwable {
    server.enqueue(new MockResponse());
    iamPolicyManagementService.replaceRole(null).execute();
  }

  // Test the getRole operation with a valid options model parameter
  @Test
  public void testGetRoleWOptions() throws Throwable {
    // Register a mock response
    String mockResponseBody = "{\"id\": \"id\", \"display_name\": \"displayName\", \"description\": \"description\", \"actions\": [\"actions\"], \"crn\": \"crn\", \"name\": \"Developer\", \"account_id\": \"accountId\", \"service_name\": \"iam-groups\", \"created_at\": \"2019-01-01T12:00:00.000Z\", \"created_by_id\": \"createdById\", \"last_modified_at\": \"2019-01-01T12:00:00.000Z\", \"last_modified_by_id\": \"lastModifiedById\", \"href\": \"href\"}";
    String getRolePath = "/v2/roles/testString";
    server.enqueue(new MockResponse()
      .setHeader("Content-type", "application/json")
      .setResponseCode(200)
      .setBody(mockResponseBody));

    // Construct an instance of the GetRoleOptions model
    GetRoleOptions getRoleOptionsModel = new GetRoleOptions.Builder()
      .roleId("testString")
      .build();

    // Invoke getRole() with a valid options model and verify the result
    Response<CustomRole> response = iamPolicyManagementService.getRole(getRoleOptionsModel).execute();
    assertNotNull(response);
    CustomRole responseObj = response.getResult();
    assertNotNull(responseObj);

    // Verify the contents of the request sent to the mock server
    RecordedRequest request = server.takeRequest();
    assertNotNull(request);
    assertEquals(request.getMethod(), "GET");
    // Verify request path
    String parsedPath = TestUtilities.parseReqPath(request);
    assertEquals(parsedPath, getRolePath);
    // Verify that there is no query string
    Map<String, String> query = TestUtilities.parseQueryString(request);
    assertNull(query);
  }

  // Test the getRole operation with and without retries enabled
  @Test
  public void testGetRoleWRetries() throws Throwable {
    iamPolicyManagementService.enableRetries(4, 30);
    testGetRoleWOptions();

    iamPolicyManagementService.disableRetries();
    testGetRoleWOptions();
  }

  // Test the getRole operation with a null options model (negative test)
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testGetRoleNoOptions() throws Throwable {
    server.enqueue(new MockResponse());
    iamPolicyManagementService.getRole(null).execute();
  }

  // Test the deleteRole operation with a valid options model parameter
  @Test
  public void testDeleteRoleWOptions() throws Throwable {
    // Register a mock response
    String mockResponseBody = "";
    String deleteRolePath = "/v2/roles/testString";
    server.enqueue(new MockResponse()
      .setResponseCode(204)
      .setBody(mockResponseBody));

    // Construct an instance of the DeleteRoleOptions model
    DeleteRoleOptions deleteRoleOptionsModel = new DeleteRoleOptions.Builder()
      .roleId("testString")
      .build();

    // Invoke deleteRole() with a valid options model and verify the result
    Response<Void> response = iamPolicyManagementService.deleteRole(deleteRoleOptionsModel).execute();
    assertNotNull(response);
    Void responseObj = response.getResult();
    assertNull(responseObj);

    // Verify the contents of the request sent to the mock server
    RecordedRequest request = server.takeRequest();
    assertNotNull(request);
    assertEquals(request.getMethod(), "DELETE");
    // Verify request path
    String parsedPath = TestUtilities.parseReqPath(request);
    assertEquals(parsedPath, deleteRolePath);
    // Verify that there is no query string
    Map<String, String> query = TestUtilities.parseQueryString(request);
    assertNull(query);
  }

  // Test the deleteRole operation with and without retries enabled
  @Test
  public void testDeleteRoleWRetries() throws Throwable {
    iamPolicyManagementService.enableRetries(4, 30);
    testDeleteRoleWOptions();

    iamPolicyManagementService.disableRetries();
    testDeleteRoleWOptions();
  }

  // Test the deleteRole operation with a null options model (negative test)
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testDeleteRoleNoOptions() throws Throwable {
    server.enqueue(new MockResponse());
    iamPolicyManagementService.deleteRole(null).execute();
  }

  // Test the listV2Policies operation with a valid options model parameter
  @Test
  public void testListV2PoliciesWOptions() throws Throwable {
    // Register a mock response
    String mockResponseBody = "{\"policies\": [{\"type\": \"access\", \"description\": \"description\", \"subject\": {\"attributes\": [{\"key\": \"key\", \"operator\": \"stringEquals\", \"value\": \"value\"}]}, \"resource\": {\"attributes\": [{\"key\": \"key\", \"operator\": \"stringEquals\", \"value\": \"anyValue\"}], \"tags\": [{\"key\": \"key\", \"value\": \"value\", \"operator\": \"stringEquals\"}]}, \"pattern\": \"pattern\", \"rule\": {\"key\": \"key\", \"operator\": \"timeLessThan\", \"value\": \"anyValue\"}, \"id\": \"id\", \"href\": \"href\", \"control\": {\"grant\": {\"roles\": [{\"role_id\": \"roleId\"}]}}, \"created_at\": \"2019-01-01T12:00:00.000Z\", \"created_by_id\": \"createdById\", \"last_modified_at\": \"2019-01-01T12:00:00.000Z\", \"last_modified_by_id\": \"lastModifiedById\", \"state\": \"active\", \"last_permit_at\": \"lastPermitAt\", \"last_permit_frequency\": 19}]}";
    String listV2PoliciesPath = "/v2/policies";
    server.enqueue(new MockResponse()
      .setHeader("Content-type", "application/json")
      .setResponseCode(200)
      .setBody(mockResponseBody));

    // Construct an instance of the ListV2PoliciesOptions model
    ListV2PoliciesOptions listV2PoliciesOptionsModel = new ListV2PoliciesOptions.Builder()
      .accountId("testString")
      .acceptLanguage("default")
      .iamId("testString")
      .accessGroupId("testString")
      .type("access")
      .serviceType("service")
      .serviceName("testString")
      .serviceGroupId("testString")
      .sort("testString")
      .format("include_last_permit")
      .state("active")
      .build();

    // Invoke listV2Policies() with a valid options model and verify the result
    Response<V2PolicyCollection> response = iamPolicyManagementService.listV2Policies(listV2PoliciesOptionsModel).execute();
    assertNotNull(response);
    V2PolicyCollection responseObj = response.getResult();
    assertNotNull(responseObj);

    // Verify the contents of the request sent to the mock server
    RecordedRequest request = server.takeRequest();
    assertNotNull(request);
    assertEquals(request.getMethod(), "GET");
    // Verify request path
    String parsedPath = TestUtilities.parseReqPath(request);
    assertEquals(parsedPath, listV2PoliciesPath);
    // Verify query params
    Map<String, String> query = TestUtilities.parseQueryString(request);
    assertNotNull(query);
    assertEquals(query.get("account_id"), "testString");
    assertEquals(query.get("iam_id"), "testString");
    assertEquals(query.get("access_group_id"), "testString");
    assertEquals(query.get("type"), "access");
    assertEquals(query.get("service_type"), "service");
    assertEquals(query.get("service_name"), "testString");
    assertEquals(query.get("service_group_id"), "testString");
    assertEquals(query.get("sort"), "testString");
    assertEquals(query.get("format"), "include_last_permit");
    assertEquals(query.get("state"), "active");
  }

  // Test the listV2Policies operation with and without retries enabled
  @Test
  public void testListV2PoliciesWRetries() throws Throwable {
    iamPolicyManagementService.enableRetries(4, 30);
    testListV2PoliciesWOptions();

    iamPolicyManagementService.disableRetries();
    testListV2PoliciesWOptions();
  }

  // Test the listV2Policies operation with a null options model (negative test)
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testListV2PoliciesNoOptions() throws Throwable {
    server.enqueue(new MockResponse());
    iamPolicyManagementService.listV2Policies(null).execute();
  }

  // Test the createV2Policy operation with a valid options model parameter
  @Test
  public void testCreateV2PolicyWOptions() throws Throwable {
    // Register a mock response
    String mockResponseBody = "{\"type\": \"access\", \"description\": \"description\", \"subject\": {\"attributes\": [{\"key\": \"key\", \"operator\": \"stringEquals\", \"value\": \"value\"}]}, \"resource\": {\"attributes\": [{\"key\": \"key\", \"operator\": \"stringEquals\", \"value\": \"anyValue\"}], \"tags\": [{\"key\": \"key\", \"value\": \"value\", \"operator\": \"stringEquals\"}]}, \"pattern\": \"pattern\", \"rule\": {\"key\": \"key\", \"operator\": \"timeLessThan\", \"value\": \"anyValue\"}, \"id\": \"id\", \"href\": \"href\", \"control\": {\"grant\": {\"roles\": [{\"role_id\": \"roleId\"}]}}, \"created_at\": \"2019-01-01T12:00:00.000Z\", \"created_by_id\": \"createdById\", \"last_modified_at\": \"2019-01-01T12:00:00.000Z\", \"last_modified_by_id\": \"lastModifiedById\", \"state\": \"active\", \"last_permit_at\": \"lastPermitAt\", \"last_permit_frequency\": 19}";
    String createV2PolicyPath = "/v2/policies";
    server.enqueue(new MockResponse()
      .setHeader("Content-type", "application/json")
      .setResponseCode(201)
      .setBody(mockResponseBody));

    // Construct an instance of the Roles model
    Roles rolesModel = new Roles.Builder()
      .roleId("testString")
      .build();

    // Construct an instance of the Grant model
    Grant grantModel = new Grant.Builder()
      .roles(java.util.Arrays.asList(rolesModel))
      .build();

    // Construct an instance of the Control model
    Control controlModel = new Control.Builder()
      .grant(grantModel)
      .build();

    // Construct an instance of the V2PolicySubjectAttribute model
    V2PolicySubjectAttribute v2PolicySubjectAttributeModel = new V2PolicySubjectAttribute.Builder()
      .key("testString")
      .operator("stringEquals")
      .value("testString")
      .build();

    // Construct an instance of the V2PolicySubject model
    V2PolicySubject v2PolicySubjectModel = new V2PolicySubject.Builder()
      .attributes(java.util.Arrays.asList(v2PolicySubjectAttributeModel))
      .build();

    // Construct an instance of the V2PolicyResourceAttribute model
    V2PolicyResourceAttribute v2PolicyResourceAttributeModel = new V2PolicyResourceAttribute.Builder()
      .key("testString")
      .operator("stringEquals")
      .value("testString")
      .build();

    // Construct an instance of the V2PolicyResourceTag model
    V2PolicyResourceTag v2PolicyResourceTagModel = new V2PolicyResourceTag.Builder()
      .key("testString")
      .value("testString")
      .operator("stringEquals")
      .build();

    // Construct an instance of the V2PolicyResource model
    V2PolicyResource v2PolicyResourceModel = new V2PolicyResource.Builder()
      .attributes(java.util.Arrays.asList(v2PolicyResourceAttributeModel))
      .tags(java.util.Arrays.asList(v2PolicyResourceTagModel))
      .build();

    // Construct an instance of the V2PolicyRuleRuleAttribute model
    V2PolicyRuleRuleAttribute v2PolicyRuleModel = new V2PolicyRuleRuleAttribute.Builder()
      .key("testString")
      .operator("timeLessThan")
      .value("testString")
      .build();

    // Construct an instance of the CreateV2PolicyOptions model
    CreateV2PolicyOptions createV2PolicyOptionsModel = new CreateV2PolicyOptions.Builder()
      .control(controlModel)
      .type("access")
      .description("testString")
      .subject(v2PolicySubjectModel)
      .resource(v2PolicyResourceModel)
      .pattern("testString")
      .rule(v2PolicyRuleModel)
      .acceptLanguage("default")
      .build();

    // Invoke createV2Policy() with a valid options model and verify the result
    Response<V2Policy> response = iamPolicyManagementService.createV2Policy(createV2PolicyOptionsModel).execute();
    assertNotNull(response);
    V2Policy responseObj = response.getResult();
    assertNotNull(responseObj);

    // Verify the contents of the request sent to the mock server
    RecordedRequest request = server.takeRequest();
    assertNotNull(request);
    assertEquals(request.getMethod(), "POST");
    // Verify request path
    String parsedPath = TestUtilities.parseReqPath(request);
    assertEquals(parsedPath, createV2PolicyPath);
    // Verify that there is no query string
    Map<String, String> query = TestUtilities.parseQueryString(request);
    assertNull(query);
  }

  // Test the createV2Policy operation with and without retries enabled
  @Test
  public void testCreateV2PolicyWRetries() throws Throwable {
    iamPolicyManagementService.enableRetries(4, 30);
    testCreateV2PolicyWOptions();

    iamPolicyManagementService.disableRetries();
    testCreateV2PolicyWOptions();
  }

  // Test the createV2Policy operation with a null options model (negative test)
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testCreateV2PolicyNoOptions() throws Throwable {
    server.enqueue(new MockResponse());
    iamPolicyManagementService.createV2Policy(null).execute();
  }

  // Test the replaceV2Policy operation with a valid options model parameter
  @Test
  public void testReplaceV2PolicyWOptions() throws Throwable {
    // Register a mock response
    String mockResponseBody = "{\"type\": \"access\", \"description\": \"description\", \"subject\": {\"attributes\": [{\"key\": \"key\", \"operator\": \"stringEquals\", \"value\": \"value\"}]}, \"resource\": {\"attributes\": [{\"key\": \"key\", \"operator\": \"stringEquals\", \"value\": \"anyValue\"}], \"tags\": [{\"key\": \"key\", \"value\": \"value\", \"operator\": \"stringEquals\"}]}, \"pattern\": \"pattern\", \"rule\": {\"key\": \"key\", \"operator\": \"timeLessThan\", \"value\": \"anyValue\"}, \"id\": \"id\", \"href\": \"href\", \"control\": {\"grant\": {\"roles\": [{\"role_id\": \"roleId\"}]}}, \"created_at\": \"2019-01-01T12:00:00.000Z\", \"created_by_id\": \"createdById\", \"last_modified_at\": \"2019-01-01T12:00:00.000Z\", \"last_modified_by_id\": \"lastModifiedById\", \"state\": \"active\", \"last_permit_at\": \"lastPermitAt\", \"last_permit_frequency\": 19}";
    String replaceV2PolicyPath = "/v2/policies/testString";
    server.enqueue(new MockResponse()
      .setHeader("Content-type", "application/json")
      .setResponseCode(200)
      .setBody(mockResponseBody));

    // Construct an instance of the Roles model
    Roles rolesModel = new Roles.Builder()
      .roleId("testString")
      .build();

    // Construct an instance of the Grant model
    Grant grantModel = new Grant.Builder()
      .roles(java.util.Arrays.asList(rolesModel))
      .build();

    // Construct an instance of the Control model
    Control controlModel = new Control.Builder()
      .grant(grantModel)
      .build();

    // Construct an instance of the V2PolicySubjectAttribute model
    V2PolicySubjectAttribute v2PolicySubjectAttributeModel = new V2PolicySubjectAttribute.Builder()
      .key("testString")
      .operator("stringEquals")
      .value("testString")
      .build();

    // Construct an instance of the V2PolicySubject model
    V2PolicySubject v2PolicySubjectModel = new V2PolicySubject.Builder()
      .attributes(java.util.Arrays.asList(v2PolicySubjectAttributeModel))
      .build();

    // Construct an instance of the V2PolicyResourceAttribute model
    V2PolicyResourceAttribute v2PolicyResourceAttributeModel = new V2PolicyResourceAttribute.Builder()
      .key("testString")
      .operator("stringEquals")
      .value("testString")
      .build();

    // Construct an instance of the V2PolicyResourceTag model
    V2PolicyResourceTag v2PolicyResourceTagModel = new V2PolicyResourceTag.Builder()
      .key("testString")
      .value("testString")
      .operator("stringEquals")
      .build();

    // Construct an instance of the V2PolicyResource model
    V2PolicyResource v2PolicyResourceModel = new V2PolicyResource.Builder()
      .attributes(java.util.Arrays.asList(v2PolicyResourceAttributeModel))
      .tags(java.util.Arrays.asList(v2PolicyResourceTagModel))
      .build();

    // Construct an instance of the V2PolicyRuleRuleAttribute model
    V2PolicyRuleRuleAttribute v2PolicyRuleModel = new V2PolicyRuleRuleAttribute.Builder()
      .key("testString")
      .operator("timeLessThan")
      .value("testString")
      .build();

    // Construct an instance of the ReplaceV2PolicyOptions model
    ReplaceV2PolicyOptions replaceV2PolicyOptionsModel = new ReplaceV2PolicyOptions.Builder()
      .id("testString")
      .ifMatch("testString")
      .control(controlModel)
      .type("access")
      .description("testString")
      .subject(v2PolicySubjectModel)
      .resource(v2PolicyResourceModel)
      .pattern("testString")
      .rule(v2PolicyRuleModel)
      .build();

    // Invoke replaceV2Policy() with a valid options model and verify the result
    Response<V2Policy> response = iamPolicyManagementService.replaceV2Policy(replaceV2PolicyOptionsModel).execute();
    assertNotNull(response);
    V2Policy responseObj = response.getResult();
    assertNotNull(responseObj);

    // Verify the contents of the request sent to the mock server
    RecordedRequest request = server.takeRequest();
    assertNotNull(request);
    assertEquals(request.getMethod(), "PUT");
    // Verify request path
    String parsedPath = TestUtilities.parseReqPath(request);
    assertEquals(parsedPath, replaceV2PolicyPath);
    // Verify header parameters
    assertEquals(request.getHeader("If-Match"), "testString");
    // Verify that there is no query string
    Map<String, String> query = TestUtilities.parseQueryString(request);
    assertNull(query);
  }

  // Test the replaceV2Policy operation with and without retries enabled
  @Test
  public void testReplaceV2PolicyWRetries() throws Throwable {
    iamPolicyManagementService.enableRetries(4, 30);
    testReplaceV2PolicyWOptions();

    iamPolicyManagementService.disableRetries();
    testReplaceV2PolicyWOptions();
  }

  // Test the replaceV2Policy operation with a null options model (negative test)
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testReplaceV2PolicyNoOptions() throws Throwable {
    server.enqueue(new MockResponse());
    iamPolicyManagementService.replaceV2Policy(null).execute();
  }

  // Test the getV2Policy operation with a valid options model parameter
  @Test
  public void testGetV2PolicyWOptions() throws Throwable {
    // Register a mock response
    String mockResponseBody = "{\"type\": \"access\", \"description\": \"description\", \"subject\": {\"attributes\": [{\"key\": \"key\", \"operator\": \"stringEquals\", \"value\": \"value\"}]}, \"resource\": {\"attributes\": [{\"key\": \"key\", \"operator\": \"stringEquals\", \"value\": \"anyValue\"}], \"tags\": [{\"key\": \"key\", \"value\": \"value\", \"operator\": \"stringEquals\"}]}, \"pattern\": \"pattern\", \"rule\": {\"key\": \"key\", \"operator\": \"timeLessThan\", \"value\": \"anyValue\"}, \"id\": \"id\", \"href\": \"href\", \"control\": {\"grant\": {\"roles\": [{\"role_id\": \"roleId\"}]}}, \"created_at\": \"2019-01-01T12:00:00.000Z\", \"created_by_id\": \"createdById\", \"last_modified_at\": \"2019-01-01T12:00:00.000Z\", \"last_modified_by_id\": \"lastModifiedById\", \"state\": \"active\", \"last_permit_at\": \"lastPermitAt\", \"last_permit_frequency\": 19}";
    String getV2PolicyPath = "/v2/policies/testString";
    server.enqueue(new MockResponse()
      .setHeader("Content-type", "application/json")
      .setResponseCode(200)
      .setBody(mockResponseBody));

    // Construct an instance of the GetV2PolicyOptions model
    GetV2PolicyOptions getV2PolicyOptionsModel = new GetV2PolicyOptions.Builder()
      .id("testString")
      .format("include_last_permit")
      .build();

    // Invoke getV2Policy() with a valid options model and verify the result
    Response<V2Policy> response = iamPolicyManagementService.getV2Policy(getV2PolicyOptionsModel).execute();
    assertNotNull(response);
    V2Policy responseObj = response.getResult();
    assertNotNull(responseObj);

    // Verify the contents of the request sent to the mock server
    RecordedRequest request = server.takeRequest();
    assertNotNull(request);
    assertEquals(request.getMethod(), "GET");
    // Verify request path
    String parsedPath = TestUtilities.parseReqPath(request);
    assertEquals(parsedPath, getV2PolicyPath);
    // Verify query params
    Map<String, String> query = TestUtilities.parseQueryString(request);
    assertNotNull(query);
    assertEquals(query.get("format"), "include_last_permit");
  }

  // Test the getV2Policy operation with and without retries enabled
  @Test
  public void testGetV2PolicyWRetries() throws Throwable {
    iamPolicyManagementService.enableRetries(4, 30);
    testGetV2PolicyWOptions();

    iamPolicyManagementService.disableRetries();
    testGetV2PolicyWOptions();
  }

  // Test the getV2Policy operation with a null options model (negative test)
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testGetV2PolicyNoOptions() throws Throwable {
    server.enqueue(new MockResponse());
    iamPolicyManagementService.getV2Policy(null).execute();
  }

  // Test the deleteV2Policy operation with a valid options model parameter
  @Test
  public void testDeleteV2PolicyWOptions() throws Throwable {
    // Register a mock response
    String mockResponseBody = "";
    String deleteV2PolicyPath = "/v2/policies/testString";
    server.enqueue(new MockResponse()
      .setResponseCode(204)
      .setBody(mockResponseBody));

    // Construct an instance of the DeleteV2PolicyOptions model
    DeleteV2PolicyOptions deleteV2PolicyOptionsModel = new DeleteV2PolicyOptions.Builder()
      .id("testString")
      .build();

    // Invoke deleteV2Policy() with a valid options model and verify the result
    Response<Void> response = iamPolicyManagementService.deleteV2Policy(deleteV2PolicyOptionsModel).execute();
    assertNotNull(response);
    Void responseObj = response.getResult();
    assertNull(responseObj);

    // Verify the contents of the request sent to the mock server
    RecordedRequest request = server.takeRequest();
    assertNotNull(request);
    assertEquals(request.getMethod(), "DELETE");
    // Verify request path
    String parsedPath = TestUtilities.parseReqPath(request);
    assertEquals(parsedPath, deleteV2PolicyPath);
    // Verify that there is no query string
    Map<String, String> query = TestUtilities.parseQueryString(request);
    assertNull(query);
  }

  // Test the deleteV2Policy operation with and without retries enabled
  @Test
  public void testDeleteV2PolicyWRetries() throws Throwable {
    iamPolicyManagementService.enableRetries(4, 30);
    testDeleteV2PolicyWOptions();

    iamPolicyManagementService.disableRetries();
    testDeleteV2PolicyWOptions();
  }

  // Test the deleteV2Policy operation with a null options model (negative test)
  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testDeleteV2PolicyNoOptions() throws Throwable {
    server.enqueue(new MockResponse());
    iamPolicyManagementService.deleteV2Policy(null).execute();
  }

  // Perform setup needed before each test method
  @BeforeMethod
  public void beforeEachTest() {
    // Start the mock server.
    try {
      server = new MockWebServer();
      server.start();
    } catch (IOException err) {
      fail("Failed to instantiate mock web server");
    }

    // Construct an instance of the service
    constructClientService();
  }

  // Perform tear down after each test method
  @AfterMethod
  public void afterEachTest() throws IOException {
    server.shutdown();
    iamPolicyManagementService = null;
  }

  // Constructs an instance of the service to be used by the tests
  public void constructClientService() {
    System.setProperty("TESTSERVICE_AUTH_TYPE", "noAuth");
    final String serviceName = "testService";

    iamPolicyManagementService = IamPolicyManagement.newInstance(serviceName);
    String url = server.url("/").toString();
    iamPolicyManagementService.setServiceUrl(url);
  }
}