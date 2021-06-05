package com.vegpal.dataload.util;

import java.io.Serializable;
import lombok.Data;

/**
 * @author sravantatikonda
 */
@Data
public class Address implements Serializable {

  private static final long serialVersionUID = 1L;

  private String city;

  private String state;

  private String zipCode;

  private String country;

  private String lat;

  private String lng;
}
