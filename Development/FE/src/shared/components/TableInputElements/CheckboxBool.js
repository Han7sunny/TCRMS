import React, { useReducer, useEffect } from "react";

import "./CheckboxGroup.css";

const inputReducer = (state, action) => {
  switch (action.type) {
    case "CHANGE":
      return {
        ...state,
        value: !state.value,
      };
    case "SET":
      return {
        ...state,
        value: action.val,
      };
    default:
      return state;
  }
};

const CheckboxBool = (props) => {
  const [inputState, dispatch] = useReducer(inputReducer, {
    value: props.initialValue || "",
  });

  const { id, onInput, initialValue, teamId } = props;
  const { value } = inputState;

  useEffect(() => {
    onInput(id, value, true, teamId);
  }, [id, value, onInput, teamId]);

  useEffect(() => {
    dispatch({
      type: "SET",
      val: initialValue,
    });
  }, [initialValue]);

  const changeHandler = (event) => {
    dispatch({
      type: "CHANGE",
      val: event.target.value,
    });
  };

  return (
    <div id={id} className={`table-checkbox `}>
      <label>
        <input
          type="checkbox"
          name={id}
          value={props.item}
          checked={inputState.value === props.item}
          onChange={changeHandler}
          readOnly={props.readonly}
          disabled={props.disabled}
        />
        {props.showLabel && props.item}
      </label>
    </div>
  );
};

export default CheckboxBool;
