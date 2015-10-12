import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class ultimatepixelsort extends PApplet {

int mode = 1;

//MODE:
//0 -> black
//1 -> bright
//2 -> white
//b(16777216)

PImage img;
String imgFileName = "cat";
String fileType = "jpg";

int loops = 1000;

int blackValue = -16000000;
int brigthnessValue = 100;
int whiteValue = -13000000;

int row = 0;
int column = 0;

boolean saved = false;

public void setup() {
  img = loadImage(imgFileName+"."+fileType);
  
  image(img, 0, 0);
}


public void draw() {
  while(column < width-1) {
    img.loadPixels(); 
    sortColumn();
    column++;
    img.updatePixels();
    println ("column"+frameCount);

  }
  
  while(row < height-1) {
    img.loadPixels(); 
    sortRow();
    row++;
    img.updatePixels();
  }
  
  image(img,0,0);
  if(!saved && frameCount >= loops) {
    saveFrame(imgFileName+"_"+mode+".png");
    saved = true;
    println("DONE"+frameCount);

    System.exit(0);
  }
  
 }


public void sortRow() {
  int x = 0;
  int y = row;
  int xend = 0;
  
  while(xend < width-1) {
    switch(mode) {
      case 0:
        x = getFirstNotBlackX(x, y);
        xend = getNextBlackX(x, y);
        break;
      case 1:
        x = getFirstBrightX(x, y);
        xend = getNextDarkX(x, y);
        break;
      case 2:
        x = getFirstNotWhiteX(x, y);
        xend = getNextWhiteX(x, y);
        break;
      default:
        break;
    }
    
    if(x < 0) break;
    
    int sortLength = xend-x;
    
    int[] unsorted = new int[sortLength];
    int[] sorted = new int[sortLength];
    
    for(int i=0; i<sortLength; i++) {
      unsorted[i] = img.pixels[x + i + y * img.width];


    }
    
    sorted = sort(unsorted);
    
    for(int i=0; i<sortLength; i++) {
      img.pixels[x + i + y * img.width] = sorted[i];      
    }
    
    x = xend+1;

  }
}


public void sortColumn() {
  int x = column;
  int y = 0;
  int yend = 0;
  
  while(yend < height-1) {
    switch(mode) {
      case 0:
        y = getFirstNotBlackY(x, y);
        yend = getNextBlackY(x, y);
        break;
      case 1:
        y = getFirstBrightY(x, y);
        yend = getNextDarkY(x, y);
        break;
      case 2:
        y = getFirstNotWhiteY(x, y);
        yend = getNextWhiteY(x, y);
        break;
      default:
        break;
    }
    
    if(y < 0) break;
    
    int sortLength = yend-y;
    
    int[] unsorted = new int[sortLength];
    int[] sorted = new int[sortLength];
    
    for(int i=0; i<sortLength; i++) {
      unsorted[i] = img.pixels[x + (y+i) * img.width];
    }
    
    sorted = sort(unsorted);
    
    for(int i=0; i<sortLength; i++) {
      img.pixels[x + (y+i) * img.width] = sorted[i];
    }
    
    y = yend+1;
  }
}


//BLACK
public int getFirstNotBlackX(int _x, int _y) {
  int x = _x;
  int y = _y;
  int c;
  while((c = img.pixels[x + y * img.width]) < blackValue) {
    x++;
    if(x >= width) return -1;
  }
  return x;
}

public int getNextBlackX(int _x, int _y) {
  int x = _x+1;
  int y = _y;
  int c;
  while((c = img.pixels[x + y * img.width]) > blackValue) {
    x++;
    if(x >= width) return width-1;
  }
  return x-1;
}

//BRIGHTNESS
public int getFirstBrightX(int _x, int _y) {
  int x = _x;
  int y = _y;
  int c;
  while(brightness(c = img.pixels[x + y * img.width]) < brigthnessValue) {
    x++;
    if(x >= width) return -1;
  }
  return x;
}

public int getNextDarkX(int _x, int _y) {
  int x = _x+1;
  int y = _y;
  int c;
  while(brightness(c = img.pixels[x + y * img.width]) > brigthnessValue) {
    x++;
    if(x >= width) return width-1;
  }
  return x-1;
}

//WHITE
public int getFirstNotWhiteX(int _x, int _y) {
  int x = _x;
  int y = _y;
  int c;
  while((c = img.pixels[x + y * img.width]) > whiteValue) {
    x++;
    if(x >= width) return -1;
  }
  return x;
}

public int getNextWhiteX(int _x, int _y) {
  int x = _x+1;
  int y = _y;
  int c;
  while((c = img.pixels[x + y * img.width]) < whiteValue) {
    x++;
    if(x >= width) return width-1;
  }
  return x-1;
}


//BLACK
public int getFirstNotBlackY(int _x, int _y) {
  int x = _x;
  int y = _y;
  int c;
  if(y < height) {
    while((c = img.pixels[x + y * img.width]) < blackValue) {
      y++;
      if(y >= height) return -1;
    }
  }
  return y;
}

public int getNextBlackY(int _x, int _y) {
  int x = _x;
  int y = _y+1;
  int c;
  if(y < height) {
    while((c = img.pixels[x + y * img.width]) > blackValue) {
      y++;
      if(y >= height) return height-1;
    }
  }
  return y-1;
}

//BRIGHTNESS
public int getFirstBrightY(int _x, int _y) {
  int x = _x;
  int y = _y;
  int c;
  if(y < height) {
    while(brightness(c = img.pixels[x + y * img.width]) < brigthnessValue) {
      y++;
      if(y >= height) return -1;
    }
  }
  return y;
}

public int getNextDarkY(int _x, int _y) {
  int x = _x;
  int y = _y+1;
  int c;
  if(y < height) {
    while(brightness(c = img.pixels[x + y * img.width]) > brigthnessValue) {
      y++;
      if(y >= height) return height-1;
    }
  }
  return y-1;
}

//WHITE
public int getFirstNotWhiteY(int _x, int _y) {
  int x = _x;
  int y = _y;
  int c;
  if(y < height) {
    while((c = img.pixels[x + y * img.width]) > whiteValue) {
      y++;
      if(y >= height) return -1;
    }
  }
  return y;
}

public int getNextWhiteY(int _x, int _y) {
  int x = _x;
  int y = _y+1;
  int c;
  if(y < height) {
    while((c = img.pixels[x + y * img.width]) < whiteValue) {
      y++;
      if(y >= height) return height-1;
    }
  }
  return y-1;
}
  public void settings() {  size(640,640); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "ultimatepixelsort" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
