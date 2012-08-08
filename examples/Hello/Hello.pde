import net.hellonico.pirc.*;

import net.hellonico.colorlib.*;
import net.hellonico.potato.*;
import colorLib.webServices.*;

IRCLibrary irc; 
ColorLibrary k;

void setup() {
  size(400,400);
  smooth();
  background(0);
  
  k = new ColorLibrary(this);
  irc = new IRCLibrary(this);
  irc.register("kuler");
  irc.register("clean");
    
  text("connected:"+irc.isConnected(), 40, 200);
}

void draw() {

}

void mousePressed() {
  irc.sendMessage(irc.getChannels()[0], "Hello@"+mouseX+":"+mouseY);  
}

/*
	call back inherited from the register("clean")
*/
void onClean(String message) {
  background(0);  
}

/*
	call back inherited from the register("kuler")
*/
void onKuler(String message) {
    KulerTheme[] kt = (KulerTheme[]) k.search(message);
    
    for (int i = 0; i < kt.length; i++) {
      for (int j = 0; j < kt[i].totalSwatches(); j++) {
        fill(kt[i].getColor(j));
        rect(j*90, i*20, 90, 20);
      }
    }
}