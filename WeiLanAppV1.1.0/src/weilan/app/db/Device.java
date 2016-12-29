package weilan.app.db;

public class Device {
	
	private String deviceName;
	private String deviceNumber;
	private String deviceSet;
	private String deviceTemp;
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getDeviceNumber() {
		return deviceNumber;
	}
	public void setDeviceNumber(String deviceNumber) {
		this.deviceNumber = deviceNumber;
	}
	public String getDeviceSet() {
		return deviceSet;
	}
	public void setDeviceSet(String deviceSet) {
		this.deviceSet = deviceSet;
	}
	public String getDeviceTemp() {
		return deviceTemp;
	}
	public void setDeviceTemp(String deviceTemp) {
		this.deviceTemp = deviceTemp;
	}
	@Override
	public String toString() {
		return "Device [deviceName=" + deviceName + ", deviceNumber="
				+ deviceNumber + ", deviceSet=" + deviceSet + ", deviceTemp="
				+ deviceTemp + "]";
	}
	

}
