package com.vegpal.dataload.service.impl;

import com.vegpal.dataload.service.DataLoaderService;
import com.vegpal.dataload.loader.VegpalDataLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author sravantatikonda
 */

@Service
@Slf4j
public class DataLoaderServiceImpl implements DataLoaderService {

  @Autowired
  private VegpalDataLoader vegpalDataLoader;

  @Override
  public Boolean loadJsonFile(Integer count) throws Exception {
    // Load the cache
    vegpalDataLoader
        .loadCSVValues(
            "/users/sravantatikonda/DATING/dataload/src/main/resources/loaderinput/");

    vegpalDataLoader.createJsonDocument(count);

    log.info("*********** Successfully Created {} records ************", count);
    return true;
  }
}

