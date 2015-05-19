Polygon eg;
SegmentedPolygon seg;
Tesselation egT, midNightBiscuit, metaSeg, betaSeg, gammaSeg;
BaseObjectFactory fact = new BaseObjectFactory();
RhomboidFiller rF;
void setup() {
  frameRate(7);
  noStroke();
  fill(255, 0, 255, 255);
  stroke(255, 0, 255, 255);
  size(2500, 1300);
  eg = new Polygon(500, 500, 100, 6, 0);
  seg = new SegmentedPolygon (500, 500, 50, 6 , 0, 10, true, false);
  midNightBiscuit= new Tesselation(1250, 650, seg, 4, true);
  metaSeg = new Tesselation(1259, 659, midNightBiscuit, 1, true);
  betaSeg = new Tesselation(1259, 659, metaSeg, 1, true);
  gammaSeg = new Tesselation(1259, 659, betaSeg, 1, true);
  background(255, 255, 255, 255);
  background(0, 0, 0, 255);
  midNightBiscuit.tesselate();
  rF = new RhomboidFiller(400, 400, metaSeg, 0);
}
void draw() {
  
}
