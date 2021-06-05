package com.vegpal.dataload.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import lombok.Data;

/**
 * @author sravantatikonda
 *///May 26, 2021 at 9:34:52 AM UTC-4
@Data
public class VegpalUserProfile implements Serializable {

  private static final long serialVersionUID = -3707819200740751550L;

  private String userId;

  private AddressDoc address;

  private Integer age;

  @JsonProperty(value = "career_ambition")
  private String careerAmbition;

  @JsonProperty(value = "created_at")
  private ZonedDateTime createdAt;

  @JsonProperty(value = "date_of_birth")
  private String dateOfBirth;

  private String deleteReason;

  private String email;

  @JsonProperty(value = "fcm_token")
  private String fcmToken;

  @JsonProperty(value = "first_name")
  private String firstName;

  private List<String> gender;

  private String hometown;

  private String instagram;

  private Boolean isApproved;

  private String lifestyle;

  private vegpalUserLoc location;

  @JsonProperty(value = "phone_number")
  private String phoneNum;

  @JsonProperty(value = "post")
  private List<String> imageURls;

  private String profession;

  private List<Prompt> prompts;

  private String height;

  private String status;

  @JsonProperty(value = "updated_at")
  private String updatedAt;

  @JsonProperty(value = "why_vegan")
  private String whyVegan;

  @JsonProperty(value = "years_vegan")
  private String yearsVegan;

  @JsonProperty(value = "like_count")
  private Integer likesCount;

  @JsonProperty(value = "notification_count")
  private Integer notificationCount;

  @JsonProperty(value = "report_id")
  private String reportId;

/*  //I want "created_at": May 28, 2021 at 9:58:24 AM UTC-4
  public void setCreatedAt(ZonedDateTime createdAt) {
    this.createdAt = ZonedDateTime.parse(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL).format(createdAt));
  }*/
}
