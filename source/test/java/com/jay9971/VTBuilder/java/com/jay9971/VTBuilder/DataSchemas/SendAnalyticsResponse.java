package com.jay9971.VTBuilder.DataSchemas;

import java.util.Arrays;

public class SendAnalyticsResponse {
	private String status;
	private String[] key1;
	private double[] val1;
	private String[] key2;
	private double[] val2;
	private String[] key3;
	private double[] val3;
	private String[] key4;
	private double[] val4;
	
	public SendAnalyticsResponse() {
		
	}
	
	public SendAnalyticsResponse(String status) {
		this.status = status;
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String[] getKey1() {
		return key1;
	}
	public void setKey1(String[] key1) {
		this.key1 = key1;
	}
	public double[] getVal1() {
		return val1;
	}
	public void setVal1(double[] val1) {
		this.val1 = val1;
	}
	public String[] getKey2() {
		return key2;
	}
	public void setKey2(String[] key2) {
		this.key2 = key2;
	}
	public double[] getVal2() {
		return val2;
	}
	public void setVal2(double[] val2) {
		this.val2 = val2;
	}
	public String[] getKey3() {
		return key3;
	}
	public void setKey3(String[] key3) {
		this.key3 = key3;
	}
	public double[] getVal3() {
		return val3;
	}
	public void setVal3(double[] val3) {
		this.val3 = val3;
	}
	public String[] getKey4() {
		return key4;
	}
	public void setKey4(String[] key4) {
		this.key4 = key4;
	}
	public double[] getVal4() {
		return val4;
	}
	public void setVal4(double[] ds) {
		this.val4 = ds;
	}
	@Override
    public String toString() {
        return "SendAnalyticsResponse{" +
                "status='" + status + '\'' +
                ", key1=" + Arrays.toString(key1) +
                ", val1=" + Arrays.toString(val1) +
                ", key2=" + Arrays.toString(key2) +
                ", val2=" + Arrays.toString(val2) +
                ", key3=" + Arrays.toString(key3) +
                ", val3=" + Arrays.toString(val3) +
                ", key4=" + Arrays.toString(key4) +
                ", val4=" + Arrays.toString(val4) +
                '}';
    }
	
}
