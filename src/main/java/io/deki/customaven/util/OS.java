package io.deki.customaven.util;

/**
 * Utility class to identify the operating system the program is running on
 *
 * @author Deki on 13.07.2021
 * @project CustoMaven
 **/
public enum OS {

  WINDOWS("win"),
  MAC("mac"),
  UNIX("nix", "nux", "aix");

  private final String[] identifiers;

  OS(String... identifiers) {
    this.identifiers = identifiers;
  }

  public static OS getOs() {
    String os = System.getProperty("os.name").toLowerCase();
    for (OS value : OS.values()) {
      for (String identifier : value.identifiers) {
        if (os.contains(identifier)) {
          return value;
        }
      }
    }
    return UNIX;
  }

}
