import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class endogeny0 extends PApplet {

Polygon eg;
SegmentedPolygon seg;
Tesselation egT, midNightBiscuit, metaSeg, betaSeg, gammaSeg;
BaseObjectFactory fact = new BaseObjectFactory();
RhomboidFiller rF;

public void setup() {
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

public void draw() {
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

class BaseObjectFactory {

  public Polygon getObject(float x, float y, float radius, Polygon baseObject, float rotation, boolean parallel, boolean centrality) {

    Polygon newShape = null;

    if (baseObject instanceof Tesselation) {
      return new Tesselation(x, y, ((Tesselation)baseObject).baseObject, ((Tesselation)baseObject).depth, ((Tesselation)baseObject).parallel );
    } else if (baseObject instanceof SegmentedPolygon) {
      return new SegmentedPolygon(x, y, radius, ((SegmentedPolygon)baseObject).numSides, rotation, ((SegmentedPolygon)baseObject).pointsPerEdge, centrality, parallel );
    } else if (baseObject instanceof Polygon) {
      return new Polygon(x, y, radius, baseObject.numSides, rotation);
    } else return null;
  }
};

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


  public void update() {
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

class FillerFactory {

  public Polygon getFiller(float x, float y, Tesselation baseTess, float rotation) {
    Polygon baseObject = baseTess.baseObject;
    Polygon nullFiller = null;


    if (baseTess.numSides >  4 ) {

      if (baseObject  instanceof Tesselation) {
        return   baseTess.recessFactory.getFiller(x, y, ((Tesselation)(baseTess.baseObject)), rotation );
      } else if (baseObject instanceof SegmentedPolygon) {
        return new SegmentedRhomboid(x, y, baseTess, rotation);
      } else return new RhomboidFiller(x, y, baseTess, rotation);
    } else if (baseTess.numSides <=  4 ) {


      if (baseObject  instanceof Tesselation) {
        return baseTess.recessFactory.getFiller(x, y, ((Tesselation)(baseTess.baseObject)), rotation );
      } else if (baseObject instanceof SegmentedPolygon) {
        return new SegmentedFiller(x, y, baseTess, rotation);
      } else 
        return new Filler(x, y, baseTess, rotation);
    } else return null;
  }
};

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

  public void update() {
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
  public void resize(float newRadius) {
    radius= newRadius;
    update();
    // println(radius);
  }

  public void grow() {
    radius++;
    update();
  }
  public void grow(float sizeChange) {
    radius = radius + sizeChange;
    update();
  }
  public void shrink() {
    radius--;
    update();
  }

  public void reorient (float newRotation) {
    rotation = newRotation;
    update();
  }
  public void spin(float rotationChange) {
    rotation = rotation + rotationChange;
    update();
  }

  public void move(float xChange, float yChange) {
    PVector changeVector = new PVector(xChange, yChange);
    cx = cx + xChange;
    cy = cy + yChange;
    update();
    tesselate();
  }


  public void establishVertices() {
    for (int v=0; v < numSides; v++ ) {
      PVector tempVertexVector = new PVector();
      tempVertexVector = vertexVector.get();
      tempVertexVector.rotate(v* baseAngle);
      tempVertexVector.add(center);
      vertices[v]= new PVector(tempVertexVector.x, tempVertexVector.y);
      append(vertices, vertices[v]);
    }
  }

  public void display() {
    establishVertices();
    for (int v=0; v < numSides; v++) {
      int tempIndex = v;
      int tempNextIndex = ((v+1)%(numSides));
      line(vertices[tempIndex].x, vertices[tempIndex].y, vertices[tempNextIndex].x, vertices[tempNextIndex].y);
    }
  }
  public void tesselate() {
    display();
  }
};






;

/*abstract class PolygonAbstract implements Tesselable{
 int type;
 int numSides;
 float cx, cy, radius, newRadius, rotation, inscribedMagnitude, circumMagnitude, baseAngle, interiorOffset, tesselationMagnitude;
 PVector center, vertexVector, inscribedVector, circumVector, tesselationVector;
 PVector [] vertices;
 PolygonAbstract [] surround;
 PolygonAbstract [] central;
 
 PolygonAbstract(){}
 
 abstract void tesselate();
 abstract void resize(float newRadius);
 abstract void reorient (float newRotation);
 abstract void update();
 abstract void setAttributes();
 abstract void setAttributes(float a, float b, float c, PolygonAbstract d, float e, boolean f, boolean g);
 
 };
 */
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
    // nearRadius = baseRadius/2;
    nearVector = new PVector();
    farVector = new PVector();

    update();
  }


  public void update() {
    //super.update();
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
    // println("nearVector" nearVector);
    //println("nearVector: "+ nearVector);

    vertices = new PVector[numSides];
    surround = new Polygon[numSides];
  }


  public void establishVertices() {
    for (int v = 0; v < numSides; v+=2) {
      PVector nearCopy = new PVector();
      //baseRadius = baseTess.radius;
      // println(farVector);

      nearCopy = nearVector.get();
      // println(nearCopy);
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
      // println(nearCopy);
      // println(vertices[v]);
    }
  }

  public void display() {
    float depthColor = (baseTess.depth)*64/255;
    //fill(00, 255, 00, 128);
    establishVertices();
    // line(center.x, center.y, baseTess.center.x, baseTess.center.y);
    beginShape();

    for (int v=0; v < numSides; v++) {
      vertex(vertices[v].x, vertices[v].y);
      // int tempIndex = v;
      // int tempNextIndex = ((v+1)%(numSides));
      // line(vertices[tempIndex].x, vertices[tempIndex].y, vertices[tempNextIndex].x, vertices[tempNextIndex].y);
    }
    endShape(CLOSE);
  }
  public void tesselate() {
    display();
  }
};

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


  public void update() {
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

  public void setAttributes(float x, float y, float radius, SegmentedPolygon baseObject, float rotation, boolean parallel, boolean centrality) {
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


  public void addPoints() {

    if (pointsPerEdge <10) {
      pointsPerEdge++;
      update();
    } else if (pointsPerEdge == 10) {
      pointsPerEdge = 1;
      update();
    }
  }
  public void subtractPoints() {

    if (pointsPerEdge == 0) {
      pointsPerEdge = 10;
      update();
    } else if (pointsPerEdge > 10) {
      pointsPerEdge--;
      update();
    }
  }

  public void update() {

    super.update();
    pointCount = pointsPerEdge * numSides;
    //tln("parallel: "+parallel);
    //tln("pointCount: "+pointCount);

    partitionPoints = new PVector[pointCount];
    nextVertex = new PVector();
    nextVertex = vertexVector.get();
    nextVertex.rotate(baseAngle);
    lerpFactor = pow(pointsPerEdge, (-1));
    float convertedPPE = PApplet.parseFloat(pointsPerEdge);
    // establishVertices();
    establishPartitionPoints();




    upperBoundCount = PApplet.parseInt(PApplet.parseFloat((pointCount-1)/2)) ;
    lowerBoundCount = upperBoundCount;
  }


  public void establishPartitionPoints() {
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
  public void setApexValues() {
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
          apexFloorIndex = floor((PApplet.parseFloat(pointsPerEdge))/2);
          apexCeilingIndex = ceil((PApplet.parseFloat(pointsPerEdge))/2);
          apex = PVector.lerp(partitionPoints[apexFloorIndex], partitionPoints[apexCeilingIndex], .5f);
          baseFloorIndex = apexFloorIndex + (pointCount/2);
          baseCeilingIndex = apexCeilingIndex + (pointCount/2);
          base = PVector.lerp(partitionPoints[((baseFloorIndex%pointCount))], partitionPoints[((baseCeilingIndex)%pointCount)], .5f);
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
          baseFloorIndex = apexFloorIndex + (ceil((PApplet.parseFloat(pointsPerEdge))/2));
          baseCeilingIndex =  apexCeilingIndex + (floor((PApplet.parseFloat(pointsPerEdge))/2));
          base = PVector.lerp(partitionPoints[((baseFloorIndex%pointCount))], partitionPoints[((baseCeilingIndex)%pointCount)], .5f);
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

          baseFloorIndex =    (apexFloorIndex + ceil((PApplet.parseFloat(pointCount))/2)) %pointCount;
          baseCeilingIndex = ( apexCeilingIndex + floor((PApplet.parseFloat(pointCount))/2)) % pointCount;
          base = PVector.lerp(partitionPoints[baseFloorIndex], partitionPoints[baseCeilingIndex], .5f);
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


  public void displaySegments() {


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

  public void tesselate() {


    displaySegments();
  }
};

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



  public void update() {
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
    float convertedPPE = PApplet.parseFloat(pointsPerEdge);
    if (pointsPerEdge == 0) {
      apexFloorIndex = floor(PApplet.parseFloat(pointsPerEdge)/2) - 1;
      apexCeilingIndex = ceil(PApplet.parseFloat(pointsPerEdge)/2) + 1;
    } else if ((pointsPerEdge%2) == 0) {
      apexFloorIndex = ((pointCount+1)%pointCount);
      apexCeilingIndex = ((pointCount-1)%pointCount);
    } else if ((pointsPerEdge % 2)!= 0) {
      apexFloorIndex = ((pointCount+1)%pointCount);
      apexCeilingIndex = ((pointCount-1)%pointCount);
    }
    baseVertexIndex = ceil(PApplet.parseFloat(numSides/2));
    upperBoundCount = PApplet.parseInt(PApplet.parseFloat((pointCount+2)/2));
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


  public void establishVertices() {
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


  public void establishPartitionPoints() {
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

  public void displaySegments() {
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

interface Tesselable {
  public void tesselate();
}

class Tesselation extends Polygon {
  int type = 2;
  int depth;
  float endoMagnitude, exoMagnitude, centralRadius, periRadius, centralRotation, periRotation;
  boolean parallel;
  PVector endoVector, exoVector;
  Polygon baseObject, central;
  Polygon [] peripherals;
  BaseObjectFactory objectFactory;
  FillerFactory   recessFactory;
  Polygon [] fillers;
  PVector fillerVector;
  float fillerMagnitude;

  Tesselation() {
  }

  Tesselation(float _cx, float _cy, Polygon _baseObject, int _depth, boolean _parallel) {

    cx = _cx; 
    cy = _cy;
    baseObject = (Polygon)_baseObject;
    depth = _depth;
    parallel = _parallel;
    numSides = baseObject.numSides;

    baseAngle = (TWO_PI) / (numSides);
    interiorOffset = baseObject.interiorOffset;  

    if (parallel == true) {

      radius = baseObject.radius*(pow(3, depth));
      rotation = baseObject.rotation;
    } else if (parallel == false) {

      radius = baseObject.radius * pow((3*sin((PI - baseAngle)/2)), depth);
      rotation =  baseObject.rotation + (interiorOffset * (depth));
    }
    setFillerMagnitude();


    update();
  }


  public void update() {
    objectFactory = new BaseObjectFactory();
    recessFactory = new FillerFactory();

    peripherals = new Polygon[numSides];
    super.update();

    if (parallel == true) {
      inscribedMagnitude = (radius* sin((PI - baseAngle)/2));
      exoMagnitude = inscribedMagnitude * 2;
      endoMagnitude = (inscribedMagnitude * 2)/3;
      centralRadius = radius/3;
      periRadius = radius/3;
      centralRotation = rotation;
      periRotation = rotation;

      endoVector = PVector.fromAngle(rotation + interiorOffset);
      endoVector.mult(endoMagnitude);
      exoVector = PVector.fromAngle(rotation + interiorOffset);
      exoVector.mult(exoMagnitude);
    } else if (parallel == false) {
      inscribedMagnitude = (radius* sin((PI - baseAngle)/2));
      exoMagnitude = (inscribedMagnitude) * 2;
      endoMagnitude = (radius * 2)/3; 
      centralRadius = radius/(3 * sin((PI - baseAngle)/2));
      periRadius = centralRadius * sin((PI - baseAngle)/2);
      centralRotation = rotation - interiorOffset;
      periRotation = rotation;

      endoVector = PVector.fromAngle(rotation);
      endoVector.mult(endoMagnitude);
      exoVector = PVector.fromAngle(rotation + interiorOffset);
      exoVector.mult(exoMagnitude);
    }
    fillers = new Polygon[numSides];
    fillerVector = PVector.fromAngle(rotation);
    fillerVector.mult((radius*5)/6);


    // println("center: "+center);
    // println("fillerVector: "+fillerVector);
  }

  public void setFillerMagnitude() {
    if (numSides<= 4) {
      fillerMagnitude = radius*(2/3);
    } else {
      fillerMagnitude = radius*(5/6);
    } 
    println("fillerMagnitude: "+fillerMagnitude);
  }
  public void spin(float rotationChange) {
    rotation = rotation + rotationChange;
    update();
    tesselate();
  }

  public void plugHoles() {
    for (int r = 0; r < numSides; ++r) {
      PVector fillerCopy = new PVector();
      fillerCopy = fillerVector.get();
      fillerCopy.rotate(r*baseAngle);
      fillerCopy.add(center);
      fillers[r] = recessFactory.getFiller(fillerCopy.x, fillerCopy.y, this, ((r*baseAngle)));
      append(fillers, fillers[r]);
      println("filler rad" + degrees((fillers[r].rotation)));
      //  fillers[r].reorient((r*((baseAngle))));

      fillers[r].tesselate();
    }
  }
  /*
  void spin(float rotationChange) {
   super.spin(rotationChange);
   }*/
  public void tesselate() {
    // super.tesselate();
    if (depth > 1) {
      if (parallel == true) {
              plugHoles();

      }

      central = new Tesselation(center.x, center.y, baseObject, depth-1, parallel);
      central.resize(centralRadius);
      central.reorient(centralRotation);

      central.tesselate();

      for (int t = 0; t < numSides; ++t) {
        PVector  endoInstance = new PVector(); 
        endoInstance = endoVector.get();
        endoInstance.rotate(t*baseAngle);
        endoInstance.add(center);
        peripherals[t]= new Tesselation(endoInstance.x, endoInstance.y, baseObject, depth-1, parallel);
        peripherals[t].resize(periRadius);
        peripherals[t].reorient(periRotation );
        append(peripherals, peripherals[t]);
        peripherals[t].tesselate();
      }
    } else if (depth == 1) {
       if (parallel == true) {
              plugHoles();

      }

      central = objectFactory.getObject(center.x, center.y, centralRadius, baseObject, centralRotation, parallel, true);
      central.resize(centralRadius);
      central.reorient(centralRotation);

      central.tesselate();

      for (int t = 0; t < numSides; ++t) {
        PVector  endoInstance = new PVector(); 
        endoInstance = endoVector.get();
        endoInstance.rotate(t*baseAngle);
        endoInstance.add(center);
        peripherals[t] = objectFactory.getObject(endoInstance.x, endoInstance.y, periRadius, baseObject, periRotation, parallel, false);
        peripherals[t].resize(periRadius );
        peripherals[t].reorient(periRotation + ((t)*baseAngle));
        append(peripherals, peripherals[t]);
        peripherals[t].tesselate();
      }
    } else if (depth == 0) {
      central = objectFactory.getObject(center.x, center.y, (baseObject.radius), baseObject, (baseObject.rotation), parallel, true);
      central.tesselate();
    }
  }
};

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "endogeny0" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
