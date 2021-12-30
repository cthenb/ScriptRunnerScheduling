# ScriptRunnerScheduling
Extension classes for ScriptRunner Script Editor to create recurring tasks in JIRA.

Optionally, you can use the cron setting in ScriptRunner jobs to do the proper scheduling and only use the IssueCreationHelper code to create the actual tasks.
I found that to be insufficiently flexible in practice.

Use case: every week, on a fixed day, recurring tasks will be created for the next week.
The recurring tasks can be once a year, once a quarter, once a month, once a week, once a day, once a whatever you want really.

* TaskCreationExamples.groovy contains a few examples, which is what you would put in a ScriptRunner 'job'.
* DateHelper.groovy contains logic on how to determine whether a task will need to be created next week or not.
* IssueCreationHelper.groovy takes the parameters and creates the actual JIRA task with the type, priority, summary, description, etc.
