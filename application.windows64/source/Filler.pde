class Filler extends Polygon {
  float tessAngle, baseRadius;
  Tesselation baseTess;

  Filler() {
  }
  Filler(float _cx, float _cy, Tesselation _baseTess, float _rotation) {

    cx = _cx; 
    cy = _cy;
    baseTess = _baseTess;
    rotation =_rotation;
    numSides = baseTess.numSides;
    update();
  }


  void update() {
    super.update();
    // vertices = new PVector[numSides];
    tessAngle = baseTess.baseAngle;
    baseRadius = baseTess.radius;
    numSides = baseTess.numSides;
    radius = baseRadius/3;
    baseAngle = TWO_PI/numSides;
    interiorOffset = baseAngle /2;
    vertexVector = PVector.fromAngle(rotation);
    vertexVector.mult(radius);
  }
}

