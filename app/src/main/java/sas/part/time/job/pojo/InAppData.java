package sas.part.time.job.pojo;


public class InAppData {

	private String ORDER_ID;
	private String PRODUCT_ID;
	private String PACKAGE_NAME;
	private String PURCHASE_TIME;
	private String PURCHASE_STATE;
	private String DEVELOPER_PAYLOAD;
	private String PURCHASE_TOKEN ;
	private String FLAG_SENT_UNSENT ;
	
	public InAppData() {
		ORDER_ID 			= new String("");
		PRODUCT_ID 			= new String("");
		PACKAGE_NAME 		= new String("");
		PURCHASE_TIME 		= new String("");
		PURCHASE_STATE 		= new String("");
		DEVELOPER_PAYLOAD 	= new String("");
		PURCHASE_TOKEN 		= new String("");
		FLAG_SENT_UNSENT 	= new String("");
	}

	public String getORDER_ID() {
		return ORDER_ID;
	}

	public void setORDER_ID(String oRDER_ID) {
		ORDER_ID = oRDER_ID;
	}

	public String getPRODUCT_ID() {
		return PRODUCT_ID;
	}

	public void setPRODUCT_ID(String pRODUCT_ID) {
		PRODUCT_ID = pRODUCT_ID;
	}

	public String getPACKAGE_NAME() {
		return PACKAGE_NAME;
	}

	public void setPACKAGE_NAME(String pACKAGE_NAME) {
		PACKAGE_NAME = pACKAGE_NAME;
	}

	public String getPURCHASE_TIME() {
		return PURCHASE_TIME;
	}

	public void setPURCHASE_TIME(String pURCHASE_TIME) {
		PURCHASE_TIME = pURCHASE_TIME;
	}

	public String getPURCHASE_STATE() {
		return PURCHASE_STATE;
	}

	public void setPURCHASE_STATE(String pURCHASE_STATE) {
		PURCHASE_STATE = pURCHASE_STATE;
	}

	public String getDEVELOPER_PAYLOAD() {
		return DEVELOPER_PAYLOAD;
	}

	public void setDEVELOPER_PAYLOAD(String dEVELOPER_PAYLOAD) {
		DEVELOPER_PAYLOAD = dEVELOPER_PAYLOAD;
	}

	public String getPURCHASE_TOKEN() {
		return PURCHASE_TOKEN;
	}

	public void setPURCHASE_TOKEN(String pURCHASE_TOKEN) {
		PURCHASE_TOKEN = pURCHASE_TOKEN;
	}

	public String getFLAG_SENT_UNSENT() {
		return FLAG_SENT_UNSENT;
	}

	public void setFLAG_SENT_UNSENT(String fLAG_SENT_UNSENT) {
		FLAG_SENT_UNSENT = fLAG_SENT_UNSENT;
	}
	
}