Polygon eg;
SegmentedPolygon seg;
Tesselation egT, midNightBiscuit, metaSeg, betaSeg, gammaSeg;
BaseObjectFactory fact = new BaseObjectFactory();
RhomboidFiller rF;

void setup() {
  frameRate(7);
  noStroke();
  fill(255, 0, 255, 255);
  //noFill();
  stroke(255, 0, 255, 255);
  size(2500, 1300);
  eg = new Polygon(500, 500, 100, 6, 0);

  seg = new SegmentedPolygon (500, 500, 50, 6 , 0, 10, true, false);
  midNightBiscuit= new Tesselation(1250, 650, seg, 4, true);
  metaSeg = new Tesselation(1259, 659, midNightBiscuit, 1, true);
  betaSeg = new Tesselation(1259, 659, metaSeg, 1, true);
  gammaSeg = new Tesselation(1259, 659, betaSeg, 1, true);
  background(255, 255, 255, 255);

  // seg.tesselate();
  background(0, 0, 0, 255);

  midNightBiscuit.tesselate();
  rF = new RhomboidFiller(400, 400, metaSeg, 0);
  //rF.tesselate();5rfdcx
}

void draw() {
  // background(255, 255, 255, 255);


  // seg.addPoints();
  // seg.spin(TWO_PI/120);
  // seg.tesselate();
  //midNightBiscuit.tesselate();
  // midNightBiscuit.central.spin(TWO_PI/120);
  //midNightBiscuit.tesselate();
  /*
     midNightBiscuit.central.spin(-1*(TWO_PI/120));
   // metaSeg.central.spin(-1*(TWO_PI/120));
   
   for (int i = 0; i < midNightBiscuit.numSides; ++i) {
   //  midNightBiscuit.peripherals[i].spin(TWO_PI/120);
   midNightBiscuit.peripherals[i].spin((TWO_PI/120));
   //midNightBiscuit.peripherals[i].tesselate();
   //midNightBiscuit.peripherals[i].peripherals[i].spin(TWO_PI/120);
   
   } */
  //betaSeg.tesselate();

  // midNightBiscuit.tesselate();



  // metaSeg.tesselate();
}

