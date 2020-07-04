package sas.part.time.job.database;

public interface DatabaseInfo {

	public String 	COUNTRY_DB 				= "Country.db";
	public String 	COUNTRY_TABLE 			= "countries";
	public String 	COUNTRY_COLUMN1 		= "id";
	public String 	COUNTRY_COLUMN2 		= "name";
	public String 	COUNTRY_COLUMN3 		= "currency";
	public String 	COUNTRY_QUERY 			= "select "+COUNTRY_COLUMN1+", "+COUNTRY_COLUMN2 +", "+COUNTRY_COLUMN3+" from "+COUNTRY_TABLE;
	
	public String 	ZONE_TABLE 				= "states";
	public String 	ZONE_COLUMN1 			= "id";
	public String 	ZONE_COLUMN2 			= "name";
	public String 	ZONE_COLUMN3 			= "country_id";
	public String 	ZONE_QUERY 				= "select "+ZONE_COLUMN1+", "+ZONE_COLUMN2+", "+ZONE_COLUMN3+" from "+ZONE_TABLE+" where "+ZONE_COLUMN3+ "=? ";
	
	public String 	CITY_TABLE 				= "cities";
	public String 	CITY_COLUMN1 			= "id";
	public String 	CITY_COLUMN2 			= "name";
	public String 	CITY_COLUMN3 			= "state_id";
	public String 	CITY_QUERY 				= "select "+CITY_COLUMN1+", "+CITY_COLUMN2+", "+CITY_COLUMN3+" from "+CITY_TABLE+" where "+CITY_COLUMN3+ "=? ";

}
