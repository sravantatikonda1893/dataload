package com.vegpal.dataload.util;

import java.util.stream.Stream;

/**
 * Enumeration defining different Listing Statuses
 *
 * @author sravantatikonda
 */
public enum GenderType implements UserDefinedEnum {

  Man("Man"),
  Woman("Woman"),
  Transgender("Transgender"),
  NonBinary("Non-Binary");

  private final String value;

  GenderType(String value) {
    this.value = value;
  }

  /**
   * Convert from String to GenderType enumeration
   *
   * @param value the String to convert from
   * @return the enumeration, or null if value was null
   */
  public static GenderType getGenderType(String value) {
    if (value == null) {
      return null;
    }
    for (GenderType listingStatus : values()) {
      if (listingStatus.value.equalsIgnoreCase(value)) {
        return listingStatus;
      }
    }
    throw new IllegalArgumentException("Unknown type to map from: " + value);
  }

  public static Stream<GenderType> stream() {
    return Stream.of(GenderType.values());
  }

  @Override
  public String getValue() {
    return value;
  }
}
