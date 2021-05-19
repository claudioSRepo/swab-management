package it.cs.contact.tracing.be.repository;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import it.cs.contact.tracing.be.entity.Swab;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SwabRepository {

	private static final Logger logger = LoggerFactory.getLogger(SwabRepository.class);

	final DynamoDBMapper dbMapper;

	public SwabRepository() {

		this.dbMapper = new DynamoDBMapper(AmazonDynamoDBClientBuilder.standard().build());
	}

	public List<Swab> getPositiveSwabsByCF(final String cf) {

		logger.info("Get Positive contact By FC : {}", cf);

		try {

			final Map<String, AttributeValue> filters = new HashMap<>();
			filters.put(":f1", new AttributeValue().withS(cf));

			final Map<String, String> expressionAttributeNames = new HashMap<>();
			expressionAttributeNames.put("#n1", "fiscal-code");

			final DynamoDBQueryExpression<Swab> queryExpression = new DynamoDBQueryExpression<Swab>()
					.withKeyConditionExpression("#n1 = :f1")
					.withExpressionAttributeValues(filters)
					.withExpressionAttributeNames(expressionAttributeNames);

			return dbMapper.query(Swab.class, queryExpression);
		}
		catch (final Exception e) {
			logger.error("Error extracting data", e);
			return Collections.emptyList();
		}
	}

	public boolean create(final Swab swabEntity) {

		try {
			logger.info("Create swab entity: {}", swabEntity.getFiscalCode());

			if (swabEntity.isValid()) {

				dbMapper.save(swabEntity);
				return true;
			}
		}
		catch (final Exception e) {
			logger.error("Error creating swabEntity.", e);
		}
		return false;
	}
}