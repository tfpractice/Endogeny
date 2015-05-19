class FillerFactory {

  Polygon getFiller(float x, float y, Tesselation baseTess, float rotation) {
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

