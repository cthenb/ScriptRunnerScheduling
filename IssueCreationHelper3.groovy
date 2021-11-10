import com.atlassian.jira.bc.issue.IssueService.TransitionValidationResult;
import com.atlassian.jira.bc.issue.IssueService.IssueResult;
import com.atlassian.jira.component.ComponentAccessor;
import com.atlassian.jira.issue.MutableIssue;
import com.atlassian.jira.util.DateFieldFormatImpl;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.LocalDate;
import org.apache.log4j.Logger;
import org.apache.log4j.Level;

public class IssueCreationHelper
{    
    public static String Create(
        String projectKey,
        String priorityId,
        String typeId,
        String summary,
        String description,
        String epicKey,
        LocalDate dueDate,
        String assigneeName,
        String customer,
        int workflowActionId
    )
    {        
        def issueService = ComponentAccessor.issueService;
        def projectManager = ComponentAccessor.projectManager;
        def user = ComponentAccessor.jiraAuthenticationContext.getLoggedInUser();
        def issueInputParameters = issueService
        	.newIssueInputParameters();
        def log = Logger.getRootLogger();

        issueInputParameters = issueInputParameters
            .setProjectId(projectManager
                .getProjectObjByKey(projectKey).id)
            .setSummary(summary)
            .setDescription(description)
            .setIssueTypeId(typeId)
            .setPriorityId(priorityId)
            .setReporterId(user.name);

        if (epicKey != null && !epicKey.isEmpty()) {
			issueInputParameters = issueInputParameters
            	.addCustomFieldValue(10006, epicKey);
        }
        
        if (assigneeName != null && !assigneeName.isEmpty()) {
			issueInputParameters = issueInputParameters
            	.setAssigneeId(assigneeName); //TODO: test
        }
        
        if (customer != null && !customer.isEmpty()) {
			issueInputParameters = issueInputParameters
            	.addCustomFieldValue(10925, customer); //TODO: test
        }
        
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
}
