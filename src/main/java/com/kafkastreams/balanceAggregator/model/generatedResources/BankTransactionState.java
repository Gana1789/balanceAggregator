/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package com.kafkastreams.balanceAggregator.model.generatedResources;
@org.apache.avro.specific.AvroGenerated
public enum BankTransactionState implements org.apache.avro.generic.GenericEnumSymbol<BankTransactionState> {
  CREATED, REJECTED, FAILED, APPROVED  ;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"enum\",\"name\":\"BankTransactionState\",\"namespace\":\"com.kafkastreams.balanceAggregator.model.generatedResources\",\"symbols\":[\"CREATED\",\"REJECTED\",\"FAILED\",\"APPROVED\"],\"default\":\"CREATED\"}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  @Override
  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
}
