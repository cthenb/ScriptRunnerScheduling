import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.YearMonth;
import java.time.Month;
import static java.time.temporal.TemporalAdjusters.*;

public class DateHelper
{
    // If the day falls on a weekend, return date of first workday after weekend
    public static LocalDate getFirstWorkDayAtOrAfterDayInMonth(LocalDate currentDate, int dayNumber)
    {
        LocalDate day25 = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), dayNumber);
        DayOfWeek day = day25.getDayOfWeek();
        
        switch (day) {
            case DayOfWeek.SUNDAY:
            	return day25.plusDays(1);
            case DayOfWeek.SATURDAY:
            	return day25.plusDays(2);
            default:
                return day25;
        }
    }
    
    public static LocalDate getEndOfNextWeek(LocalDate currentDate)
    {
		switch (currentDate.getDayOfWeek()) {
			case DayOfWeek.SUNDAY:
            	return currentDate.plusDays(7);
            case DayOfWeek.SATURDAY:
            	return currentDate.plusDays(8);
            case DayOfWeek.FRIDAY:
            	return currentDate.plusDays(9);
            case DayOfWeek.THURSDAY:
            	return currentDate.plusDays(10);
            case DayOfWeek.WEDNESDAY:
            	return currentDate.plusDays(11);
            case DayOfWeek.TUESDAY:
            	return currentDate.plusDays(12);
            case DayOfWeek.MONDAY:
            	return currentDate.plusDays(13);
		}
	}
    
    // If the day falls on a weekend, return date of first workday prior to weekend
    public static LocalDate getFirstWorkDayAtOrBeforeDayInMonth(LocalDate currentDate, int dayNumber)
    {
        LocalDate day25 = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), dayNumber);
        DayOfWeek day = day25.getDayOfWeek();
        
        switch (day) {
            case DayOfWeek.SUNDAY:
            	return day25.minusDays(2);
            case DayOfWeek.SATURDAY:
            	return day25.minusDays(1);
            default:
                return day25;
        }
    }
	
	public static LocalDate getTaskDateIfTaskNeedsToBeCreated(DayOfWeek start, boolean before, int day)
	{			
		LocalDate currentDate = LocalDate.now();
		LocalDate endOfNextWeek = currentDate.plusDays(9);
        Month currentMonth = currentDate.getMonth();
        Month nextMonth = endOfNextWeek.getMonth();
		Month[] months = new Month[2];
        months[0] = currentMonth;
        months[1] = nextMonth;
        
		return getTaskDateIfTaskNeedsToBeCreated(start, before, day, months);
	}
	public static LocalDate getTaskDateIfTaskNeedsToBeCreated(DayOfWeek start, boolean before, int day, Month[] months)
	{	
		for (Month month : months) {
			LocalDate date = getTaskDateIfTaskNeedsToBeCreated(start, before, day, month);
			
			if (date != null) {
				return date;
			}
		}
		
		return null;
	}
	public static LocalDate getTaskDateIfTaskNeedsToBeCreated(DayOfWeek start, boolean before, int day, Month month)
	{
		LocalDate currentDate = LocalDate.now();
		LocalDate workDay = null;
        
        // Only on the specified day to ensure scheduling does not get messed up
        if (currentDate.getDayOfWeek() == start) {
            LocalDate endOfNextWeek = getEndOfNextWeek(currentDate);

            // Only in the specified month
            if (currentDate.getMonth() == month || endOfNextWeek.getMonth() == month) {
                // It's at or after today in the current month
                if (day >= currentDate.getDayOfMonth()
                    && day <= endOfNextWeek.getDayOfMonth()) {
					if (before) {
						workDay = getFirstWorkDayAtOrBeforeDayInMonth(currentDate, day);
					}
					else {				
						LocalDate toReturn = getFirstWorkDayAtOrAfterDayInMonth(currentDate, day);
						
						if (toReturn <= endOfNextWeek) {
							workDay = toReturn;
						}
					}
                }
			}
        }
        
        return workDay;
	}
    
    public static int getLastDayOfMonth()
    {
        LocalDate currentDate = LocalDate.now();
        LocalDate lastDay = currentDate.with(lastDayOfMonth());
        
        return lastDay.getDayOfMonth();
    }
    		
    public static LocalDate getFirstOccurenceOfDay(int day)
    {
        int currentDay = LocalDate.now().getDayOfMonth();
        
        // Is today the day?
        if (currentDay == day) {
            return LocalDate.now();
        }
        // Did we pass it already this month?
        else if (currentDay > day){
            YearMonth month = YearMonth.from(LocalDate.now());
            LocalDate newDate = month.atDay(day);
            
            return newDate.plusMonths(1);
        }
        // It's still to come this month
        else {            
            return LocalDate.of(LocalDate.now().getYear(), LocalDate.now().getMonth(), day);
        }
    }
}
