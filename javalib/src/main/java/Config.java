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

  private final String configDir;
  private final ObjectMapper yamlMapper;
  private final ObjectMapper mapper;

  public Config(){
    this(defaultConfigDir());
  }

  public Config(String configDir){
    this.configDir = configDir;
    this.yamlMapper = new ObjectMapper(new YAMLFactory())
      .addMixIn(ClinicInfoDTO.class, ClinicInfoMixIn.class)
      .addMixIn(DiseaseExampleDTO.class, DiseaseExampleMixIn.class)
      .addMixIn(PracticeConfigDTO.class, PracticeConfigMixIn.class);
    this.mapper = new ObjectMapper();
 }

    private static class ClinicInfoMixIn {
        @JsonProperty("postal-code")
        public String postalCode;
        @JsonProperty("doctor-name")
        public String doctorName;
    }

    private static class DiseaseExampleMixIn {
        @JsonProperty("adj-list")
        public List<String> adjList;
    }

    private static class PracticeConfigMixIn {
        @JsonProperty("kouhatsu-kasan")
        public String kouhatsuKasan;
    }


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

  public ClinicInfoDTO getClinicInfo() throws Exception {
      File file = new File(configDir, "clinic-info.yml");
      return fromYamlFile(file, new TypeReference<>() {
      });
  }

  private <T> T fromYamlFile(File file, TypeReference<T> typeRef) throws Exception {
    return yamlMapper.readValue(file, typeRef);
  }

}