import groovy.json.JsonSlurper
import groovy.json.JsonOutput
import com.kms.katalon.core.annotation.Keyword

// Jira API credentials
String jiraUrl = 'https://your-domain.atlassian.net/rest/api/3/search'  // Jira search API endpoint
String jqlQuery = 'project = YOUR_PROJECT_KEY AND issuetype = Test AND status = "To Do"' // Adjust the JQL as necessary
String username = 'YOUR_JIRA_USERNAME'  // Replace with your Jira username
String apiToken = 'YOUR_JIRA_API_TOKEN'  // Replace with your Jira API token

// Method to fetch new issues from Jira
@Keyword
def fetchNewJiraIssues() {
	try {
		// Prepare the connection
		def url = new URL("${jiraUrl}?jql=${URLEncoder.encode(jqlQuery, 'UTF-8')}&fields=summary,status")
		def connection = url.openConnection()
		connection.setRequestMethod("GET")
		connection.setRequestProperty("Authorization", "Basic " + "${username}:${apiToken}".bytes.encodeBase64().toString())
		connection.setRequestProperty("Accept", "application/json")

		// Get the response
		if (connection.responseCode == 200) {
			def response = connection.inputStream.text
			def jsonSlurper = new JsonSlurper()
			def jsonResponse = jsonSlurper.parseText(response)

			// Parse and print the results
			jsonResponse.issues.each { issue ->
				println("Key: ${issue.key}, Summary: ${issue.fields.summary}, Status: ${issue.fields.status.name}")
				
				// Optionally: Create corresponding test cases in Katalon based on the issue
				createTestCase(issue.key, issue.fields.summary)
			}
		} else {
			println("Failed to fetch Jira issues. Response Code: ${connection.responseCode}")
		}
	} catch (Exception e) {
		println("Error occurred while fetching Jira issues: ${e.message}")
	}
}

// Method to create test cases in Katalon (implement your own logic)
def createTestCase(String issueKey, String issueSummary) {
	// Logic to create a test case in Katalon
	// For example, you can create a new test case with the given issueKey and issueSummary
	println("Creating test case for issue: ${issueKey} - ${issueSummary}")
	// Add your code to create a new test case in Katalon
}

// Call the method to fetch new issues
fetchNewJiraIssues()
