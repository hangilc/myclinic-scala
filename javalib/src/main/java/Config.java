package dev.myclinic.java;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class Config {

  public Config(){
    this(defaultConfigDir());
  }

  public Config(String configDir){
    this.configDir = configDir;
  }

  private String configDir;

  private static String defaultConfigDir() {
    String configDir = System.getenv("MYCLINIC_CONFIG_DIR");
    if( configDir == null ){
      throw new RuntimeException("Cannot find env var: MYCLINIC_CONFIG_DIR.");
    }
    return configDir;
  }

  public HoukatsuKensa getHoukatsuKensa() {
      File file = new File(configDir, "houkatsu-kensa.xml");
      try {
          return HoukatsuKensa.fromXmlFile(file);
      } catch (Exception e) {
          throw new RuntimeException("Failed to read houkatsu-kensa from: " + file.toString());
      }
  }


}