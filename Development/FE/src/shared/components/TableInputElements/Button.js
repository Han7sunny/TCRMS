import React from "react";

import "./Button.css";

const Button = (props) => {
  return (
    <button
      id={props.id}
      className={`table-button table-button--${props.size || "default"} ${
        props.inverse && "table-button--inverse"
      } ${props.danger && "table-button--danger"}`}
      type={props.type}
      onClick={props.onClick}
      disabled={props.disabled}
    >
      {props.children}
    </button>
  );
};

export default Button;
