package eee;

public class CenterVO {
	private String Center;
	private String Operation;
	private String Office;
	public String getCenter() {
		return Center;
	}
	public void setCenter(String center) {
		Center = center;
	}
	public String getOperation() {
		return Operation;
	}
	public void setOperation(String operation) {
		Operation = operation;
	}
	public String getOffice() {
		return Office;
	}
	public void setOffice(String office) {
		Office = office;
	}
	public CenterVO(String center, String operation, String office) {
		super();
		Center = center;
		Operation = operation;
		Office = office;
	}
	public CenterVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	
}

