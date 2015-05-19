class SegmentedPolygon extends Polygon { 
  int type = 1;
  float lerpFactor;
  int pointsPerEdge, pointCount, lowerBoundCount, upperBoundCount, baseVertexIndex, baseFloorIndex, baseCeilingIndex, apexFloorIndex, apexCeilingIndex;
  PVector base, baseVertex, apex, nextVertex;
  PVector [] partitionPoints;
  boolean centrality;
  boolean parallel;  

  SegmentedPolygon() {
  }
  SegmentedPolygon(float _cx, float _cy, float _radius, int _numSides, float _rotation, int _pointsPerEdge, boolean _centrality) {
    super(_cx, _cy, _radius, _numSides, _rotation);

    pointsPerEdge = _pointsPerEdge;
    centrality = _centrality;
    // pointCount = pointsPerEdge * numSides;
    parallel = true;


    update();
  }
  SegmentedPolygon(float _cx, float _cy, float _radius, int _numSides, float _rotation, int _pointsPerEdge, boolean _centrality, boolean _parallel) {
    super(_cx, _cy, _radius, _numSides, _rotation);

    pointsPerEdge = _pointsPerEdge;
    centrality = _centrality;
    // pointCount = pointsPerEdge * numSides;
    parallel = _parallel;



    update();
  }

  void setAttributes(float x, float y, float radius, SegmentedPolygon baseObject, float rotation, boolean parallel, boolean centrality) {
    cx =x;
    cy = y;
    radius = radius;
    numSides = baseObject.numSides;
    pointsPerEdge = baseObject.pointsPerEdge;
    centrality = baseObject.centrality;
    rotation = rotation;
    this.parallel = parallel;

    update();
  }


  void addPoints() {

    if (pointsPerEdge <10) {
      pointsPerEdge++;
      update();
    } else if (pointsPerEdge == 10) {
      pointsPerEdge = 1;
      update();
    }
  }
  void subtractPoints() {

    if (pointsPerEdge == 0) {
      pointsPerEdge = 10;
      update();
    } else if (pointsPerEdge > 10) {
      pointsPerEdge--;
      update();
    }
  }

  void update() {

    super.update();
    pointCount = pointsPerEdge * numSides;
    //tln("parallel: "+parallel);
    //tln("pointCount: "+pointCount);

    partitionPoints = new PVector[pointCount];
    nextVertex = new PVector();
    nextVertex = vertexVector.get();
    nextVertex.rotate(baseAngle);
    lerpFactor = pow(pointsPerEdge, (-1));
    float convertedPPE = float(pointsPerEdge);
    // establishVertices();
    establishPartitionPoints();




    upperBoundCount = int(float((pointCount-1)/2)) ;
    lowerBoundCount = upperBoundCount;
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
  void setApexValues() {
    establishVertices();
    establishPartitionPoints();

    if (parallel == true) {
      if ((numSides % 2) == 0) {
        if ((pointsPerEdge % 2) == 0) {
          //tln("T/E/E");
          apexFloorIndex = ((pointsPerEdge/2)% pointCount);
          apexCeilingIndex = ((pointsPerEdge/2) % pointCount);
          apex = partitionPoints[(pointsPerEdge/2)];
          baseFloorIndex = apexFloorIndex + (pointCount/2);
          baseCeilingIndex = apexCeilingIndex + (pointCount/2);
          base = partitionPoints[((pointCount+pointsPerEdge)/2)];
        } else if ((pointsPerEdge%2)!=0) {
          //tln("T/E/O");
          apexFloorIndex = floor((float(pointsPerEdge))/2);
          apexCeilingIndex = ceil((float(pointsPerEdge))/2);
          apex = PVector.lerp(partitionPoints[apexFloorIndex], partitionPoints[apexCeilingIndex], .5);
          baseFloorIndex = apexFloorIndex + (pointCount/2);
          baseCeilingIndex = apexCeilingIndex + (pointCount/2);
          base = PVector.lerp(partitionPoints[((baseFloorIndex%pointCount))], partitionPoints[((baseCeilingIndex)%pointCount)], .5);
        }
      } else if ((numSides%2) != 0) {
        apexFloorIndex = ((pointCount -1) % pointCount);
        apexCeilingIndex = ((pointCount +1) % pointCount);
        apex = partitionPoints [0];

        if ((pointsPerEdge % 2) == 0) {
          //tln("T/O/E");
          baseFloorIndex = apexFloorIndex + (pointCount/2);
          baseCeilingIndex = apexCeilingIndex = (pointCount/2);
          base = partitionPoints [(pointCount/2)];
        } else if ((pointsPerEdge%2)!=0) {
          //tln("T/O/O");
          baseFloorIndex = apexFloorIndex + (ceil((float(pointsPerEdge))/2));
          baseCeilingIndex =  apexCeilingIndex + (floor((float(pointsPerEdge))/2));
          base = PVector.lerp(partitionPoints[((baseFloorIndex%pointCount))], partitionPoints[((baseCeilingIndex)%pointCount)], .5);
        }
      }
    } else if (parallel == false) { 
      //tln("parallel: "+parallel);
      apexFloorIndex = (pointCount -1) % pointCount;
      apexCeilingIndex = (pointCount + 1) % pointCount;
      apex = partitionPoints[0];
      if ((numSides%2) != 0) {
        //tln("F/O/");
        if ((pointsPerEdge % 2) == 0) {
          //tln("F/O/E");
          baseFloorIndex = apexFloorIndex + (pointCount/2);
          baseCeilingIndex = apexCeilingIndex + (pointCount /2);
          base = partitionPoints[(pointCount/2)];
        } else if ((pointsPerEdge % 2) !=0 ) {
          //tln("F/O/O");

          baseFloorIndex =    (apexFloorIndex + ceil((float(pointCount))/2)) %pointCount;
          baseCeilingIndex = ( apexCeilingIndex + floor((float(pointCount))/2)) % pointCount;
          base = PVector.lerp(partitionPoints[baseFloorIndex], partitionPoints[baseCeilingIndex], .5);
        }
      } else if ((numSides % 2) == 0) {
        //tln("F/E");
        baseFloorIndex = apexFloorIndex + (pointCount/2);
        baseCeilingIndex = apexCeilingIndex + (pointCount /2);
        base = partitionPoints[(pointCount/2)];
      }
      
    }

    if (centrality == true) {
      apexFloorIndex = (pointCount -1) % pointCount;
      apexCeilingIndex = (pointCount +1 ) % pointCount;
      base = new PVector(center.x, center.y);
      // apex = new PVector(center.x, center.y);
      apex = new PVector(vertices[0].x, vertices[0].y);
    }
  }
  // }
  /**/


  void displaySegments() {


    // //tln("partitionPoints[1]: "+partitionPoints[1]);

    establishPartitionPoints();
    setApexValues();
    //ellipse(vertices[0].x, vertices[0].y, 20, 20);
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


    for (int a = 1; a< (upperBoundCount); a+=2) {

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

  void tesselate() {


    displaySegments();
  }
};

