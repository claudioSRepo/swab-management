package it.cs.contact.tracing.be.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.cs.contact.tracing.be.utils.Util;
import lombok.*;

@DynamoDBTable(tableName = "reported-swabs")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Swab {

	@DynamoDBHashKey(attributeName = "fiscal-code")
	@JsonProperty
	private String fiscalCode;

	@DynamoDBAttribute(attributeName = "reported-on")
	@JsonProperty
	private int reportedOn;

	@DynamoDBAttribute(attributeName = "state")
	@JsonProperty
	@DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
	private SwabState state;

	@DynamoDBIgnore
	public boolean isValid() {

		return !StringUtils.isNullOrEmpty(fiscalCode) && Util.isValidDate(reportedOn) && state != null;
	}

	@DynamoDBIgnore
	public Swab update(final Swab swab) {

		this.state = swab.state;

		return this;
	}

	public enum SwabState {

		IN_QUEUE, WAITING_FOR_RESULT, POSITIVE, NEGATIVE
	}
}