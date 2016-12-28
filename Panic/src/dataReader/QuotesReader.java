package dataReader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class is used to read data in 
 * trading quotes and calculate 
 * some numbers
 * 
 * @author lin
 *
 */
public class QuotesReader {
	
	private String fileName;
	private String[] tickers;
	private TreeMap<Date, List<Double>> quotes;
	private List<Integer> nList;
	
	public QuotesReader(String fileName) {
		this.fileName = fileName;		
	}
	
	/**
	 * Read all the data from the file
	 * @throws Exception 
	 */
	public void readData() throws Exception {
		SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
		Integer dateInt = Integer.MIN_VALUE;
		Date startDate = originalFormat.parse(dateInt.toString());
		Integer dateInt2 = Integer.MAX_VALUE;
		Date endDate = originalFormat.parse(dateInt2.toString());
		readData(startDate, endDate);		
	}
	
	/**
	 * Read data between start date
	 * and end date
	 * 
	 * @param startDate
	 * @param endDate
	 * @throws Exception 
	 */
	public void readData(Date startDate, Date endDate) throws Exception {
		if (startDate.after(endDate)) {
			throw new Exception("Invalid input date!");
		}
		quotes = new TreeMap<Date, List<Double>>();
		nList = new ArrayList<Integer>();
		BufferedReader reader = new BufferedReader(new FileReader(fileName));
		// Read the first line as tickers
		tickers = reader.readLine().replaceAll("\"", "").split(",");
		// Get rid of the first empty field
		tickers = Arrays.copyOfRange(tickers, 1, tickers.length);
		
		String line;
		while ((line = reader.readLine()) != null) {
			line = line.replace("\"", "");
			// Get date and skip all the lines before start date
			Integer dateInt = Integer.parseInt(line.substring(0, 8));
			SimpleDateFormat originalFormat = new SimpleDateFormat("yyyyMMdd");
			Date date = originalFormat.parse(dateInt.toString());
			if (date.before(startDate)) {
				continue;
			}
			// Break after passing end date
			if (date.after(endDate)) {
				break;
			}
			// Read quote data
            String[] quoteString = line.substring(9).split(",");
            List<Double> quoteDouble = new ArrayList();
            for (String s:quoteString) {
            	// Our intervals look like [lower bound, upper bound) and we assign max value to the last interval
            		quoteDouble.add(s.equalsIgnoreCase("NA")? Double.NaN : Double.parseDouble(s));
            }
            quotes.put(date, quoteDouble);           
		}
		reader.close();		
	}
	
	public String[] getTickers() {
		return tickers;
	}
	
	public TreeMap<Date, List<Double>> getQuotes() {
		return quotes;
	}
	/**
	 * This function is used to get a list of ratios
	 *  indicating how many stocks are going 
	 *  upwards compared to the whole
	 *  stock markets
	 *  
	 * @return list of up ratios
	 */
	public List<Double> getUpRatios() {
		
		// Use linked list because it is faster to append
		List<Double> ups = new LinkedList<Double>();
		
		// USe entry to compare current price and next price
		Map.Entry<Date, List<Double>> currentEntry;
		Map.Entry<Date, List<Double>> nextEntry;
		
		// Point current entry to the first entry of quotes
		currentEntry = quotes.firstEntry();
		// Run loop to calculate the ratio of stocks going upwards
		while ((nextEntry = quotes.higherEntry(currentEntry.getKey())) != null) {
			List<Double> currentQuote = currentEntry.getValue();
			List<Double> nextQuote = nextEntry.getValue();
			
			double n = 0; // Number of stocks in current market
			double u = 0; // Number of stocks going upward
			for (int i = 0; i < currentQuote.size(); i++) {
				// Skip if either current quote or next quote is NaN
				if(currentQuote.get(i).isNaN() || nextQuote.get(i).isNaN()) 
					continue;
				n++;
				if(currentQuote.get(i) < nextQuote.get(i))
					u++;				
			}
			ups.add(u / n);
			// We also store the number of stocks as a byproduct
			this.nList.add((int)n);
			currentEntry = nextEntry;
		}
		return ups;
	}
	/**
	 * This function is used to calculate the 
	 * number of stocks in a period using 
	 *  simple average
	 * @return number of stocks in this period
	 */
	public int getN() {
		int n = 0;
		for(int i:nList) {
			n += i;
		}
		n = n / nList.size();
		return n;
	}
	
}
