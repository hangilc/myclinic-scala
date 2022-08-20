package dev.myclinic.vertx.dto;

public class PaymentDTO {
	public int visitId;
	public int amount;
	public String paytime;

	@Override
	public String toString() {
		return "PaymentDTO{" +
				"visitId=" + visitId +
				", amount=" + amount +
				", paytime='" + paytime + '\'' +
				'}';
	}
}