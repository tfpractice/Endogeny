class Polygon implements Tesselable {
  int type = 0;
  int numSides;
  float cx, cy, radius, newRadius, rotation, inscribedMagnitude, circumMagnitude, baseAngle, interiorOffset, tesselationMagnitude;
  PVector center, vertexVector, inscribedVector, circumVector, tesselationVector;
  PVector [] vertices;
  Polygon [] surround;
  Polygon []	central;

  Polygon() {
  }
  Polygon(float _cx, float _cy, float _radius, int _numSides, float _rotation) {
    cx = _cx;
    cy = _cy;
    radius = _radius;
    numSides = _numSides;
    rotation = _rotation;

    update();
  }

  void update() {
    baseAngle = (TWO_PI/(numSides));
    center = new PVector(cx, cy);
    interiorOffset = (baseAngle/2);

    vertices = new PVector[numSides];
    surround = new Polygon[numSides];

    //inscribedMagnitude = (radius* sin((PI/2)-((interiorOffset))))/(sin(interiorOffset));
    inscribedMagnitude = radius * cos(PI/numSides);
    circumMagnitude = radius;

    circumVector = PVector.fromAngle(rotation); 
    circumVector.mult(circumMagnitude);
    vertexVector = circumVector;
    inscribedVector = PVector.fromAngle(rotation + interiorOffset);
    inscribedVector.mult(inscribedMagnitude);

    tesselationVector = PVector.fromAngle(rotation + interiorOffset); 
    tesselationVector.mult(tesselationMagnitude);
  }
  void resize(float newRadius) {
    radius= newRadius;
    update();
    // println(radius);
  }

  void grow() {
    radius++;
    update();
  }
  void grow(float sizeChange) {
    radius = radius + sizeChange;
    update();
  }
  void shrink() {
    radius--;
    update();
  }

  void reorient (float newRotation) {
    rotation = newRotation;
    update();
  }
  void spin(float rotationChange) {
    rotation = rotation + rotationChange;
    update();
  }

  void move(float xChange, float yChange) {
    PVector changeVector = new PVector(xChange, yChange);
    cx = cx + xChange;
    cy = cy + yChange;
    update();
    tesselate();
  }


  void establishVertices() {
    for (int v=0; v < numSides; v++ ) {
      PVector tempVertexVector = new PVector();
      tempVertexVector = vertexVector.get();
      tempVertexVector.rotate(v* baseAngle);
      tempVertexVector.add(center);
      vertices[v]= new PVector(tempVertexVector.x, tempVertexVector.y);
      append(vertices, vertices[v]);
    }
  }

  void display() {
    establishVertices();
    for (int v=0; v < numSides; v++) {
      int tempIndex = v;
      int tempNextIndex = ((v+1)%(numSides));
      line(vertices[tempIndex].x, vertices[tempIndex].y, vertices[tempNextIndex].x, vertices[tempNextIndex].y);
    }
  }
  void tesselate() {
    display();
  }
};






;

