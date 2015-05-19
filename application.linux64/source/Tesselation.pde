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


  void update() {
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

  void setFillerMagnitude() {
    if (numSides<= 4) {
      fillerMagnitude = radius*(2/3);
    } else {
      fillerMagnitude = radius*(5/6);
    } 
    println("fillerMagnitude: "+fillerMagnitude);
  }
  void spin(float rotationChange) {
    rotation = rotation + rotationChange;
    update();
    tesselate();
  }

  void plugHoles() {
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
  void tesselate() {
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

