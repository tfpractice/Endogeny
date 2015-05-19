class SegmentedFiller extends SegmentedPolygon {
  float tessAngle, baseRadius;
  Tesselation baseTess;

  SegmentedFiller() {
  }
  SegmentedFiller(float _cx, float _cy, Tesselation _baseTess, float _rotation) {

    cx = _cx; 
    cy = _cy;
    baseTess = _baseTess;
    rotation =_rotation;
    int origPPE = ((SegmentedPolygon)(baseTess.baseObject)).pointsPerEdge;
    pointsPerEdge = (int)pow(origPPE, baseTess.depth);
    // pointsPerEdge = (int)pow(3, baseTess.depth);
    numSides = baseTess.numSides;
    centrality = false;
    update();
  }


  void update() {
    super.update();
    baseRadius = baseTess.radius;
    numSides = baseTess.numSides;
    radius = baseRadius/3;
    baseAngle = TWO_PI/numSides;
    interiorOffset = baseAngle /2;
    vertexVector = PVector.fromAngle(rotation);
    vertexVector.mult(radius);
    centrality = false;

    pointCount = pointsPerEdge * numSides;

    partitionPoints = new PVector[pointCount];
    nextVertex = new PVector();
    nextVertex = vertexVector.get();
    nextVertex.rotate(PI/2);
  }
};

