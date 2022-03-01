import com.atlassian.jira.bc.issue.IssueService.TransitionValidationResult;
import com.atlassian.jira.bc.issue.IssueService.IssueResult;
import com.atlassian.jira.component.ComponentAccessor
import org.apache.log4j.Logger

public class IssueCreationHelper
{
    public static String Create(
            SimpleJiraCreation issueParameters
    )
    {
        def issueService = ComponentAccessor.issueService;
        def projectManager = ComponentAccessor.projectManager;
        def user = getReporterUser(issueParameters);
        def issueInputParameters = issueService
        	.newIssueInputParameters();
        def log = Logger.getRootLogger();

        issueInputParameters = issueInputParameters
            .setProjectId(projectManager
                .getProjectObjByKey(issueParameters.getProjectKey()).id)
            .setSummary(issueParameters.getSummary())
            .setDescription(issueParameters.getDescription())
            .setIssueTypeId(issueParameters.getTypeId())
            .setPriorityId(issueParameters.getPriorityId())
            .setReporterId(user.getName())

        issueInputParameters = setReporter(issueInputParameters, issueParameters);

        String epicKey = issueParameters.getEpicKey();
        if (epicKey != null && !epicKey.isEmpty()) {
			issueInputParameters = issueInputParameters
            	.addCustomFieldValue(10006, epicKey);
        }

        String assigneeName = issueParameters.getAssigneeUserName();
        if (assigneeName != null && !assigneeName.isEmpty()) {
			issueInputParameters = issueInputParameters
            	.setAssigneeId(assigneeName); //TODO: test
        }

        String customer = issueParameters.getCustomer();
        if (customer != null && !customer.isEmpty()) {
			issueInputParameters = issueInputParameters
            	.addCustomFieldValue(10925, customer); //TODO: test
        }

        String dueDate = issueParameters.getDueDate();
        if (dueDate != null) {
            issueInputParameters = issueInputParameters
            	.setDueDate(dueDate.format("d/MMM/yy"));
        }

        issueInputParameters
        	.skipScreenCheck();

        // Validate input
        def validationResult = issueService
        	.validateCreate(user, issueInputParameters)

        // Attempt to create task, log results
        if (validationResult.isValid()) {
            def issueResult = issueService.create(user, validationResult)
            log.info "Issue created succesfully: ${issueResult.issue.getKey()}" //TODO: doesn't log for some reason

            // Now transition to desired state

            int workflowActionId = issueParameters.getWorkflowActionId();
            if (workflowActionId != null && workflowActionId != 0) {
                TransitionValidationResult transitionValidationResult = issueService.validateTransition(
                    user, issueResult.issue.getId(), workflowActionId, issueInputParameters
                );

                if (transitionValidationResult.isValid())
                {
                    IssueResult transitionResult = issueService.transition(user, transitionValidationResult);

                    if (!transitionResult.isValid())
                    {
                        log.error "Cannot transition issue. Errors: ${transitionResult.errorCollection}"
                    }
                }
            }
        }
        else {
            log.error "Cannot create issue. Errors: ${validationResult.errorCollection}"
        }
    }

    public com.atlassian.jira.user.ApplicationUser getReporterUser(def issueParameters)
    {
        if (issueParameters.getReporterUserName() == null) {
            def user = ComponentAccessor.jiraAuthenticationContext.getLoggedInUser();
            issueInputParameters = issueInputParameters
                .setReporterId(user.name);

            return user;
        }
        else {
            def userManager = ComponentAccessor.getUserManager() as UserManager;
            def user = userManager.getUserByKey(issueParameters.getReporterUserName());

            return user;
        }
    }
}
