package dataReader;

import static org.junit.Assert.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class Test_QuotesReader {

	@Test
	/**
	 * We want to test whether the up ratio we calculate is right
	 * We have already manually calculated the first up ratio is
	 * 0.174954. We test whether the up ratio we get is close 
	 * enough to this number 
	 * @throws Exception
	 */
	public void test_upRatio() throws Exception {
		QuotesReader reader = new QuotesReader("panicData.csv");
		SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");		
		Calendar c = Calendar.getInstance();
		Integer i = 20000101;
		Date startDate = originalFormat.parse(i.toString());
		c.setTime(startDate); 
		c.add(Calendar.YEAR, 1);
		Date endDate = c.getTime();
		reader.readData(startDate, endDate);
		assertTrue(Math.abs(reader.getUpRatios().get(0) - 0.174954) < 0.0001);
	}
	
}
