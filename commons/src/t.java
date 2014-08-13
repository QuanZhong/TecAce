import java.util.Calendar;
import java.util.List;

import xml.DateUtils;


public class t {
	

	private static Calendar c = Calendar.getInstance();
	private String Abc;
	private List<Ping> ping;
	
	public List<Ping> getPing() {
		return ping;
	}
	public void setPing(List<Ping> ping) {
		this.ping = ping;
	}
	public String getAbc(){
		return Abc;
	}
	public void setAbc(String Abc){
		this.Abc = Abc;
	}
	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		
		System.out.println(DateUtils.convertToString(c, DateUtils.MONTH_DATE_YEAR_FORMAT));

	}

}
