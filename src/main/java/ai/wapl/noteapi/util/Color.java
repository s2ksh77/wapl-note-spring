package ai.wapl.noteapi.util;

import java.util.Random;

public class Color {
  private static final String[] colorArray = {
      "#C84847","#F29274","#F6C750",
      "#77B69B","#679886","#3A7973",
      "#77BED3","#5C83DA","#8F91E7",
      "#DF97AA","#CA6D6D"
  };

  public static String getRandomColor() {
    return colorArray[new Random().nextInt(colorArray.length)];
  }

}
