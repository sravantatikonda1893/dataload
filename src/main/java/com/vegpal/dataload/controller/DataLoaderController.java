package com.vegpal.dataload.controller;

import com.vegpal.dataload.service.DataLoaderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author sravantatikonda
 */

@RestController
@Api(value = "DataLoaderController", description = "This controller contains endpoints for managing and retrieving user/s")
@RequestMapping
public class DataLoaderController {

  @Autowired
  private DataLoaderService dataLoaderService;

  @ApiOperation(value = "loadJsonFile", notes = "Loads the JSON file with sufficient amount of data")
  @PostMapping(value = "/json-load/{count}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Boolean> loadJsonFile(@PathVariable Integer count) throws Exception {
    return new ResponseEntity<>(dataLoaderService.loadJsonFile(count), HttpStatus.OK);
  }
}

