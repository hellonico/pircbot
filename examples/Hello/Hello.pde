import net.hellonico.pirc.*;

import net.hellonico.colorlib.*;
import net.hellonico.potato.*;
import colorLib.webServices.*;

IRCLibrary irc; 
ColorLibrary k;

void setup() {
  size(1280,1024);
  smooth();
  background(0);
  
  k = new ColorLibrary(this);
  irc = new IRCLibrary(this);
  irc.register("kuler");
  irc.register("clean");
  irc.register("echo");
  irc.register("text");
  
  PFont f = loadFont("hira.vlw");
  textFont(f, 120); 
  text("connected:"+irc.isConnected(), 40, 200);
}

void draw() {

}

void mousePressed() {
  irc.send("Hello@"+mouseX+":"+mouseY);  
}

void onText(String message) {
  background(0);
  text(message, 100, 200); 
}

void onEcho(String message) {
  irc.send(message);  
}

void onClean(String message) {
  background(0);  
}

void onKuler(String message) {
    KulerTheme[] kt = (KulerTheme[]) k.search(message);
    
    for (int i = 0; i < kt.length; i++) {
      for (int j = 0; j < kt[i].totalSwatches(); j++) {
        fill(kt[i].getColor(j));
        rect(j*90, i*20, 90, 20);
      }
    }
}
