package it.cs.contact.tracing.be.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LambdaResponse {

	private Boolean isBase64Encoded;
	private Integer statusCode;
	private String body;
}
