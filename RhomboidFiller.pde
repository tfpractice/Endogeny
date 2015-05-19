class RhomboidFiller extends Filler {
  float nearRadius, farRadius;
  PVector nearVector, farVector;
  RhomboidFiller() {
  }
  RhomboidFiller(float _cx, float _cy, Tesselation _baseTess, float _rotation) {
    cx = _cx; 
    cy = _cy;
    baseTess = _baseTess;
    rotation =_rotation;
    numSides = 4;
    nearVector = new PVector();
    farVector = new PVector();
    update();
  }
  void update() {
    baseRadius = baseTess.radius;
    numSides = 4;
        nearRadius = baseRadius/2;
    vertices = new PVector[numSides];
    nearRadius = baseRadius/6;
    farRadius = (baseRadius/3)* sin((PI - tessAngle)/2);
    farRadius = (baseRadius/3)/(2*(tan(PI/baseTess.numSides)));
    nearVector = PVector.fromAngle(rotation);
    nearVector.mult(nearRadius);
    farVector = PVector.fromAngle(rotation+(PI/2));
    farVector.mult(farRadius);
    baseAngle = (TWO_PI/(numSides));
    center = new PVector(cx, cy);
    interiorOffset = (baseAngle/2);
    tessAngle = baseTess.baseAngle;
    vertices = new PVector[numSides];
    surround = new Polygon[numSides];
  }
  void establishVertices() {
    for (int v = 0; v < numSides; v+=2) {
      PVector nearCopy = new PVector();
      nearCopy = nearVector.get();
      nearCopy.rotate(v*(PI/2));
      PVector farCopy = new PVector();
      farCopy = farVector.get();
      farCopy.rotate(v*(PI/2));
      nearCopy.add(center);
      farCopy.add(center);
      vertices[v] = new PVector(nearCopy.x, nearCopy.y);
      append(vertices, vertices[v]);
      vertices[v+1] = new PVector(farCopy.x, farCopy.y);
      append(vertices, vertices[v+1]);
    }
  }
  void display() {
    float depthColor = (baseTess.depth)*64/255;
    establishVertices();
    beginShape();
    for (int v=0; v < numSides; v++) {
      vertex(vertices[v].x, vertices[v].y);
    }
    endShape(CLOSE);
  }
  void tesselate() {
    display();
  }
};
