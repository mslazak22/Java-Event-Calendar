package EventCalendar;

public class Event {
	String eventName;
	int eventDay, eventMonth, eventYear;
	
	public Event() {
		eventName = "no events entered.";	
	}

	public String getEventName() {
		return eventName;
	}
	
	public void setEventName(String name) {
		this.eventName = name;
	}

	public int getEventDay() {
		return eventDay;
	}
	
	public void setEventDay(int day) {
		this.eventDay = day;
	}

	public int getEventMonth() {
		return eventMonth;
	}
	
	public void setEventMonth(int month) {
		this.eventMonth = month;
	}
	public int getEventYear() {
		return eventYear;
	}
	
	public void setEventYear(int year) {
		this.eventYear = year;
	}
	
	public String toString() {
		String state = eventName;
		return state;
	}
	
}



