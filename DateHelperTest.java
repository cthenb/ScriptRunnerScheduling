import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;

public class DateHelperTest {
    @Test
    public void getFirstWorkDayAtOrAfterDayInMonthTest()
    {
        // After
        var inputDate1 = LocalDate.of(2022, 03, 6);
        var output1 = DateHelper.getFirstWorkDayAtOrAfterDayInMonth(inputDate1);
        var compareDate1 = LocalDate.of(2022, 03, 07);
        Assert.assertEquals(compareDate1, output1);

        // Same
        var inputDate2 = LocalDate.of(2022, 03, 4);
        var output2 = DateHelper.getFirstWorkDayAtOrAfterDayInMonth(inputDate2);
        var compareDate2 = LocalDate.of(2022, 03, 4);
        Assert.assertEquals(compareDate2, output2);
    }


    @Test
    public void getFirstWorkDayAtOrBeforeDayInMonthTest()
    {
        // Before
        var inputDate1 = LocalDate.of(2022, 03, 6);
        var output1 = DateHelper.getFirstWorkDayAtOrBeforeDayInMonth(inputDate1);
        var compareDate1 = LocalDate.of(2022, 03, 4);
        Assert.assertEquals(compareDate1, output1);

        // Same
        var inputDate2 = LocalDate.of(2022, 03, 4);
        var output2 = DateHelper.getFirstWorkDayAtOrBeforeDayInMonth(inputDate2);
        var compareDate2 = LocalDate.of(2022, 03, 4);
        Assert.assertEquals(compareDate2, output2);
    }

    @Test
    public void getTaskDateIfTaskNeedsToBeCreatedTest()
    {
        // Next week contains a new month
        LocalDate today = LocalDate.of(2022, 02, 25);
        var date = DateHelper.getTaskDateIfTaskNeedsToBeCreated(false, 1, today);
        Assert.assertEquals(LocalDate.of(2022, 03, 1), date);

        //TODO: Before
    }

    @Test
    public void getFirstOccurenceOfDayTest()
    {
        // today
        var today = LocalDate.now().getDayOfMonth();
        var output1 = DateHelper.getFirstOccurenceOfDay(today, LocalDate.now());
        Assert.assertEquals(output1, LocalDate.now());

        // Next month
        var yesterday = LocalDate.now().minusDays(1);
        var yesterdayDay = yesterday.getDayOfMonth();
        var output2 = DateHelper.getFirstOccurenceOfDay(yesterdayDay, LocalDate.now());
        Assert.assertEquals(output2, yesterday.plusMonths(1));

        // Somewhere this month (if we're not on the last day)
        var tomorrow = LocalDate.now().plusDays(1);
        var tomorrowDay = tomorrow.getDayOfMonth();

        if (DateHelper.getLastDayOfMonth(tomorrow) != today) {
            var output3 = DateHelper.getFirstOccurenceOfDay(tomorrowDay, LocalDate.now());
            Assert.assertEquals(output3, tomorrow);
        }
    }
}
