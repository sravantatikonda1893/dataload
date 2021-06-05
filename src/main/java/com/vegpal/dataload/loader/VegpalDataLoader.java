package com.vegpal.dataload.loader;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vegpal.dataload.util.Address;
import com.vegpal.dataload.util.AddressDoc;
import com.vegpal.dataload.util.AddressGen;
import com.vegpal.dataload.util.AddressUnit;
import com.vegpal.dataload.util.GenderType;
import com.vegpal.dataload.util.Prompt;
import com.vegpal.dataload.util.VegpalUserProfile;
import com.vegpal.dataload.util.vegpalUserLoc;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;


/**
 * @author sravantatikonda
 */

@Component
@Data
@Slf4j
public class VegpalDataLoader {

  public static final String EMAILS = "emails";
  public static final String HEADLINES = "headlines";
  public static final String FIRST_NAMES = "firstname";
  public static final String PHONE_NUMBERS = "phonenumbers";
  public static final String JOB_ROLES = "roles";
  public static final String IMAGES = "images";
  public static final String GENDERS = "genderpref";
  public static final String LIFESTYLE = "lifestyle";
  public static final String PROMPTS = "prompts";
  public static final String SEXUALORIENTATION_TYPE = "sexOrientType";
  public static final String ADDRESSES_JSON = "addresses.json";

  private static Random random = new Random();
  private Map<String, List<String>> cacheMap = new HashMap<>();
  private List<Address> addresses = new ArrayList<>();

  public static Date parseDate(String date) {
    try {
      return new SimpleDateFormat("yyyy-MM-dd").parse(date);
    } catch (ParseException e) {
      return null;
    }
  }

  private Boolean getRandomBoolean() {
    return random.nextBoolean();
  }

  private int getRandomInt(int min, int max) {
    return random.ints(min, max).findFirst().getAsInt();
  }

  private void createAddressModels(String resourcePath) {

    ObjectMapper objectMapper = new ObjectMapper();
    AddressGen addressGen = new AddressGen();
    try {
      addressGen = objectMapper
          .readValue(new File(resourcePath + ADDRESSES_JSON), AddressGen.class);
    } catch (IOException e) {
      log.error("Error while loading addresses file");
    }

    for (AddressUnit addressUnit : addressGen.getAddresses()) {
      Address address = new Address();
      if (StringUtils.isNotBlank(addressUnit.getCity())) {
        address.setCity(addressUnit.getCity());
        address.setState(addressUnit.getState());
        address.setZipCode(addressUnit.getPostalCode());
        address.setLat(addressUnit.getCoordinates().getLat());
        address.setLng(addressUnit.getCoordinates().getLng());
        address.setCountry("United States");
      } else {
        address.setCity("Waltham");
        address.setState("MA");
        address.setZipCode("02453");
        address.setCountry("United States");
      }
      addresses.add(address);
    }
  }

  public void loadCSVValues(String resourcePath) throws Exception {
    List<String> randomStrings = new ArrayList<>();
    randomStrings.add(EMAILS);
    randomStrings.add(FIRST_NAMES);
    randomStrings.add(PHONE_NUMBERS);
    randomStrings.add(JOB_ROLES);
    randomStrings.add(LIFESTYLE);
    randomStrings.add(PROMPTS);
    randomStrings.add(HEADLINES);
    randomStrings.add(IMAGES);

    for (String randomString : randomStrings) {
      if (cacheMap.get(randomString) == null) {
        List<String> values = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(
            new FileReader(resourcePath + randomString + ".csv"))) {
          String line;
          while ((line = br.readLine()) != null) {
            values.add(line);
          }
          cacheMap.put(randomString, values);

        } catch (Exception e) {
          throw new Exception("Missing CSV file for: " + randomString, e);
        }
      }
    }

    if (addresses.isEmpty()) {
      createAddressModels(resourcePath);
    }

    // Loading enums
    cacheMap.put(GENDERS,
        GenderType.stream().map(GenderType::getValue).collect(Collectors.toList()));
  }

  public void createJsonDocument(Integer count) {
    List<VegpalUserProfile> userProfiles = createVegpalUserProfiles(count);
    ObjectMapper mapper = new ObjectMapper();
    String jsonString = null;
    try {
      jsonString = mapper.writeValueAsString(userProfiles);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    System.out.println(jsonString);
    BufferedWriter writer = null;
    try {
      writer = new BufferedWriter(
          new FileWriter("/users/sravantatikonda/DATING/dataload/newusers.json"));
    } catch (IOException e) {
      e.printStackTrace();
    }
    try {
      writer.write(jsonString);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private List<VegpalUserProfile> createVegpalUserProfiles(int count) {
    List<VegpalUserProfile> userProfiles = new ArrayList<>();

    for (int i = 0; i < count; i++) {
      VegpalUserProfile userProfile = new VegpalUserProfile();

      Address address = addresses.get(getRandomInt(0, addresses.size()));
      AddressDoc addressDoc = new AddressDoc();
      addressDoc.setCity(address.getCity());
      addressDoc.setState(address.getState());
      addressDoc.setCountry(address.getCountry());

      userProfile.setAddress(addressDoc);

      int feet = getRandomInt(3, 6);
      int inches = getRandomInt(1, 11);
      userProfile.setHeight(feet + "'" + inches + " (" + (feet * 12 + inches) * 2.54 + "cm)");

      userProfile.setCreatedAt(ZonedDateTime.now());

      userProfile.setCareerAmbition(
          cacheMap.get(HEADLINES).get(new Random().nextInt(cacheMap.get(HEADLINES).size())));

      userProfile.setUserId(UUID.randomUUID().toString());

      userProfile.setDeleteReason("");

      userProfile
          .setEmail(cacheMap.get(EMAILS).get(new Random().nextInt(cacheMap.get(EMAILS).size())));

      String firstName = cacheMap.get(FIRST_NAMES)
          .get(new Random().nextInt(cacheMap.get(FIRST_NAMES).size()));

      userProfile.setFirstName(firstName);

      userProfile.setFcmToken(UUID.randomUUID().toString());

      ZonedDateTime dob = ZonedDateTime.now().minusYears(getRandomInt(18, 80));
      userProfile.setDateOfBirth(dob.toString());

      userProfile.setAge(ZonedDateTime.now().minusYears(dob.getYear()).getYear());

      List<String> genders = new ArrayList<>();
      if (getRandomBoolean()) {
        genders.add(GenderType.getGenderType(cacheMap.get(GENDERS)
            .get(new Random().nextInt(cacheMap.get(GENDERS).size()))).toString());
        genders.add(GenderType.getGenderType(cacheMap.get(GENDERS)
            .get(new Random().nextInt(cacheMap.get(GENDERS).size()))).toString());
      } else {
        genders.add(GenderType.getGenderType(cacheMap.get(GENDERS)
            .get(new Random().nextInt(cacheMap.get(GENDERS).size()))).toString());
      }

      userProfile.setGender(genders);

      userProfile.setHometown(address.getCity());

      List<String> imagesUrls = new ArrayList<>();
      imagesUrls.add(cacheMap.get(IMAGES)
          .get(new Random().nextInt(cacheMap.get(IMAGES).size())));
      imagesUrls.add(cacheMap.get(IMAGES)
          .get(new Random().nextInt(cacheMap.get(IMAGES).size())));
      imagesUrls.add(cacheMap.get(IMAGES)
          .get(new Random().nextInt(cacheMap.get(IMAGES).size())));
      imagesUrls.add(cacheMap.get(IMAGES)
          .get(new Random().nextInt(cacheMap.get(IMAGES).size())));
      imagesUrls.add(cacheMap.get(IMAGES)
          .get(new Random().nextInt(cacheMap.get(IMAGES).size())));
      imagesUrls.add(cacheMap.get(IMAGES)
          .get(new Random().nextInt(cacheMap.get(IMAGES).size())));
      userProfile.setImageURls(imagesUrls);

      userProfile.setInstagram("vegpalapp");

      userProfile.setIsApproved(getRandomBoolean());

      userProfile.setNotificationCount(getRandomInt(1, 30));

      userProfile.setLikesCount(getRandomInt(1, 30));

      userProfile.setReportId("");

      userProfile.setLifestyle(cacheMap.get(LIFESTYLE)
          .get(new Random().nextInt(cacheMap.get(LIFESTYLE).size())));

      vegpalUserLoc location = new vegpalUserLoc();
      location.setGeoHash(UUID.randomUUID().toString());
      location.setGeoPoint(address.getLat() + "," + address.getLng());
      userProfile.setLocation(location);

      userProfile.setProfession(cacheMap.get(JOB_ROLES)
          .get(new Random().nextInt(cacheMap.get(JOB_ROLES).size())));

      userProfile.setYearsVegan("Five/5 years");

      userProfile.setWhyVegan("Because I care and I'm compassionate");

      userProfile.setStatus("Active");

      userProfile.setUpdatedAt(ZonedDateTime.now().toString());

      List<Prompt> prompts = new ArrayList<>();

      Prompt prompt1 = new Prompt();
      prompt1.setPrompt(cacheMap.get(PROMPTS)
          .get(new Random().nextInt(cacheMap.get(PROMPTS).size())));
      prompt1.setPromptValue("Sravan's prompt-1 value");

      Prompt prompt2 = new Prompt();
      prompt2.setPrompt(cacheMap.get(PROMPTS)
          .get(new Random().nextInt(cacheMap.get(PROMPTS).size())));
      prompt2.setPromptValue("Sravan's prompt-2 value");

      Prompt prompt3 = new Prompt();
      prompt3.setPrompt(cacheMap.get(PROMPTS)
          .get(new Random().nextInt(cacheMap.get(PROMPTS).size())));
      prompt3.setPromptValue("Sravan's prompt-3 value");

      prompts.add(prompt1);
      prompts.add(prompt2);
      prompts.add(prompt3);

      userProfile.setPrompts(prompts);

      userProfile.setPhoneNum(cacheMap.get(PHONE_NUMBERS)
          .get(new Random().nextInt(cacheMap.get(PHONE_NUMBERS).size())));
      userProfiles.add(userProfile);
    }
    return userProfiles;
  }
}
