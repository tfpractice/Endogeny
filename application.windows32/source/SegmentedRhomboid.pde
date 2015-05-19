class SegmentedRhomboid extends SegmentedFiller {
  float nearRadius, farRadius;
  PVector nearVector, farVector;

  SegmentedRhomboid(float _cx, float _cy, Tesselation _baseTess, float _rotation) {

    cx = _cx; 
    cy = _cy;
    baseTess = _baseTess;
    rotation =_rotation;
   int origPPE = ((SegmentedPolygon)(baseTess.baseObject)).pointsPerEdge;
    pointsPerEdge = (int)(origPPE*( baseTess.depth));
   // pointsPerEdge = ((SegmentedPolygon)(baseTess.baseObject)).pointsPerEdge;
    // pointsPerEdge = (int)pow(3, baseTess.depth);
    numSides = 4;
    centrality = false;

    update();
  }



  void update() {
    baseRadius = baseTess.radius;
    numSides = 4;
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
    baseRadius = baseTess.radius;
    center = new PVector(cx, cy);
    pointCount = pointsPerEdge * numSides;
    partitionPoints = new PVector[pointCount];

    lerpFactor = pow(pointsPerEdge, (-1));
    float convertedPPE = float(pointsPerEdge);
    if (pointsPerEdge == 0) {
      apexFloorIndex = floor(float(pointsPerEdge)/2) - 1;
      apexCeilingIndex = ceil(float(pointsPerEdge)/2) + 1;
    } else if ((pointsPerEdge%2) == 0) {
      apexFloorIndex = ((pointCount+1)%pointCount);
      apexCeilingIndex = ((pointCount-1)%pointCount);
    } else if ((pointsPerEdge % 2)!= 0) {
      apexFloorIndex = ((pointCount+1)%pointCount);
      apexCeilingIndex = ((pointCount-1)%pointCount);
    }
    baseVertexIndex = ceil(float(numSides/2));
    upperBoundCount = int(float((pointCount+2)/2));
    lowerBoundCount = upperBoundCount;

    establishVertices();
    establishPartitionPoints();
    // if (centrality == true) {
    //   base = new PVector(center.x, center.y);
    // } else {
    //   base = new  PVector(vertices[baseVertexIndex].x, vertices[baseVertexIndex].y);
    // }
    //base = PVector.lerp(vertices[baseVertexIndex], vertices[((baseVertexIndex +1)% numSides)], .5);
    base = new PVector(vertices[baseVertexIndex].x, vertices[baseVertexIndex].y);

    // apex = PVector.lerp(vertices[((apexFloorIndex + numSides)%numSides)], vertices[((apexCeilingIndex + numSides)%numSides)], .5);

    // base = new  PVector(vertices[baseVertexIndex].x, vertices[baseVertexIndex].y);
    apex = new PVector(vertices[0].x, vertices[0].y);
  }


  void establishVertices() {
    for (int v = 0; v < numSides; v+=2) {
      PVector nearCopy = new PVector();
      //baseRadius = baseTess.radius;
      //   println(nearVector);

      nearCopy = nearVector.get();
      //   println(nearCopy);
      nearCopy.rotate(v*(PI/2));
      PVector farCopy = new PVector();
      farCopy = farVector.get();
      farCopy.rotate(v*(PI/2));

      nearCopy.add(center);
      farCopy.add(center);


      vertices[v] = new PVector(nearCopy.x, nearCopy.y);
      vertices[v+1] = new PVector(farCopy.x, farCopy.y);
    }
  }


  void establishPartitionPoints() {
    establishVertices();
    partitionPoints = new PVector[pointCount];

    for (int v = 0; v < numSides; v++) {
      PVector tempCenter = new PVector();
      tempCenter = center.get();
      PVector tempVertex = new PVector();
      // tempVertex = vertexVector.get();
      tempVertex = vertices[v].get();
      // tempVertex.rotate(v* baseAngle);
      // tempVertex.add(center);
      PVector tempNextVertex = new PVector();
      // tempNextVertex = nextVertex.get();
      tempNextVertex = vertices[((v+1)%numSides)].get();
      // tempNextVertex.rotate(v*baseAngle);
      // tempNextVertex.add(center);
      for (int p =0; p< pointsPerEdge; p++) {
        int pointIndex = ((pointsPerEdge * v)+ p);
        float instanceLerp = p * lerpFactor;
        partitionPoints[pointIndex] =  PVector.lerp(tempVertex, tempNextVertex, instanceLerp);

        append(partitionPoints, partitionPoints[pointIndex]);
      }
    }
  }

  /*
  void establishPartitionPoints() {
   for (int v = 0; v < numSides; v++) {
   
   if (v % 2 == 0) {
   nextVertex = new PVector();
   nextVertex = farVector.get();
   PVector tempCenter = new PVector();
   tempCenter = center.get();
   PVector tempVertex = new PVector();
   tempVertex = nearVector.get();
   tempVertex.rotate(v* (PI/2));
   tempVertex.add(center);
   PVector tempNextVertex = new PVector();
   tempNextVertex = farVector.get();
   tempNextVertex.rotate(v*(PI/2));
   tempNextVertex.add(center);
   for (int p =0; p< pointsPerEdge; p++) {
   int pointIndex = ((pointsPerEdge * v)+ p);
   float instanceLerp = p * lerpFactor;
   partitionPoints[pointIndex] =  PVector.lerp(tempVertex, tempNextVertex, instanceLerp);
   append(partitionPoints, partitionPoints[pointIndex]);
   }
   } else {   
   nextVertex = new PVector();
   nextVertex = nearVector.get();
   PVector tempCenter = new PVector();
   tempCenter = center.get();
   PVector tempVertex = new PVector();
   tempVertex = farVector.get();
   tempVertex.rotate(v* (PI/2));
   tempVertex.add(center);
   PVector tempNextVertex = new PVector();
   tempNextVertex = nearVector.get();
   tempNextVertex.rotate(v*(PI/2));
   tempNextVertex.add(center);
   for (int p =0; p< pointsPerEdge; p++) {
   int pointIndex = ((pointsPerEdge * v)+ p);
   float instanceLerp = p * lerpFactor;
   partitionPoints[pointIndex] =  PVector.lerp(tempVertex, tempNextVertex, instanceLerp);
   append(partitionPoints, partitionPoints[pointIndex]);
   }
   }
   //   println(partitionPoints);
   }
   }
   */

  void displaySegments() {
    establishPartitionPoints();
    beginShape();

    vertex(base.x, base.y);
    vertex(apex.x, apex.y);
    vertex(partitionPoints[apexFloorIndex].x, partitionPoints[apexFloorIndex].y);
    endShape(CLOSE);
    beginShape();

    vertex(base.x, base.y);
    vertex(apex.x, apex.y);
    vertex(partitionPoints[apexCeilingIndex].x, partitionPoints[apexCeilingIndex].y);
    endShape(CLOSE);


    for (int a = 1; a< upperBoundCount; a+=2) {

      int augmentedCeilingIndex = apexCeilingIndex + pointCount;
      int augmentedFloorIndex = apexFloorIndex + pointCount;
      beginShape();
      vertex(base.x, base.y);
      vertex(partitionPoints[((augmentedCeilingIndex+a)%pointCount)].x, partitionPoints[((augmentedCeilingIndex+a)%pointCount)].y);
      vertex(partitionPoints[((augmentedCeilingIndex+ (a+1))%pointCount)].x, partitionPoints[((augmentedCeilingIndex+ (a+1))%pointCount)].y);
      endShape(CLOSE);

      beginShape();

      vertex(base.x, base.y);
      vertex(partitionPoints[((augmentedFloorIndex - a)% pointCount)].x, partitionPoints[((augmentedFloorIndex -a ) %pointCount)].y);
      vertex(partitionPoints[((augmentedFloorIndex - (a+1))%pointCount)].x, partitionPoints[((augmentedFloorIndex - (a+1))%pointCount)].y);
      endShape(CLOSE);
    };
  };
};

