import React, { useReducer, useEffect } from "react";

// import { validate } from "../../util/validators";
import "./Dropdown.css";

const inputReducer = (state, action) => {
  switch (action.type) {
    case "CHANGE":
      return {
        ...state,
        value: action.val,
        // isValid: validate(action.val, action.validators),
      };
    default:
      return state;
  }
};

const Dropdown = (props) => {
  const [inputState, dispatch] = useReducer(inputReducer, {
    value: props.initialValue || "",
    isValid: props.initialValid || false,
  });

  const { id, onInput, initialValue, validators, teamId } = props;
  const { value, isValid } = inputState;

  useEffect(() => {
    onInput(id, value, isValid, teamId);
  }, [id, value, isValid, onInput, teamId]);

  useEffect(() => {
    dispatch({
      type: "CHANGE",
      val: initialValue,
      validators: validators,
    });
  }, [initialValue, validators]);

  const changeHandler = (event) => {
    if (props.readOnly) {
      return; // If readOnly is true, do nothing on change
    }
    dispatch({
      type: "CHANGE",
      val: event.target.value,
      validators: props.validators,
    });
  };

  return (
    <select
      id={props.id}
      name={props.id}
      className={`table-dropdown `}
      onChange={changeHandler}
      disabled={props.disabled}
      value={inputState.value}
    >
      {props.items.map((item, i) => (
        <option key={item + i} value={item}>
          {item}
        </option>
      ))}
    </select>
  );
};

export default Dropdown;
