package weilan.app.db;

public class SwDevice {
	
	private int id;
	private String swdeviceName;
	private String swdeviceNumber;
	private int swdeviceSelect;
	private int swdeviceSet;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public int getSwdeviceSelect() {
		return swdeviceSelect;
	}

	public void setSwdeviceSelect(int swdeviceSelect) {
		this.swdeviceSelect = swdeviceSelect;
	}

	public int getSwdeviceSet() {
		return swdeviceSet;
	}

	public void setSwdeviceSet(int swdeviceSet) {
		this.swdeviceSet = swdeviceSet;
	}

	public String getSwdeviceName() {
		return swdeviceName;
	}

	public void setSwdeviceName(String swdeviceName) {
		this.swdeviceName = swdeviceName;
	}

	public String getSwdeviceNumber() {
		return swdeviceNumber;
	}

	public void setSwdeviceNumber(String swdeviceNumber) {
		this.swdeviceNumber = swdeviceNumber;
	}

	

	public SwDevice() {
		// TODO Auto-generated constructor stub
	}

}
