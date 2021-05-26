package it.cs.contact.tracing.be.entity;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.util.StringUtils;
import com.fasterxml.jackson.annotation.JsonProperty;
import it.cs.contact.tracing.be.exception.SwabManagementException;
import it.cs.contact.tracing.be.utils.Util;
import lombok.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDate;

@DynamoDBTable(tableName = "reported-swabs")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Swab {

	private static final Logger logger = LoggerFactory.getLogger(Swab.class);

	public static final int TRACING_HISTORY = 14;
	@DynamoDBHashKey(attributeName = "fiscal-code")
	@JsonProperty
	private String fiscalCode;

	@DynamoDBAttribute(attributeName = "reported-on")
	@JsonProperty
	private Integer reportedOn;

	@DynamoDBAttribute(attributeName = "state")
	@JsonProperty
	@DynamoDBTyped(DynamoDBMapperFieldModel.DynamoDBAttributeType.S)
	private SwabState state;

	@DynamoDBAttribute(attributeName = "total-risk")
	@JsonProperty
	private String totalRisk;

	@DynamoDBIgnore
	public boolean isValid() {

		return !StringUtils.isNullOrEmpty(fiscalCode) && !StringUtils.isNullOrEmpty(totalRisk) && Util.isValidDate(reportedOn) && state != null;
	}

	@DynamoDBIgnore
	@SneakyThrows
	public Swab update(final Swab swab) {

		if (!fiscalCode.equalsIgnoreCase(swab.fiscalCode)) {
			throw new SwabManagementException();
		}

		logger.info("Updating. Current: {}", this);
		logger.info("Updating. New item: {}", swab);

		final BigDecimal currentRisk = new BigDecimal(totalRisk);
		final BigDecimal newRisk = new BigDecimal(swab.getTotalRisk());

		if (Util.numberToDate(reportedOn).isBefore(LocalDate.now().minusDays(TRACING_HISTORY))
				|| newRisk.compareTo(currentRisk) > 0
				|| !swab.state.equals(SwabState.IN_QUEUE)) {

			this.state = swab.state;
			this.reportedOn = swab.reportedOn;
			this.totalRisk = swab.totalRisk;
		}

		return this;
	}

	public enum SwabState {

		IN_QUEUE, WAITING_FOR_RESULT, POSITIVE, NEGATIVE
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder("Swab{");
		sb.append("fiscalCode='").append(fiscalCode).append('\'');
		sb.append(", reportedOn=").append(reportedOn);
		sb.append(", state=").append(state);
		sb.append(", totalRisk='").append(totalRisk).append('\'');
		sb.append('}');
		return sb.toString();
	}
}