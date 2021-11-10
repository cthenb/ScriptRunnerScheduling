import DateHelper;
import IssueCreationHelper;
import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.Month;

def now = LocalDate.now();
def monday = now.plusDays(3); // in my use case this runs on Fridays
def month = now.getMonth();

// Create a task for each day of next week
for(int i = 0; i < 5; i++) {
    IssueCreationHelper.Create(
        "HI", // Project shortkey (obtainable from your issue key. If your new issue has 'HELP-10' as key, then 'HELP' is the project shortkey.
         "2", // priority id, obtainable from jira admin -> issues tab -> Priorities -> (hover over the edit button)
         "3", // type id, obtainable from jira admin -> issues tab -> (hover over the edit button) 
        "Daily task for ${monday.plusDays(i).format("dd-MM")}", // summary
        "Very elaborate description", // description
        "EPIC-RANDOM", //JIRA key of the EPIC you want to link this to, if any. Value null if none.
         monday.plusDays(i), // due date, null if none
         null, // assignee username
         null, // customer name, in my use case this is a custom field with single select
         61 // workflow action id, obtainable from jira admin -> issues tab -> workflows -> select workflow for type -> edit -> set to text display, hover over 'edit' of desired transition
    );
}

// Create a task once every week
IssueCreationHelper3.Create(
    "HI", 
    "4", 
    "3", 
    "Weekly task",
    "Very elaborate description,
    "EPIC-RANDOM", 
    monday, 
    null, 
    null, 
    61
);

// Create this task only if next week contains the first of the month
LocalDate workDay = DateHelper.
  getTaskDateIfTaskNeedsToBeCreated(
    DayOfWeek.FRIDAY, // Only if the job is run on a Friday, check this line (to prevent possible duplicates)
    false, // if day falls in a weekend, does it need to be created this week?
    1 // day of the month
  );

if (workDay != null) {
    IssueCreationHelper3.Create(
      "HI", 
      "4", 
      "3", 
      "Weekly task",
      "Very elaborate description,
      "EPIC-RANDOM", 
      monday, 
      null, 
      null, 
      61
  );
}

// Create a task only in specific months / once a quarter on a specific day
// ScriptEdtior can't handle inline declared arrays
Month[] quarterlyMonths = new Month[4];
quarterlyMonths[0] = Month.JANUARY;
quarterlyMonths[1] = Month.APRIL;
quarterlyMonths[2] = Month.JULY;
quarterlyMonths[3] = Month.OCTOBER;
workDay = DateHelper.getTaskDateIfTaskNeedsToBeCreated(DayOfWeek.FRIDAY, false, 1, quarterlyMonths);
if (workDay != null) {
      IssueCreationHelper3.Create(
      "HI", 
      "4", 
      "3", 
      "Quarterly task",
      "Very elaborate description,
      "EPIC-RANDOM", 
      monday, 
      null, 
      null, 
      61
  );
}

// Create a task only once a year
workDay = DateHelper.getTaskDateIfTaskNeedsToBeCreated(DayOfWeek.FRIDAY, false, 12, Month.DECEMBER);

if (workDay != null) {
    IssueCreationHelper3.Create(
      "HI", 
      "4", 
      "3", 
      "Yearly task",
      "Very elaborate description,
      "EPIC-RANDOM", 
      monday, 
      null, 
      null, 
      61
  );
}
