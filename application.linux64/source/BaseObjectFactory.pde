class BaseObjectFactory {

  Polygon getObject(float x, float y, float radius, Polygon baseObject, float rotation, boolean parallel, boolean centrality) {

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

