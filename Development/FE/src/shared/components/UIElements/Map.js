import React, { useRef } from "react";

import "./Map.css";

const Map = (props) => {
  const mapRef = useRef();

  // GOOGLD MAP using useEffect

  return (
    <div
      ref={mapRef}
      className={`map ${props.className}`}
      style={props.style}
    ></div>
  );
};

export default Map;
