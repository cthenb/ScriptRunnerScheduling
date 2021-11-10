# ScriptRunnerScheduling
Extension classes to drop in the Script Editor of ScriptRunner to schedule tasks with on a recurring basis.

Use case: every week, on a fixed day, recurring tasks will be created for the next week.
The recurring tasks can be once a year, once a week, once a day. 

TaskCreationExamples.groovy contains a few examples.
DateHelper.groovy contains logic on how to determine whether a task will need to be created next week or not.
IssueCreatoinHelper.groovy takes the parameters and creates the actual JIRA task with the type, priority, summary, description, etc.
