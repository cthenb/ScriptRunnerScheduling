import java.time.LocalDate;

public class SimpleJiraCreation {
    public String getPriorityId() {
        return priorityId;
    }

    public String getProjectKey() {
        return projectKey;
    }

    public String getTypeId() {
        return typeId;
    }

    public String getSummary() {
        return summary;
    }

    public String getDescription() {
        return description;
    }

    public String getEpicKey() {
        return epicKey;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public String getAssigneeUserName() {
        return assigneeUserName;
    }

    public String getReporterUserName() {
        return reporterUserName;
    }

    public String getCustomer() {
        return customer;
    }

    public int getWorkflowActionId() {
        return workflowActionId;
    }

    private String priorityId;
    private String projectKey;
    private String typeId;
    private String summary;
    private String description;
    private String epicKey;
    private LocalDate dueDate;
    private String assigneeUserName;
    private String reporterUserName;
    private String customer;
    private int workflowActionId;

    public SimpleJiraCreation(
            String priorityId,
            String projectKey,
            String typeId,
            String summary,
            String description,
            String epicKey,
            LocalDate dueDate,
            String assigneeUserName,
            String reporterUserName,
            String customer,
            int workflowActionId) {
        this.priorityId = priorityId;
        this.projectKey = projectKey;
        this.typeId = typeId;
        this.summary = summary;
        this.description = description;
        this.epicKey = epicKey;
        this.dueDate = dueDate;
        this.assigneeUserName = assigneeUserName;
        this.reporterUserName = reporterUserName;
        this.customer = customer;
        this.workflowActionId = workflowActionId;
    }

    public SimpleJiraCreation(
            String priorityId,
            String projectKey,
            String typeId,
            String summary,
            String description,
            String customer) {
        this.priorityId = priorityId;
        this.projectKey = projectKey;
        this.typeId = typeId;
        this.summary = summary;
        this.description = description;
        this.customer = customer;
    }
}
