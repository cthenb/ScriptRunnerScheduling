import java.time.LocalDate;
import java.time.DayOfWeek;
import java.time.YearMonth;
import java.time.Month;

public class DateHelper
{
    public static LocalDate getLocalDate(int year, int month, int day)
    {
        return LocalDate.of(year, month, day);
    }

    // If the day falls on a weekend, return date of first workday after weekend
    public static LocalDate getFirstWorkDayAtOrAfterDayInMonth(LocalDate inputDate)
    {
        DayOfWeek day = inputDate.getDayOfWeek();
        
        switch (day) {
            case DayOfWeek.SUNDAY:
            	return inputDate.plusDays(1);
            case DayOfWeek.SATURDAY:
            	return inputDate.plusDays(2);
            default:
                return inputDate;
        }
    }
    
    public static LocalDate getEndOfNextWeek(LocalDate inputDate)
    {
		switch (inputDate.getDayOfWeek()) {
			case DayOfWeek.SUNDAY:
            	return inputDate.plusDays(7);
            case DayOfWeek.SATURDAY:
            	return inputDate.plusDays(8);
            case DayOfWeek.FRIDAY:
            	return inputDate.plusDays(9);
            case DayOfWeek.THURSDAY:
            	return inputDate.plusDays(10);
            case DayOfWeek.WEDNESDAY:
            	return inputDate.plusDays(11);
            case DayOfWeek.TUESDAY:
            	return inputDate.plusDays(12);
            case DayOfWeek.MONDAY:
            	return inputDate.plusDays(13);
		}
	}
    
    // If the day falls on a weekend, return date of first workday prior to weekend
    public static LocalDate getFirstWorkDayAtOrBeforeDayInMonth(LocalDate inputDate)
    {
        DayOfWeek day = inputDate.getDayOfWeek();
        
        switch (day) {
            case DayOfWeek.SUNDAY:
            	return inputDate.minusDays(2);
            case DayOfWeek.SATURDAY:
            	return inputDate.minusDays(1);
            default:
                return inputDate;
        }
    }
	
	public static LocalDate getTaskDateIfTaskNeedsToBeCreated(boolean before, int day)
    {
        LocalDate inputDate = LocalDate.now();

        return getTaskDateIfTaskNeedsToBeCreated(before, day, inputDate);
    }
    public static LocalDate getTaskDateIfTaskNeedsToBeCreated(boolean before, int day, LocalDate inputDate)
	{
		LocalDate endOfNextWeek = getEndOfNextWeek(inputDate);
        Month currentMonth = inputDate.getMonth();
        Month nextMonth = endOfNextWeek.getMonth();
        
        if (currentMonth != nextMonth) {
            Month[] months = new Month[2];
            months[0] = currentMonth;
            months[1] = nextMonth;

            return getTaskDateIfTaskNeedsToBeCreated(before, day, months, inputDate);
        }
        else {
            return getTaskDateIfTaskNeedsToBeCreated(before, day, currentMonth, inputDate);
        }
    }
	public static LocalDate getTaskDateIfTaskNeedsToBeCreated(boolean before, int day, Month[] months, LocalDate inputDate)
	{	
		for (Month month : months) {
			LocalDate date = getTaskDateIfTaskNeedsToBeCreated(before, day, month, inputDate);
			
			if (date != null) {
				return date;
			}
		}
		
		return null;
	}
	public static LocalDate getTaskDateIfTaskNeedsToBeCreated(boolean before, int day, Month month, LocalDate inputDate)
	{
		LocalDate workDay = null;
        LocalDate endOfNextWeek = getEndOfNextWeek(inputDate);
        boolean sameMonth = inputDate.getMonth() == endOfNextWeek.getMonth();
        boolean dayToCome = day >= inputDate.getDayOfMonth() || !sameMonth;
        boolean beforeNextWeek = (sameMonth && dayToCome && day <= endOfNextWeek.getDayOfMonth()) || (!sameMonth && day <= endOfNextWeek.getDayOfMonth());

        // Only in the specified month
        if (inputDate.getMonth() == month || endOfNextWeek.getMonth() == month) {
            // It's at or after today in the current month
            if (dayToCome && beforeNextWeek) {
                if (before) {
                    workDay = getFirstWorkDayAtOrBeforeDayInMonth(getFirstOccurenceOfDay(inputDate, day));
                } else {
                    LocalDate toReturn = getFirstWorkDayAtOrAfterDayInMonth(getFirstOccurenceOfDay(inputDate, day));

                    if (toReturn <= endOfNextWeek) {
                        workDay = toReturn;
                    }
                }
            }
        }
        
        return workDay;
	}
    
    public static int getLastDayOfMonth()
    {
        LocalDate inputDate = LocalDate.now();

        return getLastDayOfMonth(inputDate);
    }
    public static int getLastDayOfMonth(LocalDate inputDate)
    {
        return inputDate.lengthOfMonth();
    }
    		
    public static LocalDate getFirstOccurenceOfDay(int day)
    {
        LocalDate now = LocalDate.now();

        return getFirstOccurenceOfDay(day, now);
    }
    public static LocalDate getFirstOccurenceOfDay(int day, LocalDate inputDate)
    {
        int dateDay = inputDate.getDayOfMonth();
        
        // Is today the day?
        if (dateDay == day) {
            return inputDate;
        }
        // Did we pass it already this month?
        else if (dateDay > day){
            YearMonth month = YearMonth.from(inputDate);
            LocalDate newDate = month.atDay(day);
            
            return newDate.plusMonths(1);
        }
        // It's still to come this month
        else {            
            return LocalDate.of(inputDate.getYear(), inputDate.getMonth(), day);
        }
    }
}
