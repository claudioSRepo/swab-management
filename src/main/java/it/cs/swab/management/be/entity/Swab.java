package it.cs.swab.management.be.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIgnore;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.cs.swab.management.be.utils.Util;
import lombok.*;

@DynamoDBTable(tableName = "positive-contacts")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Swab {

	@DynamoDBHashKey(attributeName = "fiscal-code")
	@JsonProperty
	private String fiscalCode;

	@DynamoDBAttribute(attributeName = "made-on")
	@JsonProperty
	private int madeOn;

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Swab{");
		sb.append("fiscalCode='").append(fiscalCode).append('\'');
		sb.append(", madeOn=").append(madeOn);
		sb.append('}');
		return sb.toString();
	}

	@DynamoDBIgnore
	public boolean isValid() {

		return !StringUtils.isNullOrEmpty(fiscalCode) && Util.isValidDate(madeOn);
	}
}