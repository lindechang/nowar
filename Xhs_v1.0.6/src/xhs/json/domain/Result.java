package xhs.json.domain;

public class Result {
	

	private int flag;
	private String data;
	private String reason;
	

	

	public Result() {
		// TODO Auto-generated constructor stub
	}
	
	public Result(int flag, String data, String reason) {
		super();
		this.flag = flag;
		this.data = data;
		this.reason = reason;
	}


	public int getFlag() {
		return flag;
	}

	public void setFlag(int flag) {
		this.flag = flag;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Override
	public String toString() {
		return "Result [flag=" + flag + ", data=" + data + ", reason=" + reason
				+ "]";
	}


	

}
